<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${(patient != null) ? 'Edit Patient' : 'Add New Patient'}" /> - Hospital Dashboard</title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>
	
    <!-- Navigation Bar -->
    <%@ include file="includes/navbar.jsp" %>
    <%
    	response.setHeader("Cache-Control", "no-cache, no-store, must-validate");
	    if(session.getAttribute("user")==null) { // Use the 'user' object check
			response.sendRedirect("login.jsp");
			return;
		}
    %>
    
    <div class="container-fluid mt-4">
        <div class="row">
            
            <!-- Sidebar - CHANGED TO USE THE INCLUDE -->
            <div class="col-md-3 col-lg-2">
                <%@ include file="includes/sidebar.jsp" %>
            </div>

            <!-- Main Content Area -->
            <div class="col-md-9 col-lg-10">
                <h2><c:out value="${(patient != null) ? 'Edit Patient' : 'Add New Patient'}" /></h2>
                <hr>

                <div class="card">
                    <div class="card-body">
                        <form action="patients" method="post">
                            <!-- Hidden input to determine action in servlet -->
                            <input type="hidden" name="action" value="<c:out value="${(patient != null) ? 'update' : 'add'}" />">

                            <!-- Patient ID for updates -->
                            <c:if test="${patient != null}">
                                <input type="hidden" name="patientId" value="${patient.patientId}">
                                <div class="mb-3 row">
                                    <label for="patientIdDisplay" class="col-sm-2 col-form-label">Patient ID</label>
                                    <div class="col-sm-10">
                                        <input type="text" readonly class="form-control-plaintext" id="patientIdDisplay" value="${patient.patientId}">
                                    </div>
                                </div>
                            </c:if>

                            <div class="mb-3 row">
                                <label for="firstName" class="col-sm-2 col-form-label">First Name</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="firstName" name="firstName" 
                                           value="<c:out value="${patient.firstName}" />" required>
                                </div>
                            </div>

                            <div class="mb-3 row">
                                <label for="lastName" class="col-sm-2 col-form-label">Last Name</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="lastName" name="lastName" 
                                           value="<c:out value="${patient.lastName}" />" required>
                                </div>
                            </div>

                            <div class="mb-3 row">
                                <label for="email" class="col-sm-2 col-form-label">Email</label>
                                <div class="col-sm-10">
                                    <input type="email" class="form-control" id="email" name="email" 
                                           value="<c:out value="${patient.email}" />">
                                </div>
                            </div>

                            <div class="mb-3 row">
                                <label for="phone" class="col-sm-2 col-form-label">Phone</label>
                                <div class="col-sm-10">
                                    <input type="tel" class="form-control" id="phone" name="phone" 
                                           value="<c:out value="${patient.phone}" />">
                                </div>
                            </div>

                            <div class="mb-3 row">
                                <label for="address" class="col-sm-2 col-form-label">Address</label>
                                <div class="col-sm-10">
                                    <textarea class="form-control" id="address" name="address" rows="3"><c:out value="${patient.address}" /></textarea>
                                </div>
                            </div>

                            <div class="mb-3 row">
                                <label for="dateOfBirth" class="col-sm-2 col-form-label">Date of Birth</label>
                                <div class="col-sm-10">
                                    <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" 
                                           value="<c:out value="${patient.dateOfBirth}" />" required>
                                </div>
                            </div>

                            <div class="mb-3 row">
                                <label for="gender" class="col-sm-2 col-form-label">Gender</label>
                                <div class="col-sm-10">
                                    <select class="form-select" id="gender" name="gender" required>
                                        <option value="">Select Gender</option>
                                        <option value="Male" <c:if test="${patient.gender == 'Male'}">selected</c:if>>Male</option>
                                        <option value="Female" <c:if test="${patient.gender == 'Female'}">selected</c:if>>Female</option>
                                        <option value="Other" <c:if test="${patient.gender == 'Other'}">selected</c:if>>Other</option>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-3 row">
                                <label for="bloodGroup" class="col-sm-2 col-form-label">Blood Group</label>
                                <div class="col-sm-10">
                                    <select class="form-select" id="bloodGroup" name="bloodGroup">
                                        <option value="">Select Blood Group</option>
                                        <option value="A+" <c:if test="${patient.bloodGroup == 'A+'}">selected</c:if>>A+</option>
                                        <option value="A-" <c:if test="${patient.bloodGroup == 'A-'}">selected</c:if>>A-</option>
                                        <option value="B+" <c:if test="${patient.bloodGroup == 'B+'}">selected</c:if>>B+</option>
                                        <option value="B-" <c:if test="${patient.bloodGroup == 'B-'}">selected</c:if>>B-</option>
                                        <option value="AB+" <c:if test="${patient.bloodGroup == 'AB+'}">selected</c:if>>AB+</option>
                                        <option value="AB-" <c:if test="${patient.bloodGroup == 'AB-'}">selected</c:if>>AB-</option>
                                        <option value="O+" <c:if test="${patient.bloodGroup == 'O+'}">selected</c:if>>O+</option>
                                        <option value="O-" <c:if test="${patient.bloodGroup == 'O-'}">selected</c:if>>O-</option>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-3 row">
                                <label for="emergencyContact" class="col-sm-2 col-form-label">Emergency Contact</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" id="emergencyContact" name="emergencyContact" 
                                           value="<c:out value="${patient.emergencyContact}" />">
                                </div>
                            </div>

                            <!-- Status field only visible for editing existing patients -->
                            <c:if test="${patient != null}">
                                <div class="mb-3 row">
                                    <label for="status" class="col-sm-2 col-form-label">Status</label>
                                    <div class="col-sm-10">
                                        <select class="form-select" id="status" name="status">
                                            <option value="ACTIVE" <c:if test="${patient.status == 'ACTIVE'}">selected</c:if>>Active</option>
                                            <option value="INACTIVE" <c:if test="${patient.status == 'INACTIVE'}">selected</c:if>>Inactive</option>
                                        </select>
                                    </div>
                                </div>
                            </c:if>

                            <div class="row">
                                <div class="col-sm-10 offset-sm-2">
                                    <button type="submit" class="btn btn-success me-2">
                                        <i class="bi bi-save"></i> Save Patient
                                    </button>
                                    <a href="patients?action=list" class="btn btn-secondary">
                                        <i class="bi bi-x-circle"></i> Cancel
                                    </a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>