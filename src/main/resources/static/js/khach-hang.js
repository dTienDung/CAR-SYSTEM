document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    loadKhachHang();
    
    document.getElementById('khachHangForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveKhachHang();
    });
});

async function loadKhachHang(keyword = '') {
    try {
        const endpoint = keyword ? `/khach-hang?keyword=${encodeURIComponent(keyword)}` : '/khach-hang';
        const response = await apiGet(endpoint);
        
        if (response.success && response.data) {
            displayKhachHang(response.data);
        }
    } catch (error) {
        console.error('Error loading khach hang:', error);
        document.getElementById('khachHangTableBody').innerHTML = 
            '<tr><td colspan="7" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
}

function displayKhachHang(khachHangList) {
    const tbody = document.getElementById('khachHangTableBody');
    
    if (khachHangList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    tbody.innerHTML = khachHangList.map(kh => `
        <tr>
            <td>${kh.maKH}</td>
            <td>${kh.hoTen}</td>
            <td>${kh.cccd}</td>
            <td>${kh.email}</td>
            <td>${kh.soDienThoai}</td>
            <td>${formatDate(kh.ngaySinh)}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="editKhachHang(${kh.id})">Sửa</button>
                <button class="btn btn-sm btn-danger" onclick="deleteKhachHang(${kh.id})">Xóa</button>
            </td>
        </tr>
    `).join('');
}

function searchKhachHang() {
    const keyword = document.getElementById('searchInput').value;
    loadKhachHang(keyword);
}

function openModal(mode, id = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('khachHangForm');
    const title = document.getElementById('modalTitle');
    
    if (mode === 'create') {
        title.textContent = 'Thêm khách hàng';
        form.reset();
        document.getElementById('khachHangId').value = '';
    } else {
        title.textContent = 'Sửa khách hàng';
        loadKhachHangDetail(id);
    }
    
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('errorMessage').classList.remove('show');
}

async function loadKhachHangDetail(id) {
    try {
        const response = await apiGet(`/khach-hang/${id}`);
        if (response.success && response.data) {
            const kh = response.data;
            document.getElementById('khachHangId').value = kh.id;
            document.getElementById('hoTen').value = kh.hoTen || '';
            document.getElementById('cccd').value = kh.cccd || '';
            document.getElementById('ngaySinh').value = kh.ngaySinh ? kh.ngaySinh.split('T')[0] : '';
            document.getElementById('gioiTinh').value = kh.gioiTinh || '';
            document.getElementById('email').value = kh.email || '';
            document.getElementById('soDienThoai').value = kh.soDienThoai || '';
            document.getElementById('diaChi').value = kh.diaChi || '';
            document.getElementById('ngheNghiep').value = kh.ngheNghiep || '';
        }
    } catch (error) {
        showError('errorMessage', 'Lỗi khi tải thông tin khách hàng');
    }
}

async function saveKhachHang() {
    const id = document.getElementById('khachHangId').value;
    const formData = {
        hoTen: document.getElementById('hoTen').value,
        cccd: document.getElementById('cccd').value,
        ngaySinh: document.getElementById('ngaySinh').value,
        gioiTinh: document.getElementById('gioiTinh').value,
        email: document.getElementById('email').value,
        soDienThoai: document.getElementById('soDienThoai').value,
        diaChi: document.getElementById('diaChi').value,
        ngheNghiep: document.getElementById('ngheNghiep').value
    };
    
    try {
        let response;
        if (id) {
            response = await apiPut(`/khach-hang/${id}`, formData);
        } else {
            response = await apiPost('/khach-hang', formData);
        }
        
        if (response.success) {
            closeModal();
            loadKhachHang();
            alert('Lưu thành công!');
        }
    } catch (error) {
        showError('errorMessage', error.message || 'Lỗi khi lưu khách hàng');
    }
}

function editKhachHang(id) {
    openModal('edit', id);
}

async function deleteKhachHang(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa khách hàng này?')) {
        return;
    }
    
    try {
        const response = await apiDelete(`/khach-hang/${id}`);
        if (response.success) {
            loadKhachHang();
            alert('Xóa thành công!');
        }
    } catch (error) {
        alert('Lỗi khi xóa: ' + (error.message || 'Có lỗi xảy ra'));
    }
}

