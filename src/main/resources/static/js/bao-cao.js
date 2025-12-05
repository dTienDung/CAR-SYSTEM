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
        } else if (type === 'tai-tuc') {
            thongKeDiv.innerHTML = `<p><strong>Số hợp đồng tái tục:</strong> ${data.soHopDongTaiTuc || 0}</p>`;
        } else if (type === 'tham-dinh' && data.countByStatus) {
            renderThamDinhReport(data, thongKeDiv, chiTietDiv, chartCanvas);
        }
    } catch(err) {
        thongKeDiv.innerHTML = `<p style="color:red">Lỗi: ${err.message}</p>`;
        chiTietDiv.innerHTML = '';
    }
}

function renderDoanhThuReport(data, thongKeDiv, chiTietDiv, chartCanvas) {
    // KPI Cards
    const kpiHTML = `
        <div style="background:#fff;padding:32px;border-radius:16px;margin-bottom:24px;box-shadow:0 8px 24px rgba(0,0,0,0.15);">
            <h2 style="color:#667eea;margin-bottom:24px;font-size:24px;border-bottom:3px solid #667eea;padding-bottom:12px;">
                💰 Báo cáo Doanh thu
            </h2>
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:20px;margin-bottom:24px;">
                <div style="background:linear-gradient(135deg, #667eea 0%, #764ba2 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Tổng doanh thu</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.tongDoanhThu || 0).toLocaleString()} VNĐ</div>
                </div>
                <div style="background:linear-gradient(135deg, #f093fb 0%, #f5576c 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Hôm nay</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.doanhThuHomNay || 0).toLocaleString()} VNĐ</div>
                </div>
                <div style="background:linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Tuần này</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.doanhThuTuanNay || 0).toLocaleString()} VNĐ</div>
                </div>
                <div style="background:linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Tháng này</div>
                    <div style="font-size:24px;font-weight:bold;">${(data.doanhThuThangNay || 0).toLocaleString()} VNĐ</div>
                </div>
                <div style="background:linear-gradient(135deg, #fa709a 0%, #fee140 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Số giao dịch</div>
                    <div style="font-size:28px;font-weight:bold;">${data.soGiaoDich || 0}</div>
                </div>
                <div style="background:linear-gradient(135deg, #30cfd0 0%, #330867 100%);padding:20px;border-radius:12px;color:white;text-align:center;">
                    <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">Trung bình/GD</div>
                    <div style="font-size:20px;font-weight:bold;">${(data.doanhThuTrungBinh || 0).toLocaleString()} VNĐ</div>
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

function getPhuongThucBadge(phuongThuc) {
    const badges = {
        'TIEN_MAT': '<span style="background:#4caf50;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">💵 Tiền mặt</span>',
        'CHUYEN_KHOAN': '<span style="background:#2196f3;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🏦 Chuyển khoản</span>',
        'THE': '<span style="background:#ff9800;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">💳 Thẻ</span>',
        'VI_DIEN_TU': '<span style="background:#9c27b0;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">📱 Ví điện tử</span>'
    };
    return badges[phuongThuc] || `<span style="background:#999;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">${phuongThuc || 'N/A'}</span>`;
}
