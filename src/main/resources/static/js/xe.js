let khachHangList = [];

document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    loadKhachHangList();
    loadXe();
    
    document.getElementById('xeForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveXe();
    });
});

async function loadKhachHangList() {
    try {
        const response = await apiGet('/khach-hang');
        if (response.success && response.data) {
            khachHangList = response.data;
            const select = document.getElementById('khachHangId');
            select.innerHTML = '<option value="">-- Chọn khách hàng --</option>' +
                khachHangList.map(kh => 
                    `<option value="${kh.id}">${kh.maKH} - ${kh.hoTen}</option>`
                ).join('');
        }
    } catch (error) {
        console.error('Error loading khach hang list:', error);
    }
}

async function loadXe(keyword = '') {
    try {
        const endpoint = keyword ? `/xe?keyword=${encodeURIComponent(keyword)}` : '/xe';
        const response = await apiGet(endpoint);
        
        if (response.success && response.data) {
            displayXe(response.data);
        }
    } catch (error) {
        console.error('Error loading xe:', error);
        document.getElementById('xeTableBody').innerHTML = 
            '<tr><td colspan="7" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
}

function displayXe(xeList) {
    const tbody = document.getElementById('xeTableBody');
    
    if (xeList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    tbody.innerHTML = xeList.map(xe => `
        <tr>
            <td>${xe.maXe}</td>
            <td>${xe.bienSo}</td>
            <td>${xe.hangXe} ${xe.dongXe}</td>
            <td>${xe.khachHang ? xe.khachHang.hoTen : 'N/A'}</td>
            <td>${xe.namSanXuat}</td>
            <td>${formatCurrency(xe.giaTriXe)}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="editXe(${xe.id})">Sửa</button>
                <button class="btn btn-sm btn-danger" onclick="deleteXe(${xe.id})">Xóa</button>
            </td>
        </tr>
    `).join('');
}

function searchXe() {
    const keyword = document.getElementById('searchInput').value;
    loadXe(keyword);
}

function openModal(mode, id = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('xeForm');
    const title = document.getElementById('modalTitle');
    
    if (mode === 'create') {
        title.textContent = 'Thêm xe';
        form.reset();
        document.getElementById('xeId').value = '';
    } else {
        title.textContent = 'Sửa xe';
        loadXeDetail(id);
    }
    
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('errorMessage').classList.remove('show');
}

async function loadXeDetail(id) {
    try {
        const response = await apiGet(`/xe/${id}`);
        if (response.success && response.data) {
            const xe = response.data;
            document.getElementById('xeId').value = xe.id;
            document.getElementById('khachHangId').value = xe.khachHang ? xe.khachHang.id : '';
            document.getElementById('bienSo').value = xe.bienSo || '';
            document.getElementById('soKhung').value = xe.soKhung || '';
            document.getElementById('soMay').value = xe.soMay || '';
            document.getElementById('hangXe').value = xe.hangXe || '';
            document.getElementById('dongXe').value = xe.dongXe || '';
            document.getElementById('namSanXuat').value = xe.namSanXuat || '';
            document.getElementById('namDangKy').value = xe.namDangKy || '';
            document.getElementById('mauSac').value = xe.mauSac || '';
            document.getElementById('mucDichSuDung').value = xe.mucDichSuDung || '';
            document.getElementById('giaTriXe').value = xe.giaTriXe || '';
            document.getElementById('thongTinKyThuat').value = xe.thongTinKyThuat || '';
        }
    } catch (error) {
        showError('errorMessage', 'Lỗi khi tải thông tin xe');
    }
}

async function saveXe() {
    const id = document.getElementById('xeId').value;
    const formData = {
        khachHangId: parseInt(document.getElementById('khachHangId').value),
        bienSo: document.getElementById('bienSo').value,
        soKhung: document.getElementById('soKhung').value,
        soMay: document.getElementById('soMay').value,
        hangXe: document.getElementById('hangXe').value,
        dongXe: document.getElementById('dongXe').value,
        namSanXuat: parseInt(document.getElementById('namSanXuat').value),
        namDangKy: parseInt(document.getElementById('namDangKy').value),
        mauSac: document.getElementById('mauSac').value,
        mucDichSuDung: document.getElementById('mucDichSuDung').value,
        giaTriXe: parseFloat(document.getElementById('giaTriXe').value),
        thongTinKyThuat: document.getElementById('thongTinKyThuat').value
    };
    
    try {
        let response;
        if (id) {
            response = await apiPut(`/xe/${id}`, formData);
        } else {
            response = await apiPost('/xe', formData);
        }
        
        if (response.success) {
            closeModal();
            loadXe();
            alert('Lưu thành công!');
        }
    } catch (error) {
        showError('errorMessage', error.message || 'Lỗi khi lưu xe');
    }
}

function editXe(id) {
    openModal('edit', id);
}

async function deleteXe(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa xe này?')) {
        return;
    }
    
    try {
        const response = await apiDelete(`/xe/${id}`);
        if (response.success) {
            loadXe();
            alert('Xóa thành công!');
        }
    } catch (error) {
        alert('Lỗi khi xóa: ' + (error.message || 'Có lỗi xảy ra'));
    }
}

