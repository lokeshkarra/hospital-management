<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="card">
    <div class="card-header bg-light">
        <h6 class="mb-0">Quick Menu</h6>
    </div>
    <div class="list-group list-group-flush">
        <%-- Dashboard --%>
        <a href="dashboard" class="list-group-item list-group-item-action ${activePage == 'dashboard' ? 'active bg-primary text-white' : ''}">
            <i class="bi bi-speedometer2"></i> Dashboard
        </a>

        <%-- Clinical & Admin Links --%>
        <c:if test="${sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'DOCTOR'}">
            <a href="patients?action=list" class="list-group-item list-group-item-action ${activePage == 'patients' ? 'active bg-primary text-white' : ''}">
                <i class="bi bi-people"></i> Patients
            </a>
            <a href="appointments?action=list" class="list-group-item list-group-item-action ${activePage == 'appointments' ? 'active bg-primary text-white' : ''}">
                <i class="bi bi-calendar-check"></i> Appointments
            </a>
        </c:if>

        <%-- Front-Desk & Admin Links --%>
        <c:if test="${sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'RECEPTIONIST'}">
            <a href="billing" class="list-group-item list-group-item-action ${activePage == 'billing' ? 'active bg-primary text-white' : ''}">
                <i class="bi bi-receipt-cutoff"></i> Patient Billing
            </a>
        </c:if>

        <%-- Admin-Only Links --%>
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a href="staff?action=list" class="list-group-item list-group-item-action ${activePage == 'staff' ? 'active bg-primary text-white' : ''}">
                <i class="bi bi-person-badge"></i> Staff
            </a>
            
        </c:if>
    </div>
</div>