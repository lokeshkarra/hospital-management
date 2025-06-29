<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hospital Login</title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center justify-content-center vh-100">
    <div class="card shadow p-4" style="max-width: 400px; width: 100%;">
        <script src="https://unpkg.com/@dotlottie/player-component@2.7.12/dist/dotlottie-player.mjs" type="module"></script>
        <dotlottie-player src="https://lottie.host/1a999f80-bb99-4a46-a2c5-64f8d339f21b/7ZOkhy6um6.lottie" 
                          background="transparent" speed="1" style="width: 300px; height: 300px" loop autoplay>
        </dotlottie-player>
        <h2 class="text-center mb-4">Hospital Login</h2>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">
                ${errorMessage}
            </div>
        </c:if>
        
        <form id="loginForm" action="login" method="post">
            <div class="mb-3">
                <input type="text" id="userId" name="userId" class="form-control" 
                       placeholder="User ID" required value="${param.userId}">
            </div>
            <div class="mb-3">
                <input type="password" id="password" name="password" class="form-control" 
                       placeholder="Password" required>
            </div>
            <p class="text-danger small" id="errorMsg"></p>
            <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>
        
        <div class="mt-3">
            <small class="text-muted">
                Demo credentials:<br>
                Admin: admin123 / Admin@123456<br>
                Doctor: doctor001 / Doctor@123
            </small>
        </div>
    </div>
    
    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const loginForm = document.getElementById('loginForm');
        const errorMsg = document.getElementById('errorMsg');
        
        function validateUserId(userId) {
            return /^[a-zA-Z0-9]{8,}$/.test(userId);
        }
        
        function validatePassword(password) {
            return /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{10,}$/.test(password);
        }
        
        loginForm.addEventListener('submit', function(event) {
            const userId = document.getElementById('userId').value.trim();
            const password = document.getElementById('password').value;
            
            errorMsg.textContent = '';
            
            if (!validateUserId(userId)) {
                errorMsg.textContent = "User ID must be at least 8 characters and alphanumeric.";
                event.preventDefault();
                return;
            }
            
            if (!validatePassword(password)) {
                errorMsg.textContent = "Password must be at least 10 characters, including one uppercase, one number, and one special character.";
                event.preventDefault();
                return;
            }
        });
    </script>
</body>
</html>