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
            thongKeDiv.innerHTML = `<p><strong>Tổng doanh thu:</strong> ${(data.tongDoanhThu || 0).toLocaleString()} VNĐ</p>`;
            return;
        } else if (type === 'tai-tuc') {
            thongKeDiv.innerHTML = `<p><strong>Số hợp đồng tái tục:</strong> ${data.soHopDongTaiTuc || 0}</p>`;
            return;
        } else if (type === 'tham-dinh' && data.countByStatus) {
            // Helper functions
            function getStatusBadge(status) {
                const badges = {
                    'Chờ duyệt': '<span style="background:#ffc107;color:#000;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">⏳ Chờ duyệt</span>',
                    'Đã duyệt': '<span style="background:#4caf50;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">✅ Đã duyệt</span>',
                    'Từ chối': '<span style="background:#f44336;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">❌ Từ chối</span>',
                    'Đang xử lý': '<span style="background:#2196f3;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🔄 Đang xử lý</span>'
                };
                return badges[status] || `<span style="background:#999;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">${status}</span>`;
            }

            function getRiskLevelBadge(level) {
                const badges = {
                    'Thấp': '<span style="background:#4caf50;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🟢 Thấp</span>',
                    'Trung bình': '<span style="background:#ff9800;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🟡 Trung bình</span>',
                    'Cao': '<span style="background:#f44336;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">🔴 Cao</span>'
                };
                return badges[level] || `<span style="background:#999;color:#fff;padding:4px 12px;border-radius:12px;font-size:12px;font-weight:600;">${level}</span>`;
            }

            // Tạo summary card
            const statusColors = {
                'Chờ duyệt': 'linear-gradient(135deg, #ffc107 0%, #ff9800 100%)',
                'Đã duyệt': 'linear-gradient(135deg, #4caf50 0%, #45a049 100%)',
                'Từ chối': 'linear-gradient(135deg, #f44336 0%, #e53935 100%)',
                'Đang xử lý': 'linear-gradient(135deg, #2196f3 0%, #1976d2 100%)'
            };

            const statusCards = Object.entries(data.countByStatus)
                .map(([status, count]) => `
                    <div style="background:${statusColors[status] || 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'};padding:20px;border-radius:12px;color:white;text-align:center;">
                        <div style="font-size:14px;opacity:0.9;margin-bottom:8px;">${status}</div>
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
                    'Chờ duyệt': 'rgba(255, 193, 7, 0.9)',
                    'Đã duyệt': 'rgba(76, 175, 80, 0.9)',
                    'Từ chối': 'rgba(244, 67, 54, 0.9)',
                    'Đang xử lý': 'rgba(33, 150, 243, 0.9)'
                };
                return colorMap[label] || 'rgba(102, 126, 234, 0.9)';
            });

            baoCaoChartInstance = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: chartLabels,
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
    } catch(err) {
        thongKeDiv.innerHTML = `<p style="color:red">Lỗi: ${err.message}</p>`;
        chiTietDiv.innerHTML = '';
    }
}