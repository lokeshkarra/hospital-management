<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <%-- CHANGED: checks for "staffUser" object --%>
    <title>${not empty staffUser ? 'Edit' : 'Add'} Staff - Hospital Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    <%-- Security Check --%>
    <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-validate");
        if(session.getAttribute("user")==null) { // This check is fine, it's for the logged-in user
            response.sendRedirect("login.jsp");
            return;
        }
    %>
    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-md-3 col-lg-2">
                <%@ include file="includes/sidebar.jsp" %>
            </div>
            <div class="col-md-9 col-lg-10">
                <h2>${not empty staffUser ? 'Edit' : 'Add New'} Staff Member</h2>
                <hr>
                <div class="card">
                    <div class="card-body">
                        <%-- ALL references to "user" are now "staffUser" --%>
                        <form action="staff" method="post">
                            <input type="hidden" name="action" value="${not empty staffUser ? 'update' : 'add'}">
                            <c:if test="${not empty staffUser}">
                                <input type="hidden" name="id" value="${staffUser.id}">
                            </c:if>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="firstName" class="form-label">First Name</label>
                                    <input type="text" class="form-control" id="firstName" name="firstName" value="${staffUser.firstName}" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="lastName" class="form-label">Last Name</label>
                                    <input type="text" class="form-control" id="lastName" name="lastName" value="${staffUser.lastName}" required>
                                </div>
                            </div>

                            <c:if test="${empty staffUser}">
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="userId" class="form-label">User ID (for login)</label>
                                        <input type="text" class="form-control" id="userId" name="userId" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="password" class="form-label">Password</label>
                                        <input type="password" class="form-control" id="password" name="password" required>
                                    </div>
                                </div>
                            </c:if>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="email" class="form-label">Email</label>
                                    <input type="email" class="form-control" id="email" name="email" value="${staffUser.email}" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="phone" class="form-label">Phone</label>
                                    <input type="tel" class="form-control" id="phone" name="phone" value="${staffUser.phone}">
                                </div>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="role" class="form-label">Role</label>
                                    <select id="role" name="role" class="form-select" required>
                                        <option value="ADMIN" ${staffUser.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                                        <option value="DOCTOR" ${staffUser.role == 'DOCTOR' ? 'selected' : ''}>Doctor</option>
                                        <option value="NURSE" ${staffUser.role == 'NURSE' ? 'selected' : ''}>Nurse</option>
                                        <option value="RECEPTIONIST" ${staffUser.role == 'RECEPTIONIST' ? 'selected' : ''}>Receptionist</option>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <label for="specialization" class="form-label">Specialization (if Doctor)</label>
                                    <input type="text" class="form-control" id="specialization" name="specialization" value="${staffUser.specialization}">
                                </div>
                            </div>
                            
                            <c:if test="${not empty staffUser}">
                                <div class="mb-3">
                                    <label for="status" class="form-label">Status</label>
                                    <select class="form-select" id="status" name="status">
                                        <option value="ACTIVE" ${staffUser.status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                                        <option value="INACTIVE" ${staffUser.status == 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                                    </select>
                                </div>
                            </c:if>

                            <button type="submit" class="btn btn-success"><i class="bi bi-save"></i> Save</button>
                            <a href="staff?action=list" class="btn btn-secondary"><i class="bi bi-x-circle"></i> Cancel</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>