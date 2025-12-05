document.addEventListener('DOMContentLoaded', async function() {
    requireAuth();
    
    // Get ID from URL
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    
    if (!id) {
        alert('Không tìm thấy ID hồ sơ');
        window.location.href = '/ho-so-tham-dinh.html';
        return;
    }
    
    await loadHoSoDetail(id);
});

async function loadHoSoDetail(id) {
    try {
        const response = await apiGet(`/ho-so-tham-dinh/${id}`);
        
        if (response.success && response.data) {
            displayHoSoDetail(response.data);
        } else {
            alert('Không tìm thấy hồ sơ');
            window.location.href = '/ho-so-tham-dinh.html';
        }
    } catch (error) {
        console.error('Error loading ho so:', error);
        alert('Lỗi khi tải hồ sơ: ' + (error.message || 'Có lỗi xảy ra'));
        window.location.href = '/ho-so-tham-dinh.html';
    }
}

function displayHoSoDetail(hoSo) {
    // Update basic info
    document.getElementById('maHS').textContent = hoSo.maHS || 'N/A';
    document.getElementById('khachHang').textContent = hoSo.khachHang ? hoSo.khachHang.hoTen : 'N/A';
    document.getElementById('bienSo').textContent = hoSo.xe ? hoSo.xe.bienSo : 'N/A';
    document.getElementById('goiBaoHiem').textContent = hoSo.goiBaoHiem ? hoSo.goiBaoHiem.tenGoi : 'N/A';
    document.getElementById('phiBaoHiem').textContent = formatCurrency(hoSo.phiBaoHiem || 0) + ' VNĐ';
    
    // Update risk assessment
    document.getElementById('riskScore').textContent = hoSo.riskScore || 0;
    
    const riskLevelElement = document.getElementById('riskLevel');
    const riskLevelText = getRiskLevelText(hoSo.riskLevel);
    const riskLevelClass = getRiskLevelClass(hoSo.riskLevel);
    riskLevelElement.textContent = riskLevelText;
    riskLevelElement.className = 'risk-box ' + riskLevelClass;
    
    document.getElementById('trangThai').textContent = getTrangThaiText(hoSo.trangThai);
    
    // Update auto note
    const autoNote = generateNote(hoSo.trangThai, hoSo.riskLevel);
    document.getElementById('autoNote').textContent = autoNote;
    
    // Update manual note if exists
    if (hoSo.ghiChu) {
        document.getElementById('ghiChu').textContent = hoSo.ghiChu;
    }
    
    // Display chi tiet tham dinh if exists
    if (hoSo.chiTietThamDinh && hoSo.chiTietThamDinh.length > 0) {
        displayChiTietThamDinh(hoSo.chiTietThamDinh);
    }
}

