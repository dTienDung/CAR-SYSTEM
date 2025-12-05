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

// Bỏ tất cả function liên quan đến nhập điểm thủ công

async function loadHoSo() {
    try {
        const riskLevel = document.getElementById('riskLevelFilter').value;
        
        let endpoint = '/ho-so-tham-dinh';
        if (riskLevel) endpoint += `?riskLevel=${riskLevel}`;
        
        const response = await apiGet(endpoint);
        
        if (response.success && response.data) {
            displayHoSo(response.data);
        }
    } catch (error) {
        console.error('Error loading ho so:', error);
        document.getElementById('hoSoTableBody').innerHTML = 
            '<tr><td colspan="8" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
}

function filterHoSo() {
    loadHoSo();
}

function displayHoSo(hoSoList) {
    const tbody = document.getElementById('hoSoTableBody');
    
    if (hoSoList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>';
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
        // Không cần tieuChiSection nữa - đã bỏ
        loadData();
    }
    
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('errorMessage').classList.remove('show');
}

async function saveHoSo() {
    // Không cần thu thập điểm - hệ thống tự động tính
    const formData = {
        khachHangId: parseInt(document.getElementById('khachHangId').value),
        xeId: parseInt(document.getElementById('xeId').value),
        goiBaoHiemId: parseInt(document.getElementById('goiBaoHiemId').value),
        ghiChu: document.getElementById('ghiChu').value
        // Không gửi chiTietThamDinh - backend sẽ tự động tính
    };
    
    try {
        const response = await apiPost('/ho-so-tham-dinh', formData);
        if (response.success) {
            closeModal();
            loadHoSo();
            alert('✅ Tạo hồ sơ thẩm định thành công!\n🤖 Hệ thống đã tự động tính điểm thẩm định.');
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
        const riskLevel = document.getElementById('riskLevelFilter').value;
        
        let endpoint = '/ho-so-tham-dinh/export';
        if (riskLevel) endpoint += `?riskLevel=${riskLevel}`;
        
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
