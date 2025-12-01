let hoSoList = [];

document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    loadHoSoThamDinh();
    loadHopDong();
    
    document.getElementById('hopDongForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveHopDong();
    });
});

async function loadHoSoThamDinh() {
    try {
        const response = await apiGet('/ho-so-tham-dinh?riskLevel=CHAP_NHAN');
        if (response.success && response.data) {
            hoSoList = response.data;
            const select = document.getElementById('hoSoThamDinhId');
            if (hoSoList.length === 0) {
                select.innerHTML = '<option value="">-- Không có hồ sơ nào được chấp nhận --</option>';
            } else {
                select.innerHTML = '<option value="">-- Chọn hồ sơ thẩm định --</option>' +
                    hoSoList.map(hs => 
                        `<option value="${hs.id}">${hs.maHS} - ${hs.khachHang ? hs.khachHang.hoTen : 'N/A'} - ${hs.xe ? hs.xe.bienSo : 'N/A'} (${hs.riskLevel})</option>`
                    ).join('');
            }
        } else {
            const select = document.getElementById('hoSoThamDinhId');
            select.innerHTML = '<option value="">-- Lỗi khi tải dữ liệu --</option>';
        }
    } catch (error) {
        console.error('Error loading ho so:', error);
        const select = document.getElementById('hoSoThamDinhId');
        select.innerHTML = '<option value="">-- Lỗi khi tải dữ liệu --</option>';
    }
}

