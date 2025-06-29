<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="dashboard">
            <i class="bi bi-hospital"></i> Hospital Management
        </a>
        <div class="navbar-nav ms-auto">
            <span class="navbar-text me-3">
                Welcome, <strong>${sessionScope.userId}</strong>
                <span class="badge bg-secondary">${sessionScope.userRole}</span>
            </span>
            <a class="nav-link" href="logout">
                <i class="bi bi-box-arrow-right"></i> Logout
            </a>
        </div>
    </div>
</nav>