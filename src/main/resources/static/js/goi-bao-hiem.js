document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    loadGoiBaoHiem();
    
    document.getElementById('goiBaoHiemForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveGoiBaoHiem();
    });
});

async function loadGoiBaoHiem() {
    try {
        const response = await apiGet('/goi-bao-hiem');
        
        if (response.success && response.data) {
            displayGoiBaoHiem(response.data);
        }
    } catch (error) {
        console.error('Error loading goi bao hiem:', error);
        document.getElementById('goiBaoHiemTableBody').innerHTML = 
            '<tr><td colspan="6" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
}

function displayGoiBaoHiem(goiList) {
    const tbody = document.getElementById('goiBaoHiemTableBody');
    
    if (goiList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    tbody.innerHTML = goiList.map(goi => `
        <tr>
            <td>${goi.maGoi}</td>
            <td>${goi.tenGoi}</td>
            <td>${goi.moTa || ''}</td>
            <td>${formatCurrency(goi.phiCoBan)}</td>
            <td>${goi.active ? '✓ Hoạt động' : '✗ Tạm khóa'}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="editGoiBaoHiem(${goi.id})">Sửa</button>
                <button class="btn btn-sm btn-danger" onclick="deleteGoiBaoHiem(${goi.id})">Xóa</button>
            </td>
        </tr>
    `).join('');
}

function openModal(mode, id = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('goiBaoHiemForm');
    const title = document.getElementById('modalTitle');
    
    if (mode === 'create') {
        title.textContent = 'Thêm gói bảo hiểm';
        form.reset();
        document.getElementById('goiBaoHiemId').value = '';
        document.getElementById('active').checked = true;
    } else {
        title.textContent = 'Sửa gói bảo hiểm';
        loadGoiBaoHiemDetail(id);
    }
    
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('errorMessage').classList.remove('show');
}

async function loadGoiBaoHiemDetail(id) {
    try {
        const response = await apiGet(`/goi-bao-hiem/${id}`);
        if (response.success && response.data) {
            const goi = response.data;
            document.getElementById('goiBaoHiemId').value = goi.id;
            document.getElementById('tenGoi').value = goi.tenGoi || '';
            document.getElementById('moTa').value = goi.moTa || '';
            document.getElementById('phiCoBan').value = goi.phiCoBan || '';
            document.getElementById('quyenLoi').value = goi.quyenLoi || '';
            document.getElementById('active').checked = goi.active !== false;
        }
    } catch (error) {
        showError('errorMessage', 'Lỗi khi tải thông tin gói bảo hiểm');
    }
}

async function saveGoiBaoHiem() {
    const id = document.getElementById('goiBaoHiemId').value;
    const formData = {
        tenGoi: document.getElementById('tenGoi').value,
        moTa: document.getElementById('moTa').value,
        phiCoBan: parseFloat(document.getElementById('phiCoBan').value),
        quyenLoi: document.getElementById('quyenLoi').value,
        active: document.getElementById('active').checked
    };
    
    try {
        let response;
        if (id) {
            response = await apiPut(`/goi-bao-hiem/${id}`, formData);
        } else {
            response = await apiPost('/goi-bao-hiem', formData);
        }
        
        if (response.success) {
            closeModal();
            loadGoiBaoHiem();
            alert('Lưu thành công!');
        }
    } catch (error) {
        showError('errorMessage', error.message || 'Lỗi khi lưu gói bảo hiểm');
    }
}

function editGoiBaoHiem(id) {
    openModal('edit', id);
}

async function deleteGoiBaoHiem(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa gói bảo hiểm này?')) {
        return;
    }
    
    try {
        const response = await apiDelete(`/goi-bao-hiem/${id}`);
        if (response.success) {
            loadGoiBaoHiem();
            alert('Xóa thành công!');
        }
    } catch (error) {
        alert('Lỗi khi xóa: ' + (error.message || 'Có lỗi xảy ra'));
    }
}

