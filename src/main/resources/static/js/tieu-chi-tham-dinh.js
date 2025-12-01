document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    loadTieuChi();
    
    document.getElementById('tieuChiForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveTieuChi();
    });
});

async function loadTieuChi() {
    try {
        const response = await apiGet('/tieu-chi-tham-dinh');
        
        if (response.success && response.data) {
            displayTieuChi(response.data.sort((a, b) => a.thuTu - b.thuTu));
        }
    } catch (error) {
        console.error('Error loading tieu chi:', error);
        document.getElementById('tieuChiTableBody').innerHTML = 
            '<tr><td colspan="8" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
}

function displayTieuChi(tieuChiList) {
    const tbody = document.getElementById('tieuChiTableBody');
    
    if (tieuChiList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    tbody.innerHTML = tieuChiList.map(tc => `
        <tr>
            <td>${tc.maTieuChi}</td>
            <td>${tc.tenTieuChi}</td>
            <td>${tc.moTa || ''}</td>
            <td>${tc.diemToiDa}</td>
            <td>${tc.thuTu}</td>
            <td>${tc.dieuKien || ''}</td>
            <td>${tc.active ? '✓ Hoạt động' : '✗ Tạm khóa'}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="editTieuChi(${tc.id})">Sửa</button>
                <button class="btn btn-sm btn-danger" onclick="deleteTieuChi(${tc.id})">Xóa</button>
            </td>
        </tr>
    `).join('');
}

function openModal(mode, id = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('tieuChiForm');
    const title = document.getElementById('modalTitle');
    
    if (mode === 'create') {
        title.textContent = 'Thêm tiêu chí';
        form.reset();
        document.getElementById('tieuChiId').value = '';
        document.getElementById('active').checked = true;
    } else {
        title.textContent = 'Sửa tiêu chí';
        loadTieuChiDetail(id);
    }
    
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('errorMessage').classList.remove('show');
}

async function loadTieuChiDetail(id) {
    try {
        const response = await apiGet(`/tieu-chi-tham-dinh/${id}`);
        if (response.success && response.data) {
            const tc = response.data;
            document.getElementById('tieuChiId').value = tc.id;
            document.getElementById('tenTieuChi').value = tc.tenTieuChi || '';
            document.getElementById('moTa').value = tc.moTa || '';
            document.getElementById('diemToiDa').value = tc.diemToiDa || '';
            document.getElementById('thuTu').value = tc.thuTu || '';
            document.getElementById('dieuKien').value = tc.dieuKien || '';
            document.getElementById('active').checked = tc.active !== false;
        }
    } catch (error) {
        showError('errorMessage', 'Lỗi khi tải thông tin tiêu chí');
    }
}

async function saveTieuChi() {
    const id = document.getElementById('tieuChiId').value;
    const formData = {
        tenTieuChi: document.getElementById('tenTieuChi').value,
        moTa: document.getElementById('moTa').value,
        diemToiDa: parseInt(document.getElementById('diemToiDa').value),
        thuTu: parseInt(document.getElementById('thuTu').value),
        dieuKien: document.getElementById('dieuKien').value,
        active: document.getElementById('active').checked
    };
    
    try {
        let response;
        if (id) {
            response = await apiPut(`/tieu-chi-tham-dinh/${id}`, formData);
        } else {
            response = await apiPost('/tieu-chi-tham-dinh', formData);
        }
        
        if (response.success) {
            closeModal();
            loadTieuChi();
            alert('Lưu thành công!');
        }
    } catch (error) {
        showError('errorMessage', error.message || 'Lỗi khi lưu tiêu chí');
    }
}

function editTieuChi(id) {
    openModal('edit', id);
}

async function deleteTieuChi(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa tiêu chí này?')) {
        return;
    }
    
    try {
        const response = await apiDelete(`/tieu-chi-tham-dinh/${id}`);
        if (response.success) {
            loadTieuChi();
            alert('Xóa thành công!');
        }
    } catch (error) {
        alert('Lỗi khi xóa: ' + (error.message || 'Có lỗi xảy ra'));
    }
}

