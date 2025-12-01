document.addEventListener('DOMContentLoaded', function() {
    requireAuth();
    
    // Display user info
    const userInfo = getUserInfo();
    if (userInfo) {
        document.getElementById('userInfo').textContent = `👤 ${userInfo.hoTen} (${userInfo.role})`;
    }
    
    // Load statistics
    loadStatistics();
    
    // Load all charts
    loadAllCharts();
});

async function loadStatistics() {
    try {
        // Load total khach hang
        const khachHangRes = await apiGet('/khach-hang');
        if (khachHangRes.success && khachHangRes.data) {
            document.getElementById('totalKhachHang').textContent = khachHangRes.data.length;
        }
        
        // Load total xe
        const xeRes = await apiGet('/xe');
        if (xeRes.success && xeRes.data) {
            document.getElementById('totalXe').textContent = xeRes.data.length;
        }
        
        // Load active hop dong
        const hopDongRes = await apiGet('/hop-dong?trangThai=ACTIVE');
        if (hopDongRes.success && hopDongRes.data) {
            document.getElementById('totalHopDong').textContent = hopDongRes.data.length;
        }
        
        // Load total ho so
        const hoSoRes = await apiGet('/ho-so-tham-dinh');
        if (hoSoRes.success && hoSoRes.data) {
            document.getElementById('totalHoSo').textContent = hoSoRes.data.length;
        }
    } catch (error) {
        console.error('Error loading statistics:', error);
    }
}

// Chart instances
let chart1Instance = null;
let chart2Instance = null;
let chart3Instance = null;
let chart4Instance = null;
let chart5Instance = null;

async function loadAllCharts() {
    await loadChart1Lifecycle();
    await loadChart2Result();
    await loadChart3DoanhThu();
    await loadChart4TaiTuc();
    await loadChart5Risk();
}

// CHART 1: Vòng đời Hợp đồng (Donut)
async function loadChart1Lifecycle() {
    try {
        const response = await apiGet('/bao-cao/hop-dong-lifecycle');
        if (!response.success || !response.data) return;
        
        const data = response.data;
        const labels = Object.keys(data);
        const values = Object.values(data);
        
        // Status name mapping
        const statusMap = {
            'DRAFT': 'Bản nháp',
            'PENDING_PAYMENT': 'Chờ thanh toán',
            'ACTIVE': 'Đang hiệu lực',
            'EXPIRED': 'Hết hạn',
            'RENEWED': 'Đã tái tục',
            'CANCELLED': 'Đã hủy'
        };
        
        const displayLabels = labels.map(l => statusMap[l] || l);
        
        const colors = {
            'DRAFT': 'rgba(158, 158, 158, 0.9)',
            'PENDING_PAYMENT': 'rgba(255, 193, 7, 0.9)',
            'ACTIVE': 'rgba(76, 175, 80, 0.9)',
            'EXPIRED': 'rgba(156, 39, 176, 0.9)',
            'RENEWED': 'rgba(33, 150, 243, 0.9)',
            'CANCELLED': 'rgba(244, 67, 54, 0.9)'
        };
        
        const bgColors = labels.map(l => colors[l] || 'rgba(102, 126, 234, 0.9)');
        
        if (chart1Instance) chart1Instance.destroy();
        
        const ctx = document.getElementById('chart1Lifecycle').getContext('2d');
        chart1Instance = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: displayLabels,
                datasets: [{
                    data: values,
                    backgroundColor: bgColors,
                    borderColor: '#fff',
                    borderWidth: 3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                onClick: (event, elements) => {
                    if (elements.length > 0) {
                        const index = elements[0].index;
                        const status = labels[index];
                        window.location.href = `/hop-dong.html?trangThai=${status}`;
                    }
                },
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: { padding: 15, font: { size: 12 } }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percent = ((context.parsed / total) * 100).toFixed(1);
                                return `${context.label}: ${context.parsed} (${percent}%)`;
                            }
                        }
                    }
                }
            }
        });
    } catch (error) {
        console.error('Error loading chart 1:', error);
    }
}

// CHART 2: Kết quả Thẩm định (Pie)
async function loadChart2Result() {
    try {
        const response = await apiGet('/bao-cao/tham-dinh-result');
        if (!response.success || !response.data) return;
        
        const data = response.data;
        const labels = Object.keys(data);
        const values = Object.values(data);
        
        const levelMap = {
            'CHAP_NHAN': 'Chấp nhận',
            'XEM_XET': 'Xem xét',
            'TU_CHOI': 'Từ chối'
        };
        
        const displayLabels = labels.map(l => levelMap[l] || l);
        
        const colors = {
            'CHAP_NHAN': 'rgba(76, 175, 80, 0.9)',
            'XEM_XET': 'rgba(255, 152, 0, 0.9)',
            'TU_CHOI': 'rgba(244, 67, 54, 0.9)'
        };
        
        const bgColors = labels.map(l => colors[l] || 'rgba(102, 126, 234, 0.9)');
        
        if (chart2Instance) chart2Instance.destroy();
        
        const ctx = document.getElementById('chart2Result').getContext('2d');
        chart2Instance = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: displayLabels,
                datasets: [{
                    data: values,
                    backgroundColor: bgColors,
                    borderColor: '#fff',
                    borderWidth: 3
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                onClick: (event, elements) => {
                    if (elements.length > 0) {
                        const index = elements[0].index;
                        const riskLevel = labels[index];
                        window.location.href = `/ho-so-tham-dinh.html?riskLevel=${riskLevel}`;
                    }
                },
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: { padding: 15, font: { size: 12 } }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percent = ((context.parsed / total) * 100).toFixed(1);
                                return `${context.label}: ${context.parsed} hồ sơ (${percent}%)`;
                            }
                        }
                    }
                }
            }
        });
    } catch (error) {
        console.error('Error loading chart 2:', error);
    }
}

