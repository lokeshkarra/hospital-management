<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hospital Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    <%-- Security check --%>
    <%
    	response.setHeader("Cache-Control", "no-cache, no-store, must-validate");
	    if(session.getAttribute("user") == null) { // More robust check on the user object
			response.sendRedirect("login.jsp");
			return; // Important to stop further processing
		}
    %>

    <div class="container-fluid mt-4">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2">
                <%@ include file="includes/sidebar.jsp" %>
            </div>

            <!-- Main Content -->
            <div class="col-md-9 col-lg-10">
                <div class="row">
                    <div class="col-12">
                        <h2>Dashboard</h2>
                        <hr>
                    </div>
                </div>

                <!-- Stats Cards with Live Data -->
                <div class="row g-3 mb-4">
                    <div class="col-md-3">
                        <div class="card text-white bg-primary">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h5 class="card-title">Total Patients</h5>
                                        <h3><fmt:formatNumber value="${totalPatients}" /></h3>
                                    </div>
                                    <div class="align-self-center"><i class="bi bi-people fs-1"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-success">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h5 class="card-title">Today's Appointments</h5>
                                        <h3><fmt:formatNumber value="${todaysAppointments}" /></h3>
                                    </div>
                                    <div class="align-self-center"><i class="bi bi-calendar-check fs-1"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-warning">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h5 class="card-title">Available Beds</h5>
                                        <h3><fmt:formatNumber value="${availableBeds}" /></h3>
                                    </div>
                                    <div class="align-self-center"><i class="bi bi-house-door fs-1"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-info">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h5 class="card-title">Active Staff</h5>
                                        <h3><fmt:formatNumber value="${activeStaff}" /></h3>
                                    </div>
                                    <div class="align-self-center"><i class="bi bi-person-badge fs-1"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Recent Activities with Live Data -->
                <div class="row">
                    <div class="col-md-8">
                        <div class="card">
                            <div class="card-header"><h5 class="mb-0">Recent Activities</h5></div>
                            <div class="card-body">
                                <div class="list-group list-group-flush">
                                    <c:if test="${empty recentAppointments}">
                                        <div class="list-group-item">No recent activities found.</div>
                                    </c:if>
                                    <c:forEach var="appt" items="${recentAppointments}">
                                        <div class="list-group-item d-flex justify-content-between align-items-center">
                                            <div>
                                                <h6 class="mb-1">New Appointment Scheduled</h6>
                                                <p class="mb-1">${appt.patientName} with ${appt.doctorName}</p>
                                                <small class="text-muted"><fmt:formatDate value="${appt.createdAt}" pattern="MMM dd, yyyy 'at' hh:mm a" /></small>
                                            </div>
                                            <span class="badge bg-success rounded-pill">Scheduled</span>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card">
                            <div class="card-header"><h5 class="mb-0">Quick Actions</h5></div>
                            <div class="card-body">
                                <div class="d-grid gap-2">
                                    <a href="patients?action=new" class="btn btn-outline-primary"><i class="bi bi-person-plus"></i> Add New Patient</a>
                                    <a href="appointments?action=new" class="btn btn-outline-success"><i class="bi bi-calendar-plus"></i> Schedule Appointment</a>
                                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                        <a href="staff?action=new" class="btn btn-outline-warning"><i class="bi bi-person-badge"></i> Add Staff Member</a>
                                    </c:if>
                                    <a href="billing" class="btn btn-outline-info"><i class="bi bi-receipt-cutoff"></i> Go to Billing</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>