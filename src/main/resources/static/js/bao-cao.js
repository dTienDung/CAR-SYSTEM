let baoCaoChartInstance = null;

async function loadBaoCao(type) {
    const thongKeDiv = document.getElementById('baoCaoThongKe');
    const chiTietDiv = document.getElementById('baoCaoChiTiet');
    const chartCanvas = document.getElementById('baoCaoChart');

    // Hiển thị loading
    thongKeDiv.innerHTML = `
        <div style="text-align:center;padding:40px;color:#667eea;font-size:18px;">
            <div style="display:inline-block;animation:spin 1s linear infinite;">⏳</div>
            <div style="margin-top:8px;">Đang tải dữ liệu...</div>
        </div>
    `;
    chiTietDiv.innerHTML = '';
    chartCanvas.style.display = 'none';

    // Destroy chart cũ nếu có
    if (baoCaoChartInstance) {
        baoCaoChartInstance.destroy();
        baoCaoChartInstance = null;
    }

    try {
        const response = await apiGet(`/bao-cao/${type}`);
        const data = response.data;

        thongKeDiv.innerHTML = '';
        chiTietDiv.innerHTML = '';

        if (type === 'doanh-thu') {
            renderDoanhThuReport(data, thongKeDiv, chiTietDiv, chartCanvas);
        } else if (type === 'hop-dong') {
            renderHopDongReport(data, thongKeDiv, chiTietDiv, chartCanvas);
        } else if (type === 'tham-dinh' && data.countByStatus) {
            renderThamDinhReport(data, thongKeDiv, chiTietDiv, chartCanvas);
        } else if (type === 'khach-hang') {
            renderKhachHangReport(data, thongKeDiv, chiTietDiv, chartCanvas);
        }
    } catch(err) {
        thongKeDiv.innerHTML = `<p style="color:red">Lỗi: ${err.message}</p>`;
        chiTietDiv.innerHTML = '';
    }
}

