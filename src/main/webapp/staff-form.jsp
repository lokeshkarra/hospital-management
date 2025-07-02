<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${not empty staffUser ? 'Edit' : 'Add'} Staff - Hospital Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-validate");
        if(session.getAttribute("user")==null) {
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
                    	
                        <form action="staff" method="post" class="needs-validation" novalidate>
                            <input type="hidden" name="action" value="${not empty staffUser ? 'update' : 'add'}">
                            <c:if test="${not empty staffUser}">
                                <input type="hidden" name="id" value="${staffUser.id}">
                            </c:if>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="firstName" class="form-label">First Name</label>
                                    <input type="text" class="form-control" id="firstName" name="firstName"
                                           value="${staffUser.firstName}" required minlength="2">
                                    <div class="invalid-feedback">
                                        Please enter at least 2 characters for first name.
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label for="lastName" class="form-label">Last Name</label>
                                    <input type="text" class="form-control" id="lastName" name="lastName"
                                           value="${staffUser.lastName}" required minlength="2">
                                    <div class="invalid-feedback">
                                        Please enter at least 2 characters for last name.
                                    </div>
                                </div>
                            </div>

                            <c:if test="${empty staffUser}">
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="userId" class="form-label">User ID (for login)</label>
                                        <input type="text" class="form-control" id="userId" name="userId" required minlength="4" pattern="^[a-zA-Z0-9._-]{4,}$">
                                        <div class="invalid-feedback">
                                            User ID must be at least 4 characters and contain only letters, numbers, dots, underscores, or hyphens.
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="password" class="form-label">Password</label>
                                        <input type="password" class="form-control" id="password" name="password"
                                               required minlength="6">
                                        <div class="invalid-feedback">
                                            Password must be at least 6 characters long.
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="email" class="form-label">Email</label>
                                    <input type="email" class="form-control" id="email" name="email"
                                           value="${staffUser.email}" required>
                                    <div class="invalid-feedback">
                                        Please enter a valid email address.
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label for="phone" class="form-label">Phone</label>
                                    <input type="tel" class="form-control" id="phone" name="phone"
                                           value="${staffUser.phone}" pattern="^[0-9]{7,15}$" required>
                                    <div class="invalid-feedback">
                                        Please enter a valid phone number (7–15 digits).
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="role" class="form-label">Role</label>
                                    <select id="role" name="role" class="form-select" required>
                                        <option value="" disabled ${empty staffUser.role ? 'selected' : ''}>Select role...</option>
                                        <option value="ADMIN" ${staffUser.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                                        <option value="DOCTOR" ${staffUser.role == 'DOCTOR' ? 'selected' : ''}>Doctor</option>
                                        <option value="NURSE" ${staffUser.role == 'NURSE' ? 'selected' : ''}>Nurse</option>
                                        <option value="RECEPTIONIST" ${staffUser.role == 'RECEPTIONIST' ? 'selected' : ''}>Receptionist</option>
                                    </select>
                                    <div class="invalid-feedback">
                                        Please select a role.
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label for="specialization" class="form-label">Specialization (if Doctor)</label>
                                    <input type="text" class="form-control" id="specialization" name="specialization"
                                           value="${staffUser.specialization}">
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

                            <button type="submit" class="btn btn-success">
                                <i class="bi bi-save"></i> Save
                            </button>
                            <a href="staff?action=list" class="btn btn-secondary">
                                <i class="bi bi-x-circle"></i> Cancel
                            </a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS and validation script -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Bootstrap validation
        (function () {
            'use strict';
            var forms = document.querySelectorAll('.needs-validation');
            Array.prototype.slice.call(forms).forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
</body>
</html>