document.addEventListener('DOMContentLoaded', async function() {
    requireAuth();
    
    // Get ID from URL
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    
    if (!id) {
        alert('Không tìm thấy ID hợp đồng');
        window.location.href = '/hop-dong.html';
        return;
    }
    
    await loadHopDongDetail(id);
});

async function loadHopDongDetail(id) {
    try {
        const response = await apiGet(`/hop-dong/${id}`);
        
        if (response.success && response.data) {
            displayHopDongDetail(response.data);
        } else {
            alert('Không tìm thấy hợp đồng');
            window.location.href = '/hop-dong.html';
        }
    } catch (error) {
        console.error('Error loading hop dong:', error);
        alert('Lỗi khi tải hợp đồng: ' + (error.message || 'Có lỗi xảy ra'));
        window.location.href = '/hop-dong.html';
    }
}

function displayHopDongDetail(hopDong) {
    // Update basic info
    document.getElementById('maHD').textContent = hopDong.maHD || 'N/A';
    document.getElementById('khachHang').textContent = hopDong.khachHang ? hopDong.khachHang.hoTen : 'N/A';
    document.getElementById('soDienThoai').textContent = hopDong.khachHang ? hopDong.khachHang.soDienThoai : 'N/A';
    document.getElementById('diaChi').textContent = hopDong.khachHang ? hopDong.khachHang.diaChi : 'N/A';
    
    // Vehicle info
    document.getElementById('bienSo').textContent = hopDong.xe ? hopDong.xe.bienSo : 'N/A';
    document.getElementById('hangXe').textContent = hopDong.xe ? `${hopDong.xe.hangXe} ${hopDong.xe.dongXe}` : 'N/A';
    document.getElementById('namSanXuat').textContent = hopDong.xe ? hopDong.xe.namSanXuat : 'N/A';
    
    // Insurance package
    document.getElementById('goiBaoHiem').textContent = hopDong.goiBaoHiem ? hopDong.goiBaoHiem.tenGoi : 'N/A';
    document.getElementById('moTaGoi').textContent = hopDong.goiBaoHiem ? hopDong.goiBaoHiem.moTa : 'N/A';
    
    // Contract dates
    document.getElementById('ngayKy').textContent = formatDate(hopDong.ngayKy);
    document.getElementById('ngayHieuLuc').textContent = formatDate(hopDong.ngayHieuLuc);
    document.getElementById('ngayHetHan').textContent = formatDate(hopDong.ngayHetHan);
    
    // Financial info
    document.getElementById('tongPhiBaoHiem').textContent = formatCurrency(hopDong.tongPhiBaoHiem || 0) + ' VNĐ';
    document.getElementById('tongDaThanhToan').textContent = formatCurrency(hopDong.tongDaThanhToan || 0) + ' VNĐ';
    
    const conLai = (hopDong.tongPhiBaoHiem || 0) - (hopDong.tongDaThanhToan || 0);
    document.getElementById('conLai').textContent = formatCurrency(conLai) + ' VNĐ';
    
    // Status
    const trangThaiElement = document.getElementById('trangThai');
    const trangThaiText = getTrangThaiText(hopDong.trangThai);
    const trangThaiClass = getTrangThaiClass(hopDong.trangThai);
    trangThaiElement.textContent = trangThaiText;
    trangThaiElement.className = 'status-box ' + trangThaiClass;
    
    // Notes
    if (hopDong.ghiChu) {
        document.getElementById('ghiChu').textContent = hopDong.ghiChu;
    }
    
    // Load payment history
    if (hopDong.id) {
        loadPaymentHistory(hopDong.id);
    }
}

async function loadPaymentHistory(hopDongId) {
    try {
        const response = await apiGet(`/thanh-toan?hopDongId=${hopDongId}`);
        
        if (response.success && response.data && response.data.length > 0) {
            displayPaymentHistory(response.data);
        } else {
            document.getElementById('paymentHistory').innerHTML = '<p style="font-style:italic; color:#666;">Chưa có giao dịch thanh toán</p>';
        }
    } catch (error) {
        console.error('Error loading payment history:', error);
        document.getElementById('paymentHistory').innerHTML = '<p style="color:red;">Lỗi khi tải lịch sử thanh toán</p>';
    }
}

function displayPaymentHistory(payments) {
    const container = document.getElementById('paymentHistory');
    
    let html = '<table style="width:100%; border-collapse:collapse; margin-top:10px;">';
    html += '<tr style="background:#f0f0f0;"><th style="padding:8px; border:1px solid #ddd;">Mã TT</th><th style="padding:8px; border:1px solid #ddd;">Ngày thanh toán</th><th style="padding:8px; border:1px solid #ddd;">Số tiền</th><th style="padding:8px; border:1px solid #ddd;">Phương thức</th><th style="padding:8px; border:1px solid #ddd;">Trạng thái</th></tr>';
    
    payments.forEach(payment => {
        html += `<tr>
            <td style="padding:8px; border:1px solid #ddd;">${payment.maTT || 'N/A'}</td>
            <td style="padding:8px; border:1px solid #ddd;">${formatDate(payment.ngayThanhToan)}</td>
            <td style="padding:8px; border:1px solid #ddd; text-align:right;">${formatCurrency(payment.soTien || 0)} VNĐ</td>
            <td style="padding:8px; border:1px solid #ddd;">${payment.phuongThucThanhToan || 'N/A'}</td>
            <td style="padding:8px; border:1px solid #ddd;">${payment.trangThai || 'N/A'}</td>
        </tr>`;
    });
    
    html += '</table>';
    container.innerHTML = html;
}

function getTrangThaiText(trangThai) {
    const map = {
        'DRAFT': 'BẢN NHÁP',
        'PENDING_PAYMENT': 'CHỜ THANH TOÁN',
        'ACTIVE': 'ĐANG HIỆU LỰC',
        'EXPIRED': 'HẾT HẠN',
        'RENEWED': 'ĐÃ TÁI TỤC',
        'CANCELLED': 'ĐÃ HỦY',
        'TERMINATED': 'CHẤM DỨT'
    };
    return map[trangThai] || trangThai;
}

function getTrangThaiClass(trangThai) {
    const map = {
        'DRAFT': 'gray',
        'PENDING_PAYMENT': 'yellow',
        'ACTIVE': 'green',
        'EXPIRED': 'orange',
        'RENEWED': 'blue',
        'CANCELLED': 'red',
        'TERMINATED': 'red'
    };
    return map[trangThai] || 'gray';
}
