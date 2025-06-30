<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Staff List - Hospital Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    <%-- Security Check --%>
    <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-validate");
        if(session.getAttribute("userId")==null) {
            response.sendRedirect("login.jsp");
        }
    %>
    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-md-3 col-lg-2">
                <%@ include file="includes/sidebar.jsp" %>
            </div>
            <div class="col-md-9 col-lg-10">
                <h2>Staff Management</h2>
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
                    <a href="staff?action=new" class="btn btn-primary">
                        <i class="bi bi-person-plus-fill"></i> Add New Staff
                    </a>
                </div>

                <div class="table-responsive">
                    <table class="table table-hover table-striped">
                        <thead class="table-primary">
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Role</th>
                                <th>Specialization</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%-- CHANGED: items="${listUsers}" and var="user" --%>
                            <c:forEach var="user" items="${listUsers}">
                                <tr>
                                    <td>${user.id}</td>
                                    <td>${user.fullName}</td>
                                    <td><span class="badge bg-info">${user.role}</span></td>
                                    <td>${user.specialization}</td>
                                    <td>${user.email}</td>
                                    <td>${user.phone}</td>
                                    <td>
                                        <span class="badge ${user.status == 'ACTIVE' ? 'bg-success' : 'bg-danger'}">${user.status}</span>
                                    </td>
                                    <td>
                                        <a href="staff?action=edit&id=${user.id}" class="btn btn-sm btn-info" title="Edit"><i class="bi bi-pencil"></i></a>
                                        <c:if test="${user.status == 'ACTIVE'}">
                                            <a href="staff?action=delete&id=${user.id}" class="btn btn-sm btn-danger" title="Deactivate"
                                               onclick="return confirm('Are you sure you want to deactivate ${user.fullName}?');">
                                                <i class="bi bi-person-x"></i>
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