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
    
    let html = '<h3>📋 Chi tiết đánh giá</h3><table style="width:100%; border-collapse:collapse;">';
    html += '<tr style="background:#f0f0f0;"><th style="padding:8px; border:1px solid #ddd;">Tiêu chí</th><th style="padding:8px; border:1px solid #ddd;">Điểm</th><th style="padding:8px; border:1px solid #ddd;">Ghi chú</th></tr>';
    
    chiTietList.forEach(ct => {
        html += `<tr>
            <td style="padding:8px; border:1px solid #ddd;">${ct.tieuChi ? ct.tieuChi.tenTieuChi : 'N/A'}</td>
            <td style="padding:8px; border:1px solid #ddd; text-align:center;">${ct.diem || 0}</td>
            <td style="padding:8px; border:1px solid #ddd;">${ct.ghiChu || ''}</td>
        </tr>`;
    });
    
    html += '</table>';
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
