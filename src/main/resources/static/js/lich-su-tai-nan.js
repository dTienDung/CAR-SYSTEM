// file: /js/lich-su-tai-nan.js

let taiNanList = [];
let xeList = [];

document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    loadXe();
    loadTaiNan();

    document.getElementById('taiNanForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveTaiNan();
    });
});

async function loadXe() {
    try {
        console.log('🚗 Loading xe data...');
        // CHÚ Ý: api.js đã tự prefix '/api', nên endpoint ở đây chỉ cần '/xe'
        const res = await apiGet('/xe');
        
        if (res.success && res.data) {
            xeList = res.data;
            console.log(`✅ Loaded ${xeList.length} xe`);
            
            const select = document.getElementById('xeId');
            select.innerHTML = '<option value="">-- Chọn xe --</option>' +
                xeList.map(x => 
                    `<option value="${x.id}">
                        ${x.bienSo || 'N/A'} - ${x.khachHang ? x.khachHang.hoTen : 'Chưa có KH'}
                    </option>`
                ).join('');
        } else {
            console.error('❌ Xe API không thành công:', res);
            showError('Không thể tải danh sách xe');
        }
    } catch (error) {
        console.error('❌ Error loading xe:', error);
        showError('Lỗi tải danh sách xe: ' + error.message);
    }
}

async function loadTaiNan() {
    try {
        console.log('⚠️ Loading tai nan data...');
        // api.js sẽ gọi tới /api/lich-su-tai-nan
        const res = await apiGet('/lich-su-tai-nan');
        
        if (res.success && res.data) {
            taiNanList = res.data;
            console.log(`✅ Loaded ${taiNanList.length} tai nan records`);
            displayTaiNan(taiNanList);
        } else {
            console.error('❌ Tai nan API không thành công:', res);
            showErrorMessage('Lỗi từ server: ' + (res.message || 'Không thể tải dữ liệu'));
        }
    } catch (error) {
        console.error('❌ Error loading tai nan:', error);
        showErrorMessage('Lỗi kết nối: ' + error.message);
    }
}

function displayTaiNan(list) {
    const tbody = document.getElementById('taiNanTableBody');
    if (!list || list.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">📭 Không có dữ liệu tai nạn</td></tr>';
        return;
    }

    tbody.innerHTML = list.map(item => `
        <tr>
            <td>${item.bienSo || 'N/A'}${item.khachHang ? ' - ' + item.khachHang : ''}</td>
            <td>${formatDate(item.ngayXayRa)}</td>
            <td>${item.diaDiem || 'N/A'}</td>
            <td>${item.moTa || 'N/A'}</td>
            <td>${item.thietHai ? formatCurrency(item.thietHai) : 'N/A'}</td>
            <td>
                <button class="btn btn-sm btn-danger" onclick="deleteTaiNan(${item.id})">🗑️ Xóa</button>
            </td>
        </tr>
    `).join('');
}

function showErrorMessage(message) {
    const tbody = document.getElementById('taiNanTableBody');
    tbody.innerHTML = `
        <tr>
            <td colspan="6" class="text-center" style="color: red; padding: 20px;">
                <div style="font-size: 16px; font-weight: bold;">❌ ${message}</div>
                <div style="font-size: 12px; margin-top: 10px;">
                    Vui lòng mở console (F12) để xem chi tiết lỗi
                </div>
            </td>
        </tr>
    `;
}

function showError(elementId, message) {
    const element = document.getElementById(elementId);
    if (element) {
        element.textContent = message;
        element.classList.add('show');
        setTimeout(() => element.classList.remove('show'), 3000);
    }
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    try {
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return dateString;
        return date.toLocaleDateString('vi-VN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit'
        });
    } catch (e) {
        return dateString;
    }
}

function formatCurrency(amount) {
    if (!amount) return 'N/A';
    try {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    } catch (e) {
        return amount.toString();
    }
}

function filterTaiNan() {
    const bienSoKeyword = document.getElementById('searchBienSo').value.trim().toLowerCase();
    const fromDate = document.getElementById('fromDate').value;
    const toDate = document.getElementById('toDate').value;

    if (!taiNanList || taiNanList.length === 0) {
        displayTaiNan([]);
        return;
    }

    const filtered = taiNanList.filter(item => {
        // Kiểm tra biển số
        const bienSo = (item.bienSo || '').toLowerCase();
        const matchBienSo = !bienSoKeyword || bienSo.includes(bienSoKeyword);

        // Kiểm tra ngày
        let matchDate = true;
        if (fromDate) {
            matchDate = matchDate && item.ngayXayRa >= fromDate;
        }
        if (toDate) {
            const toDateEnd = toDate + 'T23:59:59';
            matchDate = matchDate && item.ngayXayRa <= toDateEnd;
        }

        return matchBienSo && matchDate;
    });

    displayTaiNan(filtered);
}

function openModal(mode) {
    const modal = document.getElementById('modal');
    const title = document.getElementById('modalTitle');
    const form = document.getElementById('taiNanForm');
    
    form.reset();
    document.getElementById('taiNanId').value = '';
    
    title.textContent = mode === 'create' ? '➕ Ghi nhận tai nạn' : '✏️ Cập nhật tai nạn';
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    const errorElement = document.getElementById('errorMessage');
    if (errorElement) {
        errorElement.classList.remove('show');
        errorElement.textContent = '';
    }
}

async function saveTaiNan() {
    const xeId = document.getElementById('xeId').value;
    const ngayXayRa = document.getElementById('ngayXayRa').value;
    const diaDiem = document.getElementById('diaDiem').value;
    const moTa = document.getElementById('moTa').value;
    const thietHai = document.getElementById('thietHai').value;
    const taiNanId = document.getElementById('taiNanId').value;

    // Validate
    if (!xeId) {
        showError('errorMessage', '⚠️ Vui lòng chọn xe');
        return;
    }
    if (!ngayXayRa) {
        showError('errorMessage', '⚠️ Vui lòng chọn ngày xảy ra');
        return;
    }

    const body = {
        xe: { id: parseInt(xeId) },
        ngayXayRa,
        diaDiem: diaDiem || null,
        moTa: moTa || null,
        thietHai: thietHai ? parseFloat(thietHai) : null
    };

    try {
        let res;
        if (taiNanId) {
            // Update
            res = await apiPut(`/lich-su-tai-nan/${taiNanId}`, body);
        } else {
            // Create
            res = await apiPost('/lich-su-tai-nan', body);
        }
        
        if (res.success) {
            closeModal();
            await loadTaiNan();
            alert('✅ ' + (taiNanId ? 'Cập nhật' : 'Ghi nhận') + ' tai nạn thành công!');
        }
    } catch (error) {
        console.error('❌ Error saving tai nan:', error);
        showError('errorMessage', '❌ ' + (error.message || 'Lỗi khi lưu dữ liệu'));
    }
}

async function deleteTaiNan(id) {
    if (!confirm('⚠️ Bạn có chắc muốn xóa bản ghi tai nạn này?')) return;

    try {
        const res = await apiDelete(`/lich-su-tai-nan/${id}`);
        if (res.success) {
            await loadTaiNan();
            alert('✅ Xóa thành công!');
        }
    } catch (error) {
        console.error('❌ Error deleting tai nan:', error);
        alert('❌ ' + (error.message || 'Lỗi khi xóa dữ liệu'));
    }
}