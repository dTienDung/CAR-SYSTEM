// Login form
document.addEventListener('DOMContentLoaded', function() {
    console.log('Auth.js loaded');
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    
    console.log('Login form:', loginForm);
    console.log('Register form:', registerForm);
    
    if (loginForm) {
        console.log('Adding submit listener to login form');
        loginForm.addEventListener('submit', async function(e) {
            console.log('Login form submitted');
            e.preventDefault();
            e.stopPropagation();
            
            const submitBtn = loginForm.querySelector('button[type="submit"]');
            const originalText = submitBtn.textContent;
            submitBtn.disabled = true;
            submitBtn.textContent = 'Đang xử lý...';
            
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value;
            
            if (!username || !password) {
                showError('errorMessage', 'Vui lòng điền đầy đủ thông tin');
                submitBtn.disabled = false;
                submitBtn.textContent = originalText;
                return;
            }
            
            try {
                console.log('Calling API /auth/login');
                const response = await apiPost('/auth/login', { username, password });
                console.log('Login response:', response);
                if (response && response.success && response.data) {
                    setToken(response.data.token);
                    setUserInfo({
                        username: response.data.username,
                        hoTen: response.data.hoTen,
                        email: response.data.email,
                        role: response.data.role
                    });
                    window.location.href = '/dashboard.html';
                } else {
                    const errorMsg = response?.message || 'Đăng nhập thất bại';
                    console.error('Login failed:', errorMsg);
                    showError('errorMessage', errorMsg);
                    submitBtn.disabled = false;
                    submitBtn.textContent = originalText;
                }
            } catch (error) {
                console.error('Login error:', error);
                const errorMsg = error.message || 'Đăng nhập thất bại. Vui lòng thử lại.';
                showError('errorMessage', errorMsg);
                submitBtn.disabled = false;
                submitBtn.textContent = originalText;
            }
        });
    }
    
    if (registerForm) {
        console.log('Adding submit listener to register form');
        registerForm.addEventListener('submit', async function(e) {
            console.log('Register form submitted');
            e.preventDefault();
            e.stopPropagation();
            
            const submitBtn = registerForm.querySelector('button[type="submit"]');
            const originalText = submitBtn.textContent;
            submitBtn.disabled = true;
            submitBtn.textContent = 'Đang xử lý...';
            
            const formData = {
                username: document.getElementById('username').value.trim(),
                password: document.getElementById('password').value,
                hoTen: document.getElementById('hoTen').value.trim(),
                email: document.getElementById('email').value.trim(),
                soDienThoai: document.getElementById('soDienThoai').value.trim(),
                role: document.getElementById('role').value
            };
            
            if (!formData.username || !formData.password || !formData.hoTen || !formData.email || !formData.role) {
                showError('errorMessage', 'Vui lòng điền đầy đủ thông tin bắt buộc');
                submitBtn.disabled = false;
                submitBtn.textContent = originalText;
                return;
            }
            
            if (formData.password.length < 6) {
                showError('errorMessage', 'Mật khẩu phải có ít nhất 6 ký tự');
                submitBtn.disabled = false;
                submitBtn.textContent = originalText;
                return;
            }
            
            try {
                console.log('Calling API /auth/register', formData);
                const response = await apiPost('/auth/register', formData);
                console.log('Register response:', response);
                if (response && response.success && response.data) {
                    setToken(response.data.token);
                    setUserInfo({
                        username: response.data.username,
                        hoTen: response.data.hoTen,
                        email: response.data.email,
                        role: response.data.role
                    });
                    window.location.href = '/dashboard.html';
                } else {
                    const errorMsg = response?.message || 'Đăng ký thất bại';
                    console.error('Register failed:', errorMsg);
                    showError('errorMessage', errorMsg);
                    submitBtn.disabled = false;
                    submitBtn.textContent = originalText;
                }
            } catch (error) {
                console.error('Register error:', error);
                const errorMsg = error.message || 'Đăng ký thất bại. Vui lòng thử lại.';
                showError('errorMessage', errorMsg);
                submitBtn.disabled = false;
                submitBtn.textContent = originalText;
            }
        });
    }
});

// Handle login with button click
async function handleLogin() {
    const loginForm = document.getElementById('loginForm');
    if (!loginForm) return;
    
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    
    if (!username || !password) {
        showError('errorMessage', 'Vui lòng điền đầy đủ thông tin');
        return;
    }
    
    const submitBtn = document.getElementById('loginBtn');
    const originalText = submitBtn.textContent;
    submitBtn.disabled = true;
    submitBtn.textContent = 'Đang xử lý...';
    
    try {
        console.log('Calling API /auth/login');
        const response = await apiPost('/auth/login', { username, password });
        console.log('Login response:', response);
        if (response && response.success && response.data) {
            setToken(response.data.token);
            setUserInfo({
                username: response.data.username,
                hoTen: response.data.hoTen,
                email: response.data.email,
                role: response.data.role
            });
            window.location.href = '/dashboard.html';
        } else {
            const errorMsg = response?.message || 'Đăng nhập thất bại';
            showError('errorMessage', errorMsg);
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }
    } catch (error) {
        console.error('Login error:', error);
        showError('errorMessage', error.message || 'Đăng nhập thất bại. Vui lòng thử lại.');
        submitBtn.disabled = false;
        submitBtn.textContent = originalText;
    }
}

// Handle register with button click
async function handleRegister() {
    const registerForm = document.getElementById('registerForm');
    if (!registerForm) return;
    
    const formData = {
        username: document.getElementById('username').value.trim(),
        password: document.getElementById('password').value,
        hoTen: document.getElementById('hoTen').value.trim(),
        email: document.getElementById('email').value.trim(),
        soDienThoai: document.getElementById('soDienThoai').value.trim(),
        role: document.getElementById('role').value
    };
    
    if (!formData.username || !formData.password || !formData.hoTen || !formData.email || !formData.role) {
        showError('errorMessage', 'Vui lòng điền đầy đủ thông tin bắt buộc');
        return;
    }
    
    if (formData.password.length < 6) {
        showError('errorMessage', 'Mật khẩu phải có ít nhất 6 ký tự');
        return;
    }
    
    const submitBtn = document.getElementById('registerBtn');
    const originalText = submitBtn.textContent;
    submitBtn.disabled = true;
    submitBtn.textContent = 'Đang xử lý...';
    
    try {
        console.log('Calling API /auth/register', formData);
        const response = await apiPost('/auth/register', formData);
        console.log('Register response:', response);
        if (response && response.success && response.data) {
            setToken(response.data.token);
            setUserInfo({
                username: response.data.username,
                hoTen: response.data.hoTen,
                email: response.data.email,
                role: response.data.role
            });
            window.location.href = '/dashboard.html';
        } else {
            const errorMsg = response?.message || 'Đăng ký thất bại';
            showError('errorMessage', errorMsg);
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }
    } catch (error) {
        console.error('Register error:', error);
        showError('errorMessage', error.message || 'Đăng ký thất bại. Vui lòng thử lại.');
        submitBtn.disabled = false;
        submitBtn.textContent = originalText;
    }
}

// Logout function
function logout() {
    removeToken();
    localStorage.removeItem('userInfo');
    window.location.href = '/index.html';
}