// Handle date range change
function handleDateRangeChange() {
    const daysSelect = document.getElementById('daysSelect');
    const customDateRange = document.getElementById('customDateRange');
    
    if (daysSelect.value === 'custom') {
        customDateRange.style.display = 'flex';
        // Set default start date to 30 days ago
        const defaultDate = new Date();
        defaultDate.setDate(defaultDate.getDate() - 30);
        document.getElementById('startDate').value = defaultDate.toISOString().split('T')[0];
    } else {
        customDateRange.style.display = 'none';
        loadChart3DoanhThu();
    }
}

// CHART 3: Doanh thu Timeline (Vertical Bar)
async function loadChart3DoanhThu() {
    try {
        const daysSelect = document.getElementById('daysSelect').value;
        let endpoint = '/bao-cao/doanh-thu-timeline';
        
        if (daysSelect === 'custom') {
            const startDate = document.getElementById('startDate').value;
            if (!startDate) {
                alert('Vui lòng chọn ngày bắt đầu');
                return;
            }
            endpoint += `?startDate=${startDate}`;
        } else {
            endpoint += `?days=${daysSelect}`;
        }
        
        const response = await apiGet(endpoint);
        if (!response.success || !response.data) return;
        
        const { labels, data } = response.data;
        
        if (chart3Instance) chart3Instance.destroy();
        
        const ctx = document.getElementById('chart3DoanhThu').getContext('2d');
        chart3Instance = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Thực thu (VNĐ)',
                    data: data,
                    backgroundColor: 'rgba(102, 126, 234, 0.8)',
                    borderColor: 'rgba(102, 126, 234, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return value.toLocaleString() + ' đ';
                            }
                        }
                    }
                },
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return 'Thực thu: ' + context.parsed.y.toLocaleString() + ' VNĐ';
                            }
                        }
                    }
                }
            }
        });
    } catch (error) {
        console.error('Error loading chart 3:', error);
    }
}

// CHART 4: Tỷ lệ Tái tục (Stacked Bar)
async function loadChart4TaiTuc() {
    try {
        const response = await apiGet('/bao-cao/tai-tuc-rate?months=6');
        if (!response.success || !response.data) return;
        
        const { labels, renewed, expired } = response.data;
        
        if (chart4Instance) chart4Instance.destroy();
        
        const ctx = document.getElementById('chart4TaiTuc').getContext('2d');
        chart4Instance = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Đã tái tục',
                        data: renewed,
                        backgroundColor: 'rgba(76, 175, 80, 0.8)',
                        borderColor: 'rgba(76, 175, 80, 1)',
                        borderWidth: 1
                    },
                    {
                        label: 'Hết hạn chưa tục',
                        data: expired,
                        backgroundColor: 'rgba(255, 152, 0, 0.8)',
                        borderColor: 'rgba(255, 152, 0, 1)',
                        borderWidth: 1
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                scales: {
                    x: { stacked: true },
                    y: { 
                        stacked: true,
                        beginAtZero: true
                    }
                },
                onClick: (event, elements) => {
                    if (elements.length > 0) {
                        const datasetIndex = elements[0].datasetIndex;
                        const status = datasetIndex === 0 ? 'RENEWED' : 'EXPIRED';
                        window.location.href = `/hop-dong.html?trangThai=${status}`;
                    }
                },
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: { padding: 15, font: { size: 12 } }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return `${context.dataset.label}: ${context.parsed.y} hợp đồng`;
                            }
                        }
                    }
                }
            }
        });
    } catch (error) {
        console.error('Error loading chart 4:', error);
    }
}

// CHART5: Top Rủi ro (Horizontal Bar)
async function loadChart5Risk() {
    try {
        const response = await apiGet('/bao-cao/top-risk-vehicles?limit=10');
        if (!response.success || !response.data) return;
        
        const vehicles = response.data;
        const labels = vehicles.map(v => `${v.bienSo} (${v.model})`);
        const scores = vehicles.map(v => v.riskScore);
        const xeIds = vehicles.map(v => v.xeId);
        
        // Gradient colors red (higher risk = darker red)
        const bgColors = scores.map((score, idx) => {
            const intensity = 0.6 + (idx / scores.length) * 0.4;
            return `rgba(244, 67, 54, ${intensity})`;
        });
        
        if (chart5Instance) chart5Instance.destroy();
        
        const ctx = document.getElementById('chart5Risk').getContext('2d');
        chart5Instance = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Risk Score',
                    data: scores,
                    backgroundColor: bgColors,
                    borderColor: 'rgba(244, 67, 54, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                indexAxis: 'y',
                responsive: true,
                maintainAspectRatio: true,
                onClick: (event, elements) => {
                    if (elements.length > 0) {
                        const index = elements[0].index;
                        const xeId = xeIds[index];
                        if (xeId) {
                            window.location.href = `/ho-so-tham-dinh.html?xeId=${xeId}`;
                        }
                    }
                },
                scales: {
                    x: { 
                        beginAtZero: true,
                        max: 30
                    }
                },
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const vehicle = vehicles[context.dataIndex];
                                return [
                                    `Risk Score: ${vehicle.riskScore}`,
                                    `Chủ xe: ${vehicle.chuXe}`,
                                    `Mức độ: ${vehicle.riskLevel}`
                                ];
                            }
                        }
                    }
                }
            }
        });
    } catch (error) {
        console.error('Error loading chart 5:', error);
    }
}
