<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${not empty appointment ? 'Edit' : 'Schedule'} Appointment - Hospital Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-md-3 col-lg-2">
                <%@ include file="includes/sidebar.jsp" %>
            </div>
            <div class="col-md-9 col-lg-10">
                <h2>${not empty appointment ? 'Edit' : 'Schedule New'} Appointment</h2>
                <hr>
                <div class="card">
                    <div class="card-body">
                        <form action="appointments" method="post">
                            <input type="hidden" name="action" value="${not empty appointment ? 'update' : 'add'}">
                            <c:if test="${not empty appointment}">
                                <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                            </c:if>
                            
                            <div class="mb-3">
                                <label for="patientId" class="form-label">Patient</label>
                                <select id="patientId" name="patientId" class="form-select" required>
                                    <option value="">Select a Patient</option>
                                    <c:forEach var="p" items="${listPatients}">
                                        <option value="${p.patientId}" ${appointment.patientId == p.patientId ? 'selected' : ''}>
                                            ${p.fullName} (ID: ${p.patientId})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label for="doctorId" class="form-label">Doctor</label>
                                <select id="doctorId" name="doctorId" class="form-select" required>
                                    <option value="">Select a Doctor</option>
                                    <c:forEach var="d" items="${listDoctors}">
									    <option value="${d.id}" ${appointment.doctorId == d.id ? 'selected' : ''}>
									        Dr. ${d.fullName} - ${d.specialization}
									    </option>
									</c:forEach>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label for="appointmentDate" class="form-label">Appointment Date and Time</label>
                                <fmt:formatDate value="${appointment.appointmentDate}" pattern="yyyy-MM-dd'T'HH:mm" var="formattedDate" />
                                <input type="datetime-local" class="form-control" id="appointmentDate" name="appointmentDate" value="${formattedDate}" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="reason" class="form-label">Reason for Visit</label>
                                <input type="text" class="form-control" id="reason" name="reason" value="${appointment.reason}">
                            </div>

                            <div class="mb-3">
                                <label for="notes" class="form-label">Notes</label>
                                <textarea class="form-control" id="notes" name="notes" rows="3">${appointment.notes}</textarea>
                            </div>

                            <c:if test="${not empty appointment}">
                                <div class="mb-3">
                                    <label for="status" class="form-label">Status</label>
                                    <select id="status" name="status" class="form-select" required>
                                        <option value="SCHEDULED" ${appointment.status == 'SCHEDULED' ? 'selected' : ''}>Scheduled</option>
                                        <option value="COMPLETED" ${appointment.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                                        <option value="CANCELLED" ${appointment.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                    </select>
                                </div>
                            </c:if>

                            <button type="submit" class="btn btn-success"><i class="bi bi-save"></i> Save Appointment</button>
                            <a href="appointments?action=list" class="btn btn-secondary"><i class="bi bi-x-circle"></i> Cancel</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>