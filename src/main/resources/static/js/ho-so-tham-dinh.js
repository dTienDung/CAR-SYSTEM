let khachHangList = [];
let xeList = [];
let goiBaoHiemList = [];
let tieuChiList = [];
let maTranTinhPhiList = [];

document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    loadData();
    loadMaTranTinhPhi();
    
    // Check for URL params (drill-down from dashboard)
    const urlParams = new URLSearchParams(window.location.search);
    const riskLevelParam = urlParams.get('riskLevel');
    const xeIdParam = urlParams.get('xeId');
    
    // Apply filters from URL params
    if (riskLevelParam) {
        document.getElementById('riskLevelFilter').value = riskLevelParam;
    }
    
    // Load ho so (will use filters if set)
    loadHoSo();
    
    // If xeId specified, scroll to and highlight that vehicle's records
    if (xeIdParam) {
        setTimeout(() => highlightXeRecords(xeIdParam), 500);
    }
    
    document.getElementById('hoSoForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveHoSo();
    });
});

function highlightXeRecords(xeId) {
    // Find and highlight rows with matching xeId
    const rows = document.querySelectorAll('#hoSoTableBody tr');
    rows.forEach(row => {
        const xeCell = row.cells[2]; // Biển số column
        if (xeCell) {
            row.style.backgroundColor = '#fff3cd';
            row.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    });
}

async function loadData() {
    try {
        const [khRes, goiRes] = await Promise.all([
            apiGet('/khach-hang'),
            apiGet('/goi-bao-hiem')
        ]);
        
        if (khRes.success && khRes.data) {
            khachHangList = khRes.data;
            const select = document.getElementById('khachHangId');
            select.innerHTML = '<option value="">-- Chọn khách hàng --</option>' +
                khachHangList.map(kh => 
                    `<option value="${kh.id}">${kh.maKH} - ${kh.hoTen}</option>`
                ).join('');
        }
        
        if (goiRes.success && goiRes.data) {
            goiBaoHiemList = goiRes.data;
            const select = document.getElementById('goiBaoHiemId');
            select.innerHTML = '<option value="">-- Chọn gói bảo hiểm --</option>' +
                goiBaoHiemList.map(g => 
                    `<option value="${g.id}">${g.maGoi} - ${g.tenGoi}</option>`
                ).join('');
        }
    } catch (error) {
        console.error('Error loading data:', error);
    }
}

async function loadXeByKhachHang() {
    const khachHangId = document.getElementById('khachHangId').value;
    if (!khachHangId) {
        document.getElementById('xeId').innerHTML = '<option value="">-- Chọn xe --</option>';
        return;
    }
    
    try {
        const response = await apiGet(`/xe?khachHangId=${khachHangId}`);
        if (response.success && response.data) {
            xeList = response.data;
            const select = document.getElementById('xeId');
            if (xeList.length === 0) {
                select.innerHTML = '<option value="">-- Không có xe nào --</option>';
            } else {
                select.innerHTML = '<option value="">-- Chọn xe --</option>' +
                    xeList.map(xe => 
                        `<option value="${xe.id}">${xe.bienSo} - ${xe.hangXe} ${xe.dongXe}</option>`
                    ).join('');
            }
        } else {
            document.getElementById('xeId').innerHTML = '<option value="">-- Chọn xe --</option>';
        }
    } catch (error) {
        console.error('Error loading xe:', error);
        document.getElementById('xeId').innerHTML = '<option value="">-- Lỗi khi tải danh sách xe --</option>';
    }
}

async function loadTieuChi() {
    const goiBaoHiemId = document.getElementById('goiBaoHiemId').value;
    if (!goiBaoHiemId) {
        document.getElementById('tieuChiSection').style.display = 'none';
        return;
    }
    
    try {
        const response = await apiGet('/tieu-chi-tham-dinh/active');
        if (response.success && response.data) {
            tieuChiList = response.data;
            displayTieuChiForm();
            document.getElementById('tieuChiSection').style.display = 'block';
        }
    } catch (error) {
        console.error('Error loading tieu chi:', error);
    }
}

async function loadMaTranTinhPhi() {
    try {
        const response = await apiGet('/ma-tran-tinh-phi');
        if (response.success && response.data) {
            maTranTinhPhiList = response.data.filter(mt => mt.active);
        }
    } catch (error) {
        console.error('Error loading ma tran tinh phi:', error);
    }
}

function displayTieuChiForm() {
    const container = document.getElementById('tieuChiList');
    container.innerHTML = tieuChiList.map((tc, index) => `
        <div class="form-group" style="border: 1px solid #ddd; padding: 15px; margin-bottom: 10px; border-radius: 5px;">
            <label><strong>${tc.tenTieuChi}</strong> (Điểm tối đa: ${tc.diemToiDa})</label>
            ${tc.moTa ? `<small style="display: block; color: #666; margin-bottom: 5px;">${tc.moTa}</small>` : ''}
            ${tc.dieuKien ? `<small style="display: block; color: #666; margin-bottom: 5px;">Điều kiện: ${tc.dieuKien}</small>` : ''}
            <input type="number" 
                   id="tieuChi_${tc.id}" 
                   class="tieuChiInput" 
                   min="0" 
                   max="${tc.diemToiDa}" 
                   value="0" 
                   onchange="calculateTotalScore()"
                   data-tieu-chi-id="${tc.id}"
                   style="width: 100px;">
            <input type="text" 
                   id="ghiChu_${tc.id}" 
                   placeholder="Ghi chú (tùy chọn)" 
                   style="width: calc(100% - 120px); margin-left: 10px;">
        </div>
    `).join('');
    
    calculateTotalScore();
}

// Load lại khi chọn gói bảo hiểm
document.getElementById('goiBaoHiemId').addEventListener('change', function() {
    loadTieuChi();
});

function calculateTotalScore() {
    let totalScore = 0;
    const inputs = document.querySelectorAll('.tieuChiInput');
    
    inputs.forEach(input => {
        const diem = parseInt(input.value) || 0;
        totalScore += diem;
    });
    
    document.getElementById('totalScore').textContent = totalScore;
    
    // Xác định RiskLevel theo business rules
    let riskLevel = '';
    let riskLevelText = '';
    if (totalScore < 15) {
        riskLevel = 'CHAP_NHAN';
        riskLevelText = 'CHẤP NHẬN';
    } else if (totalScore < 25) {
        riskLevel = 'XEM_XET';
        riskLevelText = 'XEM XÉT';
    } else {
        riskLevel = 'TU_CHOI';
        riskLevelText = 'TỪ CHỐI';
    }
    
    document.getElementById('riskLevel').textContent = riskLevelText;
    document.getElementById('riskLevel').style.color = 
        riskLevel === 'CHAP_NHAN' ? 'green' : 
        riskLevel === 'XEM_XET' ? 'orange' : 'red';
    
    // Tính phí bảo hiểm
    calculatePhiBaoHiem(totalScore);
}

function calculatePhiBaoHiem(riskScore) {
    const goiBaoHiemId = document.getElementById('goiBaoHiemId').value;
    if (!goiBaoHiemId) {
        document.getElementById('phiBaoHiem').textContent = '-';
        return;
    }
    
    const goiBaoHiem = goiBaoHiemList.find(g => g.id == goiBaoHiemId);
    if (!goiBaoHiem || !goiBaoHiem.phiCoBan) {
        document.getElementById('phiBaoHiem').textContent = '-';
        return;
    }
    
    // Tìm ma trận tính phí phù hợp
    const maTran = maTranTinhPhiList.find(mt => 
        riskScore >= mt.diemRuiRoTu && riskScore <= mt.diemRuiRoDen
    );
    
    if (maTran && maTran.heSoPhi) {
        const phiBaoHiem = goiBaoHiem.phiCoBan * parseFloat(maTran.heSoPhi);
        document.getElementById('phiBaoHiem').textContent = formatCurrency(phiBaoHiem);
    } else {
        document.getElementById('phiBaoHiem').textContent = formatCurrency(goiBaoHiem.phiCoBan) + ' (chưa có hệ số)';
    }
}

async function loadHoSo() {
    try {
        const trangThai = document.getElementById('trangThaiFilter').value;
        const riskLevel = document.getElementById('riskLevelFilter').value;
        
        let endpoint = '/ho-so-tham-dinh';
        const params = [];
        if (trangThai) params.push(`trangThai=${trangThai}`);
        if (riskLevel) params.push(`riskLevel=${riskLevel}`);
        if (params.length > 0) endpoint += '?' + params.join('&');
        
        const response = await apiGet(endpoint);
        
        if (response.success && response.data) {
            displayHoSo(response.data);
        }
    } catch (error) {
        console.error('Error loading ho so:', error);
        document.getElementById('hoSoTableBody').innerHTML = 
            '<tr><td colspan="9" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
}

function filterHoSo() {
    loadHoSo();
}

function displayHoSo(hoSoList) {
    const tbody = document.getElementById('hoSoTableBody');
    
    if (hoSoList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="9" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    tbody.innerHTML = hoSoList.map(hs => `
        <tr>
            <td>${hs.maHS}</td>
            <td>${hs.khachHang ? hs.khachHang.hoTen : 'N/A'}</td>
            <td>${hs.xe ? hs.xe.bienSo : 'N/A'}</td>
            <td>${hs.goiBaoHiem ? hs.goiBaoHiem.tenGoi : 'N/A'}</td>
            <td>${hs.riskScore}</td>
            <td><span class="badge badge-${getRiskLevelColor(hs.riskLevel)}">${hs.riskLevel}</span></td>
            <td>${hs.trangThai}</td>
            <td>${formatCurrency(hs.phiBaoHiem)}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="viewHoSo(${hs.id})">Xem</button>
                <button class="btn btn-sm btn-success" onclick="calculateRiskScore(${hs.id})">Tính lại điểm</button>
            </td>
        </tr>
    `).join('');
}

function getRiskLevelColor(level) {
    const colors = {
        'CHAP_NHAN': 'success',
        'XEM_XET': 'warning',
        'TU_CHOI': 'danger'
    };
    return colors[level] || 'secondary';
}

function openModal(mode, id = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('hoSoForm');
    const title = document.getElementById('modalTitle');
    
    if (mode === 'create') {
        title.textContent = 'Tạo hồ sơ thẩm định';
        form.reset();
        document.getElementById('hoSoId').value = '';
        document.getElementById('tieuChiSection').style.display = 'none';
        loadData();
    }
    
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('errorMessage').classList.remove('show');
}

async function saveHoSo() {
    // Thu thập điểm từng tiêu chí
    const chiTietThamDinh = [];
    const inputs = document.querySelectorAll('.tieuChiInput');
    
    inputs.forEach(input => {
        const tieuChiId = parseInt(input.dataset.tieuChiId);
        const diem = parseInt(input.value) || 0;
        const ghiChu = document.getElementById(`ghiChu_${tieuChiId}`).value;
        
        if (diem > 0 || ghiChu) {
            chiTietThamDinh.push({
                tieuChiId: tieuChiId,
                diem: diem,
                ghiChu: ghiChu
            });
        }
    });
    
    const formData = {
        khachHangId: parseInt(document.getElementById('khachHangId').value),
        xeId: parseInt(document.getElementById('xeId').value),
        goiBaoHiemId: parseInt(document.getElementById('goiBaoHiemId').value),
        ghiChu: document.getElementById('ghiChu').value,
        chiTietThamDinh: chiTietThamDinh
    };
    
    try {
        const response = await apiPost('/ho-so-tham-dinh', formData);
        if (response.success) {
            closeModal();
            loadHoSo();
            alert('Tạo hồ sơ thẩm định thành công!');
        }
    } catch (error) {
        showError('errorMessage', error.message || 'Lỗi khi tạo hồ sơ');
    }
}

function viewHoSo(id) {
    window.location.href = `/ho-so-detail.html?id=${id}`;
}

async function calculateRiskScore(id) {
    try {
        const response = await apiPost(`/ho-so-tham-dinh/${id}/risk-score`, {});
        if (response.success) {
            loadHoSo();
            alert(`RiskScore: ${response.data.riskScore}, RiskLevel: ${response.data.riskLevel}`);
        }
    } catch (error) {
        alert('Lỗi: ' + (error.message || 'Có lỗi xảy ra'));
    }
}

async function exportToExcel() {
    try {
        const trangThai = document.getElementById('trangThaiFilter').value;
        const riskLevel = document.getElementById('riskLevelFilter').value;
        
        let endpoint = '/ho-so-tham-dinh/export';
        const params = [];
        if (trangThai) params.push(`trangThai=${trangThai}`);
        if (riskLevel) params.push(`riskLevel=${riskLevel}`);
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
        a.download = `HoSoThamDinh_${new Date().toISOString().slice(0,10)}.xlsx`;
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
