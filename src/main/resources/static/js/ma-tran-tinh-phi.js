document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    loadMaTran();
    
    document.getElementById('maTranForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        await saveMaTran();
    });
});

async function loadMaTran() {
    try {
        const response = await apiGet('/ma-tran-tinh-phi');
        
        if (response.success && response.data) {
            displayMaTran(response.data.sort((a, b) => a.diemRuiRoTu - b.diemRuiRoTu));
        }
    } catch (error) {
        console.error('Error loading ma tran:', error);
        document.getElementById('maTranTableBody').innerHTML = 
            '<tr><td colspan="6" class="text-center">Lỗi khi tải dữ liệu</td></tr>';
    }
}

function displayMaTran(maTranList) {
    const tbody = document.getElementById('maTranTableBody');
    
    if (maTranList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">Không có dữ liệu</td></tr>';
        return;
    }
    
    tbody.innerHTML = maTranList.map(mt => {
        // Tính % thay đổi
        let thayDoi = '';
        let thayDoiColor = '';
        if (mt.heSoPhi < 1.0) {
            const percent = Math.round((1.0 - mt.heSoPhi) * 100);
            thayDoi = `Giảm ${percent}%`;
            thayDoiColor = 'color: #4caf50; font-weight: bold;';
        } else if (mt.heSoPhi === 1.0) {
            thayDoi = 'Phí chuẩn';
            thayDoiColor = 'color: #2196f3; font-weight: bold;';
        } else {
            const percent = Math.round((mt.heSoPhi - 1.0) * 100);
            thayDoi = `Tăng ${percent}%`;
            thayDoiColor = 'color: #f44336; font-weight: bold;';
        }
        
        // Badge cho khoảng %
        let badge = '';
        if (mt.diemRuiRoDen <= 10) {
            badge = '<span style="background: #ffd700; color: #000; padding: 2px 8px; border-radius: 12px; font-size: 11px;">⭐ ƯU TIÊN</span>';
        } else if (mt.diemRuiRoDen <= 25) {
            badge = '<span style="background: #4caf50; color: #fff; padding: 2px 8px; border-radius: 12px; font-size: 11px;">✅ CHẤP NHẬN</span>';
        } else if (mt.diemRuiRoDen <= 50) {
            badge = '<span style="background: #ff9800; color: #fff; padding: 2px 8px; border-radius: 12px; font-size: 11px;">⚠️ XEM XÉT</span>';
        } else {
            badge = '<span style="background: #f44336; color: #fff; padding: 2px 8px; border-radius: 12px; font-size: 11px;">❌ TỪ CHỐI</span>';
        }
        
        return `
            <tr>
                <td><strong>${mt.diemRuiRoTu}%</strong></td>
                <td><strong>${mt.diemRuiRoDen}%</strong> ${badge}</td>
                <td><strong style="color: #1976d2;">${mt.heSoPhi}x</strong></td>
                <td><span style="${thayDoiColor}">${thayDoi}</span></td>
                <td style="max-width: 300px;">${mt.moTa || ''}</td>
                <td>${mt.active ? '<span style="color: #4caf50;">✓ Hoạt động</span>' : '<span style="color: #999;">✗ Tạm khóa</span>'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editMaTran(${mt.id})">✏️ Sửa</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteMaTran(${mt.id})">🗑️ Xóa</button>
                </td>
            </tr>
        `;
    }).join('');
}

function openModal(mode, id = null) {
    const modal = document.getElementById('modal');
    const form = document.getElementById('maTranForm');
    const title = document.getElementById('modalTitle');
    
    if (mode === 'create') {
        title.textContent = 'Thêm ma trận tính phí';
        form.reset();
        document.getElementById('maTranId').value = '';
        document.getElementById('active').checked = true;
    } else {
        title.textContent = 'Sửa ma trận tính phí';
        loadMaTranDetail(id);
    }
    
    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('errorMessage').classList.remove('show');
}

async function loadMaTranDetail(id) {
    try {
        const response = await apiGet(`/ma-tran-tinh-phi/${id}`);
        if (response.success && response.data) {
            const mt = response.data;
            document.getElementById('maTranId').value = mt.id;
            document.getElementById('diemRuiRoTu').value = mt.diemRuiRoTu || '';
            document.getElementById('diemRuiRoDen').value = mt.diemRuiRoDen || '';
            document.getElementById('heSoPhi').value = mt.heSoPhi || '';
            document.getElementById('moTa').value = mt.moTa || '';
            document.getElementById('active').checked = mt.active !== false;
        }
    } catch (error) {
        showError('errorMessage', 'Lỗi khi tải thông tin ma trận');
    }
}

async function saveMaTran() {
    const id = document.getElementById('maTranId').value;
    const formData = {
        diemRuiRoTu: parseInt(document.getElementById('diemRuiRoTu').value),
        diemRuiRoDen: parseInt(document.getElementById('diemRuiRoDen').value),
        heSoPhi: parseFloat(document.getElementById('heSoPhi').value),
        moTa: document.getElementById('moTa').value,
        active: document.getElementById('active').checked
    };
    
    try {
        let response;
        if (id) {
            response = await apiPut(`/ma-tran-tinh-phi/${id}`, formData);
        } else {
            response = await apiPost('/ma-tran-tinh-phi', formData);
        }
        
        if (response.success) {
            closeModal();
            loadMaTran();
            alert('Lưu thành công!');
        }
    } catch (error) {
        showError('errorMessage', error.message || 'Lỗi khi lưu ma trận');
    }
}

function editMaTran(id) {
    openModal('edit', id);
}

async function deleteMaTran(id) {
    if (!confirm('Bạn có chắc chắn muốn xóa ma trận này?')) {
        return;
    }
    
    try {
        const response = await apiDelete(`/ma-tran-tinh-phi/${id}`);
        if (response.success) {
            loadMaTran();
            alert('Xóa thành công!');
        }
    } catch (error) {
        alert('Lỗi khi xóa: ' + (error.message || 'Có lỗi xảy ra'));
    }
}