function renderDoanhThuReport(data, thongKeDiv, chiTietDiv, chartCanvas) {
    // KPI Cards với phân tích nâng cao
    const tangTruongColor = (data.tangTruong || 0) >= 0 ? '#4caf50' : '#f44336';
    const tangTruongIcon = (data.tangTruong || 0) >= 0 ? '📈' : '📉';
    
    const kpiHTML = `
        <div style="background:#fff;padding:32px;border-radius:16px;margin-bottom:24px;box-shadow:0 8px 24px rgba(0,0,0,0.15);">
            <h2 style="color:#667eea;margin-bottom:24px;font-size:24px;border-bottom:3px solid #667eea;padding-bottom:12px;">
                💰 Báo cáo Doanh thu - Phân tích Chi tiết
            </h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:20px;margin-bottom:24px;">
                <div style="background:linear-gradient(135deg, #667eea 0%, #764ba2 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Tổng doanh thu</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.tongDoanhThu || 0).toLocaleString()} VNĐ</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">Kỳ trước: ${(data.doanhThuKyTruoc || 0).toLocaleString()} VNĐ</div>
                </div>
                <div style="background:linear-gradient(135deg, #f093fb 0%, #f5576c 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">${tangTruongIcon} Tăng trưởng</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.tangTruong || 0).toFixed(2)}%</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">So với kỳ trước</div>
                </div>
                <div style="background:linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Hôm nay</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.doanhThuHomNay || 0).toLocaleString()} VNĐ</div>
                </div>
                <div style="background:linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Tuần này</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.doanhThuTuanNay || 0).toLocaleString()} VNĐ</div>
                </div>
                <div style="background:linear-gradient(135deg, #fa709a 0%, #fee140 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Tháng này</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.doanhThuThangNay || 0).toLocaleString()} VNĐ</div>
                </div>
                <div style="background:linear-gradient(135deg, #30cfd0 0%, #330867 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Số giao dịch</div>
                    <div style="font-size:28px;font-weight:bold;">${data.soGiaoDich || 0}</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">Kỳ trước: ${data.soGiaoDichKyTruoc || 0}</div>
                </div>
                <div style="background:linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Trung bình/GD</div>
                    <div style="font-size:20px;font-weight:bold;">${(data.doanhThuTrungBinh || 0).toLocaleString()} VNĐ</div>
                </div>
                <div style="background:linear-gradient(135deg, #fc5c7d 0%, #6a82fb 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">⚠️ Tỷ lệ hoàn phí</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.tyLeHoanPhi || 0).toFixed(2)}%</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">${data.soGiaoDichHoanPhi || 0} giao dịch</div>
                </div>
            </div>
        </div>
    `;
    thongKeDiv.innerHTML = kpiHTML;

    // Biểu đồ Timeline
    if (data.timeline && Object.keys(data.timeline).length > 0) {
        chartCanvas.style.display = 'block';
        const ctx = chartCanvas.getContext('2d');
        const labels = Object.keys(data.timeline);
        const values = Object.values(data.timeline);

        baoCaoChartInstance = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Doanh thu (VNĐ)',
                    data: values,
                    borderColor: 'rgba(102, 126, 234, 1)',
                    backgroundColor: 'rgba(102, 126, 234, 0.1)',
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4,
                    pointRadius: 5,
                    pointHoverRadius: 7,
                    pointBackgroundColor: 'rgba(102, 126, 234, 1)',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { display: true, position: 'top' },
                    title: { 
                        display: true, 
                        text: 'Biểu đồ doanh thu theo thời gian', 
                        font: { size: 18, weight: 'bold' },
                        padding: { top: 10, bottom: 20 }
                    },
                    tooltip: {
                        backgroundColor: 'rgba(0,0,0,0.8)',
                        padding: 12,
                        callbacks: {
                            label: function(ctx) {
                                return 'Doanh thu: ' + ctx.parsed.y.toLocaleString() + ' VNĐ';
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return value.toLocaleString() + ' VNĐ';
                            }
                        }
                    }
                }
            }
        });
    }

    // Bảng chi tiết giao dịch
    if (data.chiTiet && data.chiTiet.length > 0) {
        const tableRows = data.chiTiet.map(item => `
            <tr>
                <td>${item.maTT || ''}</td>
                <td>${item.khachHang || '<em style="color:#999;">N/A</em>'}</td>
                <td>${item.hopDong || '<em style="color:#999;">N/A</em>'}</td>
                <td style="font-weight:bold;color:${item.isHoanPhi ? '#f44336' : '#4caf50'};">
                    ${item.isHoanPhi ? '-' : ''}${(item.soTien || 0).toLocaleString()} VNĐ
                </td>
                <td>${getPhuongThucBadge(item.phuongThuc)}</td>
                <td>${item.ngayThanhToan || ''}</td>
                <td>${item.ghiChu || '<em style="color:#999;">Không có</em>'}</td>
            </tr>
        `).join('');

        chiTietDiv.innerHTML = `
            <div style="background:#fff;padding:32px;border-radius:16px;box-shadow:0 8px 24px rgba(0,0,0,0.15);margin-top:24px;">
                <h2 style="color:#667eea;margin-bottom:24px;font-size:24px;border-bottom:3px solid #667eea;padding-bottom:12px;">
                    📋 Chi tiết giao dịch (${data.chiTiet.length} giao dịch gần nhất)
                </h2>
                <div style="overflow-x:auto;">
                    <table class="styled-table">
                        <thead>
                            <tr>
                                <th>Mã TT</th>
                                <th>Khách hàng</th>
                                <th>Hợp đồng</th>
                                <th>Số tiền</th>
                                <th>Phương thức</th>
                                <th>Ngày TT</th>
                                <th>Ghi chú</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${tableRows}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
    }

    // Biểu đồ phân bổ theo phương thức thanh toán
    if (data.theoPhuongThucThanhToan && Object.keys(data.theoPhuongThucThanhToan).length > 0) {
        const phuongThucDiv = document.createElement('div');
        phuongThucDiv.style.cssText = 'background:#fff;padding:32px;border-radius:16px;box-shadow:0 8px 24px rgba(0,0,0,0.15);margin-top:24px;';
        phuongThucDiv.innerHTML = `
            <h3 style="color:#667eea;margin-bottom:20px;">📊 Phân bổ theo phương thức thanh toán</h3>
            <canvas id="phuongThucChart" style="max-width:500px;margin:0 auto;"></canvas>
        `;
        chiTietDiv.appendChild(phuongThucDiv);

        setTimeout(() => {
            const ctx2 = document.getElementById('phuongThucChart').getContext('2d');
            new Chart(ctx2, {
                type: 'doughnut',
                data: {
                    labels: Object.keys(data.theoPhuongThucThanhToan),
                    datasets: [{
                        data: Object.values(data.theoPhuongThucThanhToan),
                        backgroundColor: [
                            'rgba(102, 126, 234, 0.9)',
                            'rgba(76, 175, 80, 0.9)',
                            'rgba(255, 193, 7, 0.9)',
                            'rgba(244, 67, 54, 0.9)'
                        ],
                        borderWidth: 3,
                        borderColor: '#fff'
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: { position: 'bottom' },
                        tooltip: {
                            callbacks: {
                                label: function(ctx) {
                                    return ctx.label + ': ' + ctx.parsed.toLocaleString() + ' VNĐ';
                                }
                            }
                        }
                    }
                }
            });
        }, 100);
    }
}

function renderThamDinhReport(data, thongKeDiv, chiTietDiv, chartCanvas) {
    // Helper functions
    function getStatusBadge(status) {
        const badges = {
            'CHO_DUYET': '<span style="background:#ffc107;color:#000;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">⏳ Chờ duyệt</span>',
            'DA_DUYET': '<span style="background:#4caf50;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">✅ Đã duyệt</span>',
            'TU_CHOI': '<span style="background:#f44336;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">❌ Từ chối</span>',
            'DANG_XU_LY': '<span style="background:#2196f3;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🔄 Đang xử lý</span>'
        };
        return badges[status] || `<span style="background:#999;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">${status}</span>`;
    }

    function getRiskLevelBadge(level) {
        const badges = {
            'THAP': '<span style="background:#4caf50;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🟢 Thấp</span>',
            'TRUNG_BINH': '<span style="background:#ff9800;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🟡 Trung bình</span>',
            'CAO': '<span style="background:#f44336;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🔴 Cao</span>'
        };
        return badges[level] || `<span style="background:#999;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">${level}</span>`;
    }

    // Tạo summary card
    const statusColors = {
        'CHO_DUYET': 'linear-gradient(135deg, #ffc107 0%, #ff9800 100%)',
        'DA_DUYET': 'linear-gradient(135deg, #4caf50 0%, #45a049 100%)',
        'TU_CHOI': 'linear-gradient(135deg, #f44336 0%, #e53935 100%)',
        'DANG_XU_LY': 'linear-gradient(135deg, #2196f3 0%, #1976d2 100%)'
    };

    const statusCards = Object.entries(data.countByStatus)
        .map(([status, count]) => `
            <div style="background:${statusColors[status] || 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'};padding:20px;border-radius:12px;color:white;text-align:center;">
                <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">${status.replace('_', ' ')}</div>
                <div style="font-size:28px;font-weight:bold;">${count}</div>
            </div>
        `).join('');

    thongKeDiv.innerHTML = `
        <div style="background:#fff;padding:32px;border-radius:16px;margin-bottom:24px;box-shadow:0 8px 24px rgba(0,0,0,0.15);">
            <h2 style="color:#667eea;margin-bottom:24px;font-size:24px;border-bottom:3px solid #667eea;padding-bottom:12px;">✅ Báo cáo Thẩm định</h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:20px;margin-bottom:0;">
                ${statusCards}
                <div style="background:linear-gradient(135deg, #667eea 0%, #764ba2 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Điểm TB Risk Score</div>
                    <div style="font-size:28px;font-weight:bold;">${data.avgRiskScore?.toFixed(2) || '0.00'}</div>
                </div>
                <div style="background:linear-gradient(135deg, #11998e 0%, #38ef7d 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Tổng phí bảo hiểm</div>
                    <div style="font-size:20px;font-weight:bold;">${(data.totalPhi || 0).toLocaleString()} VNĐ</div>
                </div>
            </div>
        </div>
    `;

    // Tạo HTML cho bảng chi tiết
    const tableRows = data.details.map(hs => {
        const riskLevel = hs.riskLevel || '';
        const trangThai = hs.trangThai || '';

        return `
            <tr>
                <td>${hs.maHS}</td>
                <td>${hs.khachHang || '<em style="color:#999;">Chưa có</em>'}</td>
                <td><strong style="color:#667eea;">${hs.bienSo || '<em style="color:#999;">N/A</em>'}</strong></td>
                <td>${hs.goiBaoHiem || '<em style="color:#999;">Chưa chọn</em>'}</td>
                <td>${hs.riskScore || 0}</td>
                <td>${getRiskLevelBadge(riskLevel)}</td>
                <td>${getStatusBadge(trangThai)}</td>
                <td style="font-weight:bold;color:#2e7d32;">${(hs.phiBaoHiem || 0).toLocaleString()} VNĐ</td>
            </tr>
        `;
    }).join('');

    chiTietDiv.innerHTML = `
        <div style="background:#fff;padding:32px;border-radius:16px;box-shadow:0 8px 24px rgba(0,0,0,0.15);">
            <h2 style="color:#667eea;margin-bottom:24px;font-size:24px;border-bottom:3px solid #667eea;padding-bottom:12px;">📋 Chi tiết hồ sơ thẩm định</h2>
            <div style="overflow-x:auto;">
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th>Mã HS</th>
                            <th>Khách hàng</th>
                            <th>Biển số</th>
                            <th>Gói BH</th>
                            <th>Risk Score</th>
                            <th>Risk Level</th>
                            <th>Trạng thái</th>
                            <th>Phí BH</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${tableRows}
                    </tbody>
                </table>
            </div>
        </div>
    `;

    // Vẽ Doughnut chart
    chartCanvas.style.display = 'block';
    const ctx = chartCanvas.getContext('2d');
    const chartLabels = Object.keys(data.countByStatus);
    const chartData = Object.values(data.countByStatus);
    const chartColors = chartLabels.map(label => {
        const colorMap = {
            'CHO_DUYET': 'rgba(255, 193, 7, 0.9)',
            'DA_DUYET': 'rgba(76, 175, 80, 0.9)',
            'TU_CHOI': 'rgba(244, 67, 54, 0.9)',
            'DANG_XU_LY': 'rgba(33, 150, 243, 0.9)'
        };
        return colorMap[label] || 'rgba(102, 126, 234, 0.9)';
    });

    baoCaoChartInstance = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: chartLabels.map(l => l.replace('_', ' ')),
            datasets: [{
                data: chartData,
                backgroundColor: chartColors,
                borderColor: '#fff',
                borderWidth: 3,
                hoverOffset: 15
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'bottom', labels: { padding: 20, font: { size: 14, weight: '600' }, usePointStyle: true, pointStyle: 'circle' } },
                title: { display: true, text: 'Phân bổ trạng thái hồ sơ thẩm định', font: { size: 20, weight: 'bold' }, padding: { top: 10, bottom: 30 }, color: '#333' },
                tooltip: {
                    backgroundColor: 'rgba(0,0,0,0.8)',
                    padding: 12,
                    callbacks: {
                        label: function(ctx) {
                            const val = ctx.parsed || 0;
                            const total = ctx.dataset.data.reduce((a,b)=>a+b,0);
                            return `${ctx.label}: ${val} hồ sơ (${((val/total)*100).toFixed(1)}%)`;
                        }
                    }
                }
            }
        }
    });
}

function renderKhachHangReport(data, thongKeDiv, chiTietDiv, chartCanvas) {
    // KPI Card
    const kpiHTML = `
        <div style="background:#fff;padding:32px;border-radius:16px;margin-bottom:24px;box-shadow:0 8px 24px rgba(0,0,0,0.15);">
            <h2 style="color:#667eea;margin-bottom:24px;font-size:24px;border-bottom:3px solid #667eea;padding-bottom:12px;">
                👥 Báo cáo Khách hàng
            </h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:20px;">
                <div style="background:linear-gradient(135deg, #667eea 0%, #764ba2 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Tổng khách hàng</div>
                    <div style="font-size:32px;font-weight:bold;">${data.tongKhachHang || 0}</div>
                </div>
            </div>
        </div>
    `;
    thongKeDiv.innerHTML = kpiHTML;

    // Biểu đồ phân loại
    if (data.theoGioiTinh && Object.keys(data.theoGioiTinh).length > 0) {
        chartCanvas.style.display = 'block';
        const ctx = chartCanvas.getContext('2d');
        
        baoCaoChartInstance = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Giới tính', 'Độ tuổi'],
                datasets: [{
                    label: 'Phân loại khách hàng',
                    data: [Object.keys(data.theoGioiTinh).length, Object.keys(data.theoDoTuoi).length],
                    backgroundColor: ['rgba(102, 126, 234, 0.8)', 'rgba(76, 175, 80, 0.8)']
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: { display: true, text: 'Phân loại khách hàng', font: { size: 18, weight: 'bold' } }
                }
            }
        });
    }

    // Bảng phân loại
    let phanLoaiHTML = '<div style="background:#fff;padding:32px;border-radius:16px;box-shadow:0 8px 24px rgba(0,0,0,0.15);margin-top:24px;">';
    phanLoaiHTML += '<h3 style="color:#667eea;margin-bottom:20px;">📊 Phân loại khách hàng</h3>';
    phanLoaiHTML += '<div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(300px,1fr));gap:24px;">';
    
    // Theo giới tính
    if (data.theoGioiTinh) {
        phanLoaiHTML += '<div><h4 style="color:#333;margin-bottom:12px;">👫 Theo giới tính</h4><ul style="list-style:none;padding:0;">';
        Object.entries(data.theoGioiTinh).forEach(([key, val]) => {
            phanLoaiHTML += `<li style="padding:8px;background:#f5f5f5;margin-bottom:8px;border-radius:8px;"><strong>${key}:</strong> ${val} người</li>`;
        });
        phanLoaiHTML += '</ul></div>';
    }
    
    // Theo độ tuổi
    if (data.theoDoTuoi) {
        phanLoaiHTML += '<div><h4 style="color:#333;margin-bottom:12px;">🎂 Theo độ tuổi</h4><ul style="list-style:none;padding:0;">';
        Object.entries(data.theoDoTuoi).forEach(([key, val]) => {
            phanLoaiHTML += `<li style="padding:8px;background:#f5f5f5;margin-bottom:8px;border-radius:8px;"><strong>${key}:</strong> ${val} người</li>`;
        });
        phanLoaiHTML += '</ul></div>';
    }
    
    // Theo nghề nghiệp
    if (data.theoNgheNghiep) {
        phanLoaiHTML += '<div><h4 style="color:#333;margin-bottom:12px;">💼 Theo nghề nghiệp</h4><ul style="list-style:none;padding:0;">';
        Object.entries(data.theoNgheNghiep).slice(0, 5).forEach(([key, val]) => {
            phanLoaiHTML += `<li style="padding:8px;background:#f5f5f5;margin-bottom:8px;border-radius:8px;"><strong>${key}:</strong> ${val} người</li>`;
        });
        phanLoaiHTML += '</ul></div>';
    }
    
    phanLoaiHTML += '</div></div>';
    chiTietDiv.innerHTML = phanLoaiHTML;

    // Top khách hàng nhiều xe
    if (data.topKhachHangNhieuXe && data.topKhachHangNhieuXe.length > 0) {
        const topXeRows = data.topKhachHangNhieuXe.map(kh => `
            <tr>
                <td>${kh.maKH}</td>
                <td><strong>${kh.hoTen}</strong></td>
                <td style="text-align:center;font-size:20px;font-weight:bold;color:#667eea;">${kh.soXe}</td>
                <td>${kh.soDienThoai}</td>
                <td>${kh.email || '<em style="color:#999;">N/A</em>'}</td>
            </tr>
        `).join('');
        
        chiTietDiv.innerHTML += `
            <div style="background:#fff;padding:32px;border-radius:16px;box-shadow:0 8px 24px rgba(0,0,0,0.15);margin-top:24px;">
                <h3 style="color:#667eea;margin-bottom:20px;">🚗 Top khách hàng có nhiều xe</h3>
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th>Mã KH</th>
                            <th>Họ tên</th>
                            <th>Số xe</th>
                            <th>SĐT</th>
                            <th>Email</th>
                        </tr>
                    </thead>
                    <tbody>${topXeRows}</tbody>
                </table>
            </div>
        `;
    }

    // Top khách hàng giá trị cao
    if (data.topKhachHangGiaTriCao && data.topKhachHangGiaTriCao.length > 0) {
        const topGiaTriRows = data.topKhachHangGiaTriCao.map(kh => `
            <tr>
                <td>${kh.maKH}</td>
                <td><strong>${kh.hoTen}</strong></td>
                <td style="text-align:center;">${kh.soHopDong || 0}</td>
                <td style="font-weight:bold;color:#4caf50;text-align:right;">${(kh.tongGiaTri || 0).toLocaleString()} VNĐ</td>
                <td>${kh.soDienThoai}</td>
            </tr>
        `).join('');
        
        chiTietDiv.innerHTML += `
            <div style="background:#fff;padding:32px;border-radius:16px;box-shadow:0 8px 24px rgba(0,0,0,0.15);margin-top:24px;">
                <h3 style="color:#667eea;margin-bottom:20px;">💎 Top khách hàng giá trị cao</h3>
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th>Mã KH</th>
                            <th>Họ tên</th>
                            <th>Số HĐ</th>
                            <th>Tổng giá trị</th>
                            <th>SĐT</th>
                        </tr>
                    </thead>
                    <tbody>${topGiaTriRows}</tbody>
                </table>
            </div>
        `;
    }
}

function renderHopDongReport(data, thongKeDiv, chiTietDiv, chartCanvas) {
    // KPI Cards với phân tích nâng cao
    const kpiHTML = `
        <div style="background:#fff;padding:32px;border-radius:16px;margin-bottom:24px;box-shadow:0 8px 24px rgba(0,0,0,0.15);">
            <h2 style="color:#667eea;margin-bottom:24px;font-size:24px;border-bottom:3px solid #667eea;padding-bottom:12px;">
                📄 Báo cáo Hợp đồng - Phân tích Chi tiết
            </h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:20px;margin-bottom:24px;">
                <div style="background:linear-gradient(135deg, #667eea 0%, #764ba2 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Tổng hợp đồng</div>
                    <div style="font-size:32px;font-weight:bold;">${data.tongHopDong || 0}</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">Hiệu lực: ${data.hopDongHieuLuc || 0}</div>
                </div>
                <div style="background:linear-gradient(135deg, #f093fb 0%, #f5576c 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Tổng phí BH</div>
                    <div style="font-size:20px;font-weight:bold;">${(data.tongPhiBaoHiem || 0).toLocaleString()} VNĐ</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">TB: ${(data.giaTriTrungBinh || 0).toLocaleString()} VNĐ</div>
                </div>
                <div style="background:linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Đã thanh toán</div>
                    <div style="font-size:20px;font-weight:bold;">${(data.tongDaThanhToan || 0).toLocaleString()} VNĐ</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">Tỷ lệ: ${(data.tyLeThanhToan || 0).toFixed(1)}%</div>
                </div>
                <div style="background:linear-gradient(135deg, #fa709a 0%, #fee140 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Còn nợ</div>
                    <div style="font-size:20px;font-weight:bold;">${(data.tongConNo || 0).toLocaleString()} VNĐ</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">${(100 - (data.tyLeThanhToan || 0)).toFixed(1)}% chưa thu</div>
                </div>
                <div style="background:linear-gradient(135deg, #ff9800 0%, #ff5722 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">⚠️ Sắp hết hạn</div>
                    <div style="font-size:28px;font-weight:bold;">${data.hopDongSapHetHan || 0}</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">Trong 30 ngày tới</div>
                </div>
                <div style="background:linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">🔄 Tỷ lệ tái tục</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.tyLeTaiTuc || 0).toFixed(1)}%</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">Khách trung thành</div>
                </div>
                <div style="background:linear-gradient(135deg, #fc5c7d 0%, #6a82fb 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">❌ Tỷ lệ hủy</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.tyLeHuy || 0).toFixed(2)}%</div>
                    <div style="font-size:12px;margin-top:8px;opacity:0.8;">Cần cải thiện</div>
                </div>
            </div>
        </div>
    `;
    thongKeDiv.innerHTML = kpiHTML;

    // Biểu đồ trạng thái
    if (data.theoTrangThai && Object.keys(data.theoTrangThai).length > 0) {
        chartCanvas.style.display = 'block';
        const ctx = chartCanvas.getContext('2d');
        
        const statusColors = {
            'DRAFT': 'rgba(158, 158, 158, 0.9)',
            'PENDING_PAYMENT': 'rgba(255, 193, 7, 0.9)',
            'ACTIVE': 'rgba(76, 175, 80, 0.9)',
            'EXPIRED': 'rgba(33, 150, 243, 0.9)',
            'RENEWED': 'rgba(156, 39, 176, 0.9)',
            'CANCELLED': 'rgba(244, 67, 54, 0.9)'
        };
        
        const labels = Object.keys(data.theoTrangThai);
        const chartData = Object.values(data.theoTrangThai);
        const colors = labels.map(l => statusColors[l] || 'rgba(102, 126, 234, 0.9)');
        
        baoCaoChartInstance = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels.map(l => l.replace('_', ' ')),
                datasets: [{
                    data: chartData,
                    backgroundColor: colors,
                    borderColor: '#fff',
                    borderWidth: 3
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { position: 'bottom', labels: { padding: 15, font: { size: 13 } } },
                    title: { display: true, text: 'Phân bổ theo trạng thái', font: { size: 18, weight: 'bold' } },
                    tooltip: {
                        callbacks: {
                            label: function(ctx) {
                                const total = ctx.dataset.data.reduce((a,b)=>a+b,0);
                                return `${ctx.label}: ${ctx.parsed} HĐ (${((ctx.parsed/total)*100).toFixed(1)}%)`;
                            }
                        }
                    }
                }
            }
        });
    }

    // Phân loại
    let phanLoaiHTML = '<div style="background:#fff;padding:32px;border-radius:16px;box-shadow:0 8px 24px rgba(0,0,0,0.15);margin-top:24px;">';
    phanLoaiHTML += '<h3 style="color:#667eea;margin-bottom:20px;">📊 Phân loại hợp đồng</h3>';
    phanLoaiHTML += '<div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(300px,1fr));gap:24px;">';
    
    // Theo loại quan hệ
    if (data.theoLoaiQuanHe) {
        phanLoaiHTML += '<div><h4 style="color:#333;margin-bottom:12px;">🔄 Theo loại quan hệ</h4><ul style="list-style:none;padding:0;">';
        Object.entries(data.theoLoaiQuanHe).forEach(([key, val]) => {
            const label = key === 'MOI' ? 'Mới' : (key === 'TAI_TUC' ? 'Tái tục' : key);
            phanLoaiHTML += `<li style="padding:8px;background:#f5f5f5;margin-bottom:8px;border-radius:8px;"><strong>${label}:</strong> ${val} HĐ</li>`;
        });
        phanLoaiHTML += '</ul></div>';
    }
    
    // Top gói bảo hiểm
    if (data.topGoiBaoHiem) {
        phanLoaiHTML += '<div><h4 style="color:#333;margin-bottom:12px;">🏆 Top gói bảo hiểm</h4><ul style="list-style:none;padding:0;">';
        Object.entries(data.topGoiBaoHiem).slice(0, 5).forEach(([key, val]) => {
            phanLoaiHTML += `<li style="padding:8px;background:#f5f5f5;margin-bottom:8px;border-radius:8px;"><strong>${key}:</strong> ${val} HĐ</li>`;
        });
        phanLoaiHTML += '</ul></div>';
    }
    
    phanLoaiHTML += '</div></div>';
    chiTietDiv.innerHTML = phanLoaiHTML;

    // Bảng chi tiết
    if (data.chiTiet && data.chiTiet.length > 0) {
        const tableRows = data.chiTiet.map(hd => `
            <tr>
                <td>${hd.maHD}</td>
                <td>${hd.khachHang || '<em style="color:#999;">N/A</em>'}</td>
                <td><strong style="color:#667eea;">${hd.xe || '<em style="color:#999;">N/A</em>'}</strong></td>
                <td>${hd.goiBaoHiem || '<em style="color:#999;">N/A</em>'}</td>
                <td>${hd.ngayKy || ''}</td>
                <td>${hd.ngayHieuLuc || ''}</td>
                <td>${hd.ngayHetHan || ''}</td>
                <td style="font-weight:bold;color:#4caf50;text-align:right;">${(hd.tongPhi || 0).toLocaleString()} VNĐ</td>
                <td style="text-align:right;">${(hd.daThanhToan || 0).toLocaleString()} VNĐ</td>
                <td>${getTrangThaiHDBadge(hd.trangThai)}</td>
            </tr>
        `).join('');
        
        chiTietDiv.innerHTML += `
            <div style="background:#fff;padding:32px;border-radius:16px;box-shadow:0 8px 24px rgba(0,0,0,0.15);margin-top:24px;">
                <h3 style="color:#667eea;margin-bottom:20px;">📋 Chi tiết hợp đồng (${data.chiTiet.length} HĐ gần nhất)</h3>
                <div style="overflow-x:auto;">
                    <table class="styled-table">
                        <thead>
                            <tr>
                                <th>Mã HĐ</th>
                                <th>Khách hàng</th>
                                <th>Xe</th>
                                <th>Gói BH</th>
                                <th>Ngày ký</th>
                                <th>Hiệu lực</th>
                                <th>Hết hạn</th>
                                <th>Tổng phí</th>
                                <th>Đã TT</th>
                                <th>Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody>${tableRows}</tbody>
                    </table>
                </div>
            </div>
        `;
    }
}

function getTrangThaiHDBadge(status) {
    const badges = {
        'DRAFT': '<span style="background:#9e9e9e;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">📝 Nháp</span>',
        'PENDING_PAYMENT': '<span style="background:#ffc107;color:#000;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">⏳ Chờ TT</span>',
        'ACTIVE': '<span style="background:#4caf50;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">✅ Hiệu lực</span>',
        'EXPIRED': '<span style="background:#2196f3;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">⏰ Hết hạn</span>',
        'RENEWED': '<span style="background:#9c27b0;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🔄 Tái tục</span>',
        'CANCELLED': '<span style="background:#f44336;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">❌ Đã hủy</span>'
    };
    return badges[status] || `<span style="background:#999;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">${status}</span>`;
}

function getPhuongThucBadge(phuongThuc) {
    const badges = {
        'TIEN_MAT': '<span style="background:#4caf50;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">💵 Tiền mặt</span>',
        'CHUYEN_KHOAN': '<span style="background:#2196f3;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🏦 Chuyển khoản</span>',
        'THE': '<span style="background:#ff9800;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">💳 Thẻ</span>',
        'VI_DIEN_TU': '<span style="background:#9c27b0;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">📱 Ví điện tử</span>'
    };
    return badges[phuongThuc] || `<span style="background:#999;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">${phuongThuc || 'N/A'}</span>`;
}
