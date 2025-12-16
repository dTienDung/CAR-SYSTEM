// Role-based menu visibility
const ROLE_PERMISSIONS = {
    ADMIN: {
        // Admin có thể xem tất cả
        users: true,
        khachHang: true,
        xe: true,
        goiBaoHiem: true,
        hoSoThamDinh: true,
        hopDong: true,
        thanhToan: true,
        tieuChiThamDinh: true,
        maTranTinhPhi: true,
        baoCao: true
    },
    MANAGER: {
        users: false,
        khachHang: true,
        xe: true,
        goiBaoHiem: true,
        hoSoThamDinh: true,
        hopDong: true,
        thanhToan: true,
        tieuChiThamDinh: false,
        maTranTinhPhi: false,
        baoCao: true
    },
    UNDERWRITER: {
        users: false,
        khachHang: false,
        xe: false,
        goiBaoHiem: false,
        hoSoThamDinh: true,
        hopDong: false,
        thanhToan: false,
        tieuChiThamDinh: true,
        maTranTinhPhi: false,
        baoCao: true
    },
    SALES: {
        users: false,
        khachHang: true,
        xe: true,
        goiBaoHiem: true,
        hoSoThamDinh: true,
        hopDong: true,
        thanhToan: false,
        tieuChiThamDinh: false,
        maTranTinhPhi: false,
        baoCao: true
    },
    ACCOUNTANT: {
        users: false,
        khachHang: false,
        xe: false,
        goiBaoHiem: false,
        hoSoThamDinh: false,
        hopDong: true,
        thanhToan: true,
        tieuChiThamDinh: false,
        maTranTinhPhi: false,
        baoCao: true
    }
};

// Get current user role
function getCurrentUserRole() {
    const userInfo = getUserInfo();
    return userInfo ? userInfo.role : null;
}

// Check if user has permission for a feature
function hasPermission(feature) {
    const role = getCurrentUserRole();
    if (!role) return false;
    
    const permissions = ROLE_PERMISSIONS[role];
    return permissions && permissions[feature] === true;
}

// Hide menu items based on role
function setupRoleBasedMenu() {
    const role = getCurrentUserRole();
    if (!role) return;
    
    const permissions = ROLE_PERMISSIONS[role];
    if (!permissions) return;
    
    // Map of menu item IDs/selectors to feature names
    const menuMap = {
        'users-link': 'users',
        'khach-hang-link': 'khachHang',
        'xe-link': 'xe',
        'goi-bao-hiem-link': 'goiBaoHiem',
        'ho-so-tham-dinh-link': 'hoSoThamDinh',
        'hop-dong-link': 'hopDong',
        'thanh-toan-link': 'thanhToan',
        'tieu-chi-tham-dinh-link': 'tieuChiThamDinh',
        'ma-tran-tinh-phi-link': 'maTranTinhPhi',
        'bao-cao-link': 'baoCao'
    };
    
    // Hide menu items based on permissions
    Object.keys(menuMap).forEach(menuId => {
        const feature = menuMap[menuId];
        const element = document.getElementById(menuId) || 
                       document.querySelector(`a[href*="${feature}"]`) ||
                       document.querySelector(`.nav-link[href*="${feature}"]`);
        
        if (element && !permissions[feature]) {
            element.style.display = 'none';
        }
    });
    
    // Also check by href attribute
    document.querySelectorAll('.nav-link').forEach(link => {
        const href = link.getAttribute('href');
        if (!href) return;
        
        let feature = null;
        if (href.includes('users.html')) feature = 'users';
        else if (href.includes('khach-hang.html')) feature = 'khachHang';
        else if (href.includes('xe.html')) feature = 'xe';
        else if (href.includes('goi-bao-hiem.html')) feature = 'goiBaoHiem';
        else if (href.includes('ho-so-tham-dinh.html')) feature = 'hoSoThamDinh';
        else if (href.includes('hop-dong.html')) feature = 'hopDong';
        else if (href.includes('thanh-toan.html')) feature = 'thanhToan';
        else if (href.includes('tieu-chi-tham-dinh.html')) feature = 'tieuChiThamDinh';
        else if (href.includes('ma-tran-tinh-phi.html')) feature = 'maTranTinhPhi';
        else if (href.includes('bao-cao.html')) feature = 'baoCao';
        
        if (feature && !permissions[feature]) {
            link.style.display = 'none';
        }
    });
}

// Check permission before accessing a page
function requirePermission(feature) {
    if (!hasPermission(feature)) {
        alert('Bạn không có quyền truy cập tính năng này!');
        window.location.href = '/dashboard.html';
        return false;
    }
    return true;
}

// Initialize role-based menu on page load
document.addEventListener('DOMContentLoaded', function() {
    if (typeof requireAuth === 'function') {
        requireAuth();
    }
    setupRoleBasedMenu();
});
