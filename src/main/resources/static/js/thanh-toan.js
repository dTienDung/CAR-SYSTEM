let hopDongList = [];

document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    loadHopDongList();
    loadThanhToan();
    
    document.getElementById('thanhToanForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveThanhToan();
    });
    
    document.getElementById('phuongThuc').addEventListener('change', function() {
        const method = this.value;
        document.getElementById('soTaiKhoanGroup').style.display = 
            (method === 'CHUYEN_KHOAN') ? 'block' : 'none';
        document.getElementById('soTheGroup').style.display = 
            (method === 'POS_THE') ? 'block' : 'none';
    });
});

async function loadHopDongList() {
    try {
        const response = await apiGet('/hop-dong');
        if (response.success && response.data) {
            hopDongList = response.data;
            const select = document.getElementById('hopDongId');
            select.innerHTML = '<option value="">-- Chọn hợp đồng --</option>' +
                hopDongList.map(hd => 
                    `<option value="${hd.id}">${hd.maHD} - ${hd.khachHang ? hd.khachHang.hoTen : ''} (${formatCurrency(hd.tongPhiBaoHiem)})</option>`
                ).join('');
        }
    } catch (error) {
        console.error('Error loading hop dong:', error);
    }
}

async function loadThanhToan() {
    try {
        const response = await apiGet('/thanh-toan');
        
        if (response.success && response.data) {
            displayThanhToan(response.data);
        }
    } catch (error) {
        console.error('Error loading thanh toan:', error);
        document.getElementById('thanhToanTableBody').innerHTML = 
            '<tr><td colspan="8" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
}

function displayThanhToan(thanhToanList) {
    const tbody = document.getElementById('thanhToanTableBody');
    
    if (thanhToanList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    tbody.innerHTML = thanhToanList.map(tt => {
        const isHoanPhi = tt.isHoanPhi || tt.soTien < 0;
        const soTien = Math.abs(tt.soTien);
        return `
        <tr>
            <td>${tt.maTT}</td>
            <td>${tt.hopDong ? tt.hopDong.maHD : 'N/A'}</td>
            <td>${tt.hopDong && tt.hopDong.khachHang ? tt.hopDong.khachHang.hoTen : 'N/A'}</td>
            <td style="color: ${isHoanPhi ? 'red' : 'green'}">
                ${isHoanPhi ? '-' : '+'}${formatCurrency(soTien)}
            </td>
            <td>${tt.phuongThuc}</td>
            <td>${formatDate(tt.createdAt)}</td>
            <td>${isHoanPhi ? '✓' : ''}</td>
            <td>
                <button class="btn btn-sm btn-danger" onclick="deleteThanhToan(${tt.id})">Xóa</button>
            </td>
        </tr>
    `}).join('');
}

function openModal(mode, id = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('thanhToanForm');
    const title = document.getElementById('modalTitle');
    
    if (mode === 'create') {
        title.textContent = 'Thêm thanh toán';
        form.reset();
        loadHopDongList();
    }
    
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('errorMessage').classList.remove('show');
}

async function saveThanhToan() {
    const formData = {
        hopDongId: parseInt(document.getElementById('hopDongId').value),
        soTien: parseFloat(document.getElementById('soTien').value),
        phuongThuc: document.getElementById('phuongThuc').value,
        soTaiKhoan: document.getElementById('soTaiKhoan').value,
        soThe: document.getElementById('soThe').value,
        ghiChu: document.getElementById('ghiChu').value
    };
    
    try {
        const response = await apiPost('/thanh-toan', formData);
        if (response.success) {
            closeModal();
            loadThanhToan();
            alert('Thanh toán thành công! Trạng thái hợp đồng đã được cập nhật.');
        }
    } catch (error) {
        showError('errorMessage', error.message || 'Lỗi khi thanh toán');
    }
}

async function deleteThanhToan(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa giao dịch thanh toán này?')) {
        return;
    }
    
    try {
        const response = await apiDelete(`/thanh-toan/${id}`);
        if (response.success) {
            loadThanhToan();
            alert('Xóa thành công!');
        }
    } catch (error) {
        alert('Lỗi khi xóa: ' + (error.message || 'Có lỗi xảy ra'));
    }
}