function displayChiTietThamDinh(chiTietList) {
    const container = document.getElementById('chiTietThamDinh');
    if (!container) return;
    
    // Tính tổng điểm và tổng điểm tối đa
    let tongDiem = 0;
    let tongDiemToiDa = 0;
    chiTietList.forEach(ct => {
        tongDiem += ct.diem || 0;
        tongDiemToiDa += ct.tieuChi ? ct.tieuChi.diemToiDa : 0;
    });
    
    let html = '<h3>📋 Chi tiết đánh giá từng tiêu chí</h3>';
    html += '<table style="width:100%; border-collapse:collapse; margin-bottom:20px;">';
    html += '<thead><tr style="background:#3498db; color:white;">';
    html += '<th style="padding:12px; border:1px solid #ddd; text-align:left;">Tiêu chí</th>';
    html += '<th style="padding:12px; border:1px solid #ddd; text-align:center; width:120px;">Điểm</th>';
    html += '<th style="padding:12px; border:1px solid #ddd; text-align:center; width:100px;">% Điểm</th>';
    html += '<th style="padding:12px; border:1px solid #ddd; text-align:left;">Ghi chú</th>';
    html += '</tr></thead><tbody>';
    
    chiTietList.forEach(ct => {
        const diem = ct.diem || 0;
        const diemToiDa = ct.tieuChi ? ct.tieuChi.diemToiDa : 0;
        const phanTram = diemToiDa > 0 ? Math.round((diem / diemToiDa) * 100) : 0;
        const ghiChu = ct.ghiChu || (diem > 0 ? '🤖 Tự động' : '-');
        
        // Màu sắc theo % điểm
        let bgColor = '#f8f9fa';
        if (phanTram >= 70) bgColor = '#ffebee'; // Đỏ nhạt
        else if (phanTram >= 40) bgColor = '#fff3e0'; // Cam nhạt
        else if (phanTram > 0) bgColor = '#fff9c4'; // Vàng nhạt
        
        html += `<tr style="background:${bgColor};">
            <td style="padding:10px; border:1px solid #ddd;">
                <strong>${ct.tieuChi ? ct.tieuChi.tenTieuChi : 'N/A'}</strong>
                ${ct.tieuChi && ct.tieuChi.moTa ? `<br><small style="color:#666;">${ct.tieuChi.moTa}</small>` : ''}
            </td>
            <td style="padding:10px; border:1px solid #ddd; text-align:center;">
                <strong style="font-size:16px; color:${phanTram >= 70 ? '#e74c3c' : phanTram >= 40 ? '#f39c12' : '#27ae60'};">${diem}</strong> / ${diemToiDa}
            </td>
            <td style="padding:10px; border:1px solid #ddd; text-align:center;">
                <span style="font-weight:bold; color:${phanTram >= 70 ? '#e74c3c' : phanTram >= 40 ? '#f39c12' : '#27ae60'};">${phanTram}%</span>
            </td>
            <td style="padding:10px; border:1px solid #ddd; font-style:italic; color:#666;">${ghiChu}</td>
        </tr>`;
    });
    
    // Tổng kết
    const tongPhanTram = tongDiemToiDa > 0 ? Math.round((tongDiem / tongDiemToiDa) * 100) : 0;
    html += `<tr style="background:#ecf0f1; font-weight:bold;">
        <td style="padding:12px; border:1px solid #ddd;">TỔNG CỘNG</td>
        <td style="padding:12px; border:1px solid #ddd; text-align:center; font-size:18px; color:#2c3e50;">
            ${tongDiem} / ${tongDiemToiDa}
        </td>
        <td style="padding:12px; border:1px solid #ddd; text-align:center; font-size:18px; color:${tongPhanTram >= 50 ? '#e74c3c' : tongPhanTram >= 25 ? '#f39c12' : '#27ae60'};">
            ${tongPhanTram}%
        </td>
        <td style="padding:12px; border:1px solid #ddd;"></td>
    </tr>`;
    
    html += '</tbody></table>';
    
    // Thêm chú thích
    html += '<div style="padding:15px; background:#e8f5e9; border-left:4px solid #4caf50; border-radius:4px; margin-top:15px;">';
    html += '<strong>📊 Giải thích:</strong><br>';
    html += '• <strong>Điểm:</strong> Điểm đạt được / Điểm tối đa của tiêu chí<br>';
    html += '• <strong>% Điểm:</strong> Tỷ lệ phần trăm điểm đạt được<br>';
    html += '• <strong>Ghi chú:</strong> 🤖 = Tự động tính, còn lại là ghi chú thủ công<br>';
    html += '• <strong>Màu nền:</strong> Xanh nhạt (an toàn) → Vàng (cảnh báo) → Cam (rủi ro) → Đỏ (nguy hiểm)';
    html += '</div>';
    
    container.innerHTML = html;
}

function getRiskLevelText(riskLevel) {
    const map = {
        'CHAP_NHAN': 'CHẤP NHẬN',
        'XEM_XET': 'XEM XÉT',
        'TU_CHOI': 'TỪ CHỐI'
    };
    return map[riskLevel] || riskLevel;
}

function getRiskLevelClass(riskLevel) {
    const map = {
        'CHAP_NHAN': 'green',
        'XEM_XET': 'yellow',
        'TU_CHOI': 'red'
    };
    return map[riskLevel] || 'green';
}

function getTrangThaiText(trangThai) {
    const map = {
        'MOI_TAO': 'MỚI TẠO',
        'DANG_THAM_DINH': 'ĐANG THẨM ĐỊNH',
        'CHAP_NHAN': 'CHẤP NHẬN',
        'TU_CHOI': 'TỪ CHỐI',
        'XEM_XET': 'XEM XÉT'
    };
    return map[trangThai] || trangThai;
}

function generateNote(state, risk) {
    if (state === 'MOI_TAO') return 'Hồ sơ mới tạo, chưa đủ dữ liệu đánh giá.';
    if (risk === 'CHAP_NHAN') return 'Rủi ro thấp. Hồ sơ đủ điều kiện chấp nhận theo tiêu chuẩn.';
    if (risk === 'XEM_XET') return 'Rủi ro trung bình. Cần xem xét thêm giấy tờ hoặc kiểm tra thực tế.';
    if (risk === 'TU_CHOI') return 'Rủi ro cao. Hồ sơ không đáp ứng yêu cầu thẩm định.';
    return 'Không có ghi chú phù hợp.';
}