async function loadHopDong() {
    try {
        const trangThai = document.getElementById('trangThaiFilter').value;
        const fromDate = document.getElementById('fromDate').value;
        const toDate = document.getElementById('toDate').value;
        
        let endpoint = '/hop-dong';
        const params = [];
        if (trangThai) params.push(`trangThai=${trangThai}`);
        if (fromDate) params.push(`fromDate=${fromDate}`);
        if (toDate) params.push(`toDate=${toDate}`);
        if (params.length > 0) endpoint += '?' + params.join('&');
        
        const response = await apiGet(endpoint);
        
        if (response.success && response.data) {
            displayHopDong(response.data);
        }
    } catch (error) {
        console.error('Error loading hop dong:', error);
        document.getElementById('hopDongTableBody').innerHTML = 
            '<tr><td colspan="10" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
}

function filterHopDong() {
    loadHopDong();
}

function displayHopDong(hopDongList) {
    const tbody = document.getElementById('hopDongTableBody');
    
    if (hopDongList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="10" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    tbody.innerHTML = hopDongList.map(hd => `
        <tr>
            <td>${hd.maHD}</td>
            <td>${hd.khachHang ? hd.khachHang.hoTen : 'N/A'}</td>
            <td>${hd.xe ? hd.xe.bienSo : 'N/A'}</td>
            <td>${formatDate(hd.ngayKy)}</td>
            <td>${formatDate(hd.ngayHieuLuc)}</td>
            <td>${formatDate(hd.ngayHetHan)}</td>
            <td>${formatCurrency(hd.tongPhiBaoHiem)}</td>
            <td>${formatCurrency(hd.tongDaThanhToan)}</td>
            <td><span class="badge badge-${getStatusColor(hd.trangThai)}">${hd.trangThai}</span></td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="viewHopDong(${hd.id})">Xem</button>
                ${hd.trangThai === 'ACTIVE' || hd.trangThai === 'EXPIRED' ? 
                    `<button class="btn btn-sm btn-success" onclick="renewHopDong(${hd.id})">Tái tục</button>` : ''}
                ${hd.trangThai !== 'CANCELLED' ? 
                    `<button class="btn btn-sm btn-danger" onclick="cancelHopDong(${hd.id})">Hủy</button>` : ''}
            </td>
        </tr>
    `).join('');
}

function getStatusColor(status) {
    const colors = {
        'DRAFT': 'secondary',
        'PENDING_PAYMENT': 'warning',
        'ACTIVE': 'success',
        'EXPIRED': 'info',
        'CANCELLED': 'danger',
        'RENEWED': 'primary'
    };
    return colors[status] || 'secondary';
}

function openModal(mode, id = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('hopDongForm');
    const title = document.getElementById('modalTitle');
    
    if (mode === 'create') {
        title.textContent = 'Tạo hợp đồng';
        form.reset();
        document.getElementById('hopDongId').value = '';
        loadHoSoThamDinh();
    }
    
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('errorMessage').classList.remove('show');
}

async function saveHopDong() {
    const formData = {
        hoSoThamDinhId: parseInt(document.getElementById('hoSoThamDinhId').value),
        ngayKy: document.getElementById('ngayKy').value,
        ngayHieuLuc: document.getElementById('ngayHieuLuc').value,
        ngayHetHan: document.getElementById('ngayHetHan').value,
        ghiChu: document.getElementById('ghiChu').value
    };
    
    try {
        const response = await apiPost('/hop-dong', formData);
        if (response.success) {
            closeModal();
            loadHopDong();
            alert('Tạo hợp đồng thành công!');
        }
    } catch (error) {
        showError('errorMessage', error.message || 'Lỗi khi tạo hợp đồng');
    }
}

function viewHopDong(id) {
    window.location.href = `/hop-dong-detail.html?id=${id}`;
}

async function renewHopDong(id) {
    try {
        // Get current contract details first
        const currentContract = await apiGet(`/hop-dong/${id}`);
        if (!currentContract.success || !currentContract.data) {
            alert('Không thể tải thông tin hợp đồng');
            return;
        }
        
        const hopDong = currentContract.data;
        const ngayHetHanCu = hopDong.ngayHetHan;
        
        // Calculate suggested dates
        const suggestedNgayKy = new Date(ngayHetHanCu);
        const suggestedNgayHieuLuc = new Date(ngayHetHanCu);
        suggestedNgayHieuLuc.setDate(suggestedNgayHieuLuc.getDate() + 1);
        const suggestedNgayHetHan = new Date(suggestedNgayHieuLuc);
        suggestedNgayHetHan.setFullYear(suggestedNgayHetHan.getFullYear() + 1);
        
        const formatDate = (date) => date.toISOString().split('T')[0];
        
        alert(`Thông tin hợp đồng cũ:\n` +
              `- Mã HĐ: ${hopDong.maHD}\n` +
              `- Ngày hết hạn: ${ngayHetHanCu}\n\n` +
              `Lưu ý:\n` +
              `- Ngày ký phải >= ${ngayHetHanCu}\n` +
              `- Ngày hiệu lực phải > ${ngayHetHanCu}\n` +
              `- Thời hạn tối thiểu: 30 ngày`);
        
        const ngayKy = prompt(`Nhập ngày ký (yyyy-MM-dd)\nGợi ý: ${formatDate(suggestedNgayKy)}:`, formatDate(suggestedNgayKy));
        if (!ngayKy) return;
        
        const ngayHieuLuc = prompt(`Nhập ngày hiệu lực (yyyy-MM-dd)\nGợi ý: ${formatDate(suggestedNgayHieuLuc)}:`, formatDate(suggestedNgayHieuLuc));
        if (!ngayHieuLuc) return;
        
        const ngayHetHan = prompt(`Nhập ngày hết hạn (yyyy-MM-dd)\nGợi ý: ${formatDate(suggestedNgayHetHan)}:`, formatDate(suggestedNgayHetHan));
        if (!ngayHetHan) return;
        
        const response = await apiPost(`/hop-dong/${id}/renew`, {
            ngayKy, ngayHieuLuc, ngayHetHan
        });
        
        if (response.success) {
            loadHopDong();
            alert('Tái tục hợp đồng thành công!\nMã hợp đồng mới: ' + response.data.maHD);
        }
    } catch (error) {
        alert('Lỗi: ' + (error.message || 'Có lỗi xảy ra'));
    }
}

async function cancelHopDong(id) {
    const lyDo = prompt('Nhập lý do hủy:');
    if (lyDo === null) return;
    
    try {
        const response = await apiPost(`/hop-dong/${id}/cancel`, { lyDo });
        if (response.success) {
            loadHopDong();
            alert('Hủy hợp đồng thành công!');
        }
    } catch (error) {
        alert('Lỗi: ' + (error.message || 'Có lỗi xảy ra'));
    }
}

async function exportToExcel() {
    try {
        const trangThai = document.getElementById('trangThaiFilter').value;
        const fromDate = document.getElementById('fromDate').value;
        const toDate = document.getElementById('toDate').value;
        
        let endpoint = '/hop-dong/export';
        const params = [];
        if (trangThai) params.push(`trangThai=${trangThai}`);
        if (fromDate) params.push(`fromDate=${fromDate}`);
        if (toDate) params.push(`toDate=${toDate}`);
        if (params.length > 0) endpoint += '?' + params.join('&');
        
        // Create download link
        const token = localStorage.getItem('token');
        const url = API_BASE_URL + endpoint;
        
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (!response.ok) {
            throw new Error('Lỗi khi xuất file');
        }
        
        const blob = await response.blob();
        const downloadUrl = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = downloadUrl;
        a.download = `HopDong_${new Date().toISOString().slice(0,10)}.xlsx`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(downloadUrl);
        document.body.removeChild(a);
        
        alert('Xuất file Excel thành công!');
    } catch (error) {
        console.error('Error exporting:', error);
        alert('Lỗi khi xuất file: ' + (error.message || 'Có lỗi xảy ra'));
    }
}
