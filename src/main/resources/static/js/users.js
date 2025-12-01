let roles = [];

document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    loadRoles();
    loadUsers();
    
    document.getElementById('userForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveUser();
    });
});

async function loadRoles() {
    try {
        const response = await apiGet('/users/roles');
        if (response.success && response.data) {
            roles = response.data;
            const select = document.getElementById('role');
            select.innerHTML = '<option value="">-- Chọn vai trò --</option>' +
                roles.map(role => 
                    `<option value="${role}">${role}</option>`
                ).join('');
        }
    } catch (error) {
        console.error('Error loading roles:', error);
    }
}

async function loadUsers() {
    try {
        const response = await apiGet('/users');
        
        if (response.success && response.data) {
            displayUsers(response.data);
        }
    } catch (error) {
        console.error('Error loading users:', error);
        document.getElementById('userTableBody').innerHTML = 
            '<tr><td colspan="6" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
}

function displayUsers(userList) {
    const tbody = document.getElementById('userTableBody');
    
    if (userList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    tbody.innerHTML = userList.map(user => `
        <tr>
            <td>${user.username}</td>
            <td>${user.hoTen}</td>
            <td>${user.email}</td>
            <td>${user.role}</td>
            <td>${user.active ? '✓ Hoạt động' : '✗ Khóa'}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="editUser(${user.id})">Sửa</button>
                <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.id})">Xóa</button>
            </td>
        </tr>
    `).join('');
}

function openModal(mode, id = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('userForm');
    const title = document.getElementById('modalTitle');
    
    if (mode === 'create') {
        title.textContent = 'Thêm người dùng';
        form.reset();
        document.getElementById('userId').value = '';
        document.getElementById('password').required = true;
        document.getElementById('active').checked = true;
    } else {
        title.textContent = 'Sửa người dùng';
        document.getElementById('password').required = false;
        loadUserDetail(id);
    }
    
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('errorMessage').classList.remove('show');
}

async function loadUserDetail(id) {
    try {
        const response = await apiGet(`/users/${id}`);
        if (response.success && response.data) {
            const user = response.data;
            document.getElementById('userId').value = user.id;
            document.getElementById('username').value = user.username || '';
            document.getElementById('password').value = '';
            document.getElementById('hoTen').value = user.hoTen || '';
            document.getElementById('email').value = user.email || '';
            document.getElementById('soDienThoai').value = user.soDienThoai || '';
            document.getElementById('role').value = user.role || '';
            document.getElementById('active').checked = user.active !== false;
        }
    } catch (error) {
        showError('errorMessage', 'Lỗi khi tải thông tin người dùng');
    }
}

async function saveUser() {
    const id = document.getElementById('userId').value;
    const formData = {
        username: document.getElementById('username').value,
        hoTen: document.getElementById('hoTen').value,
        email: document.getElementById('email').value,
        soDienThoai: document.getElementById('soDienThoai').value,
        role: document.getElementById('role').value,
        active: document.getElementById('active').checked
    };
    
    const password = document.getElementById('password').value;
    if (password) {
        formData.password = password;
    }
    
    try {
        let response;
        if (id) {
            response = await apiPut(`/users/${id}`, formData);
        } else {
            if (!password) {
                showError('errorMessage', 'Password là bắt buộc khi tạo mới');
                return;
            }
            response = await apiPost('/users', formData);
        }
        
        if (response.success) {
            closeModal();
            loadUsers();
            alert('Lưu thành công!');
        }
    } catch (error) {
        showError('errorMessage', error.message || 'Lỗi khi lưu người dùng');
    }
}

function editUser(id) {
    openModal('edit', id);
}

async function deleteUser(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa người dùng này?')) {
        return;
    }
    
    try {
        const response = await apiDelete(`/users/${id}`);
        if (response.success) {
            loadUsers();
            alert('Xóa thành công!');
        }
    } catch (error) {
        alert('Lỗi khi xóa: ' + (error.message || 'Có lỗi xảy ra'));
    }
}

