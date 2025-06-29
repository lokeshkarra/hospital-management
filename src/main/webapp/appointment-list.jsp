<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Appointments - Hospital Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    <%
    	response.setHeader("Cache-Control", "no-cache, no-store, must-validate");
	    if(session.getAttribute("userId")==null) {
			response.sendRedirect("login.jsp");
		}
    %>
    
    

    <div class="container-fluid mt-4">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2">
                <%@ include file="includes/sidebar.jsp" %>
            </div>
            
            <div class="col-md-9 col-lg-10">
                <h2>Appointment Management</h2>
                <hr>
                
                <!-- Messages -->
                <c:if test="${not empty sessionScope.message}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${sessionScope.message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <c:remove var="message" scope="session"/>
                </c:if>
                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${sessionScope.error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <div class="mb-3">
                    <a href="appointments?action=new" class="btn btn-primary">
                        <i class="bi bi-calendar-plus"></i> Schedule New Appointment
                    </a>
                </div>

                <div class="table-responsive">
                    <table class="table table-hover table-striped">
                        <thead class="table-primary">
                            <tr>
                                <th>ID</th>
                                <th>Patient</th>
                                <th>Doctor</th>
                                <th>Date & Time</th>
                                <th>Reason</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="appt" items="${listAppointments}">
                                <tr>
                                    <td>${appt.appointmentId}</td>
                                    <td>${appt.patientName}</td>
                                    <td>${appt.doctorName}</td>
                                    <td><fmt:formatDate value="${appt.appointmentDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                                    <td>${appt.reason}</td>
                                    <td>
                                        <c:set var="statusClass" value="bg-secondary"/>
                                        <c:if test="${appt.status == 'SCHEDULED'}"><c:set var="statusClass" value="bg-primary"/></c:if>
                                        <c:if test="${appt.status == 'COMPLETED'}"><c:set var="statusClass" value="bg-success"/></c:if>
                                        <c:if test="${appt.status == 'CANCELLED'}"><c:set var="statusClass" value="bg-danger"/></c:if>
                                        <span class="badge ${statusClass}">${appt.status}</span>
                                    </td>
                                    <td>
                                        <a href="appointments?action=edit&id=${appt.appointmentId}" class="btn btn-sm btn-info" title="Edit"><i class="bi bi-pencil"></i></a>
                                        <c:if test="${appt.status == 'SCHEDULED'}">
                                            <a href="appointments?action=cancel&id=${appt.appointmentId}" class="btn btn-sm btn-warning" title="Cancel"
                                               onclick="return confirm('Are you sure you want to cancel this appointment?');">
                                               <i class="bi bi-calendar-x"></i>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>