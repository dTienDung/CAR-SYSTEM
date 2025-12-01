document.addEventListener('DOMContentLoaded', async function() {
    // Yêu cầu đăng nhập trước khi xem hợp đồng
    if (typeof requireAuth === 'function') {
        requireAuth();
    }

    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');

    if (!id) {
        showErrorMessage('Không tìm thấy mã hợp đồng trên đường dẫn');
        return;
    }

    try {
        const response = await apiGet(`/hop-dong/${id}`);
        if (response && response.success && response.data) {
            renderHopDong(response.data);
        } else {
            showErrorMessage(response?.message || 'Không tải được dữ liệu hợp đồng');
        }
    } catch (error) {
        console.error('Error loading hop dong detail:', error);
        showErrorMessage(error.message || 'Lỗi khi tải dữ liệu hợp đồng');
    }
});

function renderHopDong(hd) {
    // Thông tin cơ bản
    setText('maHopDong', hd.maHD || '');
    setText('tenKhachHang', hd.khachHang ? hd.khachHang.hoTen : '');
    setText('bienSoXe', hd.xe ? hd.xe.bienSo : '');

    // Ngày tháng
    setText('ngayKy', formatDate(hd.ngayKy));
    setText('ngayHieuLuc', formatDate(hd.ngayHieuLuc));
    setText('ngayHetHan', formatDate(hd.ngayHetHan));

    // Số tiền
    setText('tongPhi', formatCurrency(hd.tongPhiBaoHiem));
    setText('daThanhToan', formatCurrency(hd.tongDaThanhToan));

    // Trạng thái
    const statusElement = document.getElementById('trangThaiLabel');
    if (statusElement) {
        const mapping = mapTrangThaiHopDong(hd.trangThai);

        statusElement.classList.remove('CHO_THANH_TOAN', 'DANG_HIEU_LUC', 'HET_HAN', 'DA_HUY', 'NHAP');
        statusElement.classList.add('status', mapping.cssClass);
        statusElement.textContent = mapping.label;
    }
}

function setText(id, value) {
    const el = document.getElementById(id);
    if (el) {
        el.textContent = value || '';
    }
}

function mapTrangThaiHopDong(status) {
    const map = {
        DRAFT:            { cssClass: 'NHAP',             label: 'NHÁP' },
        PENDING_PAYMENT:  { cssClass: 'CHO_THANH_TOAN',   label: 'CHỜ THANH TOÁN' },
        ACTIVE:           { cssClass: 'DANG_HIEU_LUC',    label: 'ĐANG HIỆU LỰC' },
        EXPIRED:          { cssClass: 'HET_HAN',          label: 'HẾT HẠN' },
        CANCELLED:        { cssClass: 'DA_HUY',           label: 'ĐÃ HỦY' },
        TERMINATED:       { cssClass: 'DA_HUY',           label: 'CHẤM DỨT' },
        RENEWED:          { cssClass: 'DANG_HIEU_LUC',    label: 'ĐÃ TÁI TỤC' }
    };

    return map[status] || { cssClass: 'NHAP', label: status || 'KHÔNG XÁC ĐỊNH' };
}

function showErrorMessage(message) {
    const el = document.getElementById('errorMessage');
    if (el) {
        el.textContent = message;
    } else {
        alert(message);
    }
}


