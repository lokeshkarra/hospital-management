<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patient List - Hospital Dashboard</title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>
	<%@ include file="includes/navbar.jsp" %>
    <%
    	response.setHeader("Cache-Control", "no-cache, no-store, must-validate");
	    if(session.getAttribute("user") == null) {
			response.sendRedirect("login.jsp");
			return;
		}
    %>

    <div class="container-fluid mt-4">
        <div class="row">
            <!-- Sidebar - NOW USES THE CORRECT SHARED FILE -->
            <div class="col-md-3 col-lg-2">
                <%@ include file="includes/sidebar.jsp" %>
            </div>

            <!-- Main Content Area -->
            <div class="col-md-9 col-lg-10">
                <h2>Patient Management</h2>
                <hr>

                <!-- Success/Error Messages -->
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

                <div class="d-flex justify-content-between align-items-center mb-3">
                    <a href="patients?action=new" class="btn btn-primary">
                        <i class="bi bi-person-plus"></i> Add New Patient
                    </a>
                    <form class="d-flex" action="patients" method="get">
                        <input type="hidden" name="action" value="search">
                        <input class="form-control me-2" type="search" name="searchTerm" placeholder="Search Patients by Name, Phone, Email..." aria-label="Search">
                        <button class="btn btn-outline-success" type="submit"><i class="bi bi-search"></i> Search</button>
                    </form>
                </div>

                <div class="table-responsive">
                    <table class="table table-hover table-striped">
                        <thead class="table-primary">
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>DOB</th>
                                <th>Gender</th>
                                <th>Blood Group</th>
                                <th>Status</th>
                                <th>Registration Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="patient" items="${listPatients}">
                                <tr>
                                    <td>${patient.patientId}</td>
                                    <td>${patient.firstName} ${patient.lastName}</td>
                                    <td>${patient.email}</td>
                                    <td>${patient.phone}</td>
                                    <td>${patient.dateOfBirth}</td>
                                    <td>${patient.gender}</td>
                                    <td>${patient.bloodGroup}</td>
                                    <td>
                                        <span class="badge 
                                            <c:choose>
                                                <c:when test="${patient.status == 'ACTIVE'}">bg-success</c:when>
                                                <c:when test="${patient.status == 'INACTIVE'}">bg-danger</c:when>
                                                <c:otherwise>bg-secondary</c:otherwise>
                                            </c:choose>
                                        ">
                                            ${patient.status}
                                        </span>
                                    </td>
                                    <td>${patient.registrationDate}</td>
                                    <td>
                                        <a href="patients?action=edit&id=${patient.patientId}" class="btn btn-sm btn-info me-1" title="Edit Patient">
                                            <i class="bi bi-pencil"></i>
                                        </a>
                                        <c:if test="${patient.status == 'ACTIVE'}">
                                            <a href="patients?action=delete&id=${patient.patientId}" class="btn btn-sm btn-danger" title="Deactivate Patient"
                                               onclick="return confirm('Are you sure you want to deactivate patient ID: ${patient.patientId}?');">
                                                <i class="bi bi-person-x"></i>
                                            </a>
                                        </c:if>
                                        <c:if test="${sessionScope.user.role == 'ADMIN'}">
									        <a href="patients?action=purge&id=${patient.patientId}" class="btn btn-sm btn-danger" title="Delete Patient Permanently"
									           onclick="return confirm('WARNING: This action is irreversible and will PERMANENTLY DELETE patient ${patient.fullName} (ID: ${patient.patientId}). Are you absolutely sure?');">
									            <i class="bi bi-trash3-fill"></i>
									        </a>
									    </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listPatients}">
                                <tr>
                                    <td colspan="10" class="text-center">No patients found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
