<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Patient Billing</title>
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
                <h2>Patient Billing</h2>
                <hr>
                
                <!-- Display any error/success messages from session -->
                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger">${sessionScope.error}</div>
                    <c:remove var="error" scope="session"/>
                </c:if>
                 <c:if test="${not empty requestScope.error}">
                    <div class="alert alert-danger">${requestScope.error}</div>
                </c:if>

                <!-- Patient Search Form -->
                <div class="card mb-4">
                    <div class="card-header">
                        <i class="bi bi-search"></i> Find Patient
                    </div>
                    <div class="card-body">
                        <form action="billing" method="post" class="d-flex">
                            <input type="hidden" name="action" value="search">
                            <input type="number" name="patientId" class="form-control me-2" placeholder="Enter Patient ID" required value="${patient.patientId}">
                            <button type="submit" class="btn btn-primary">Search</button>
                        </form>
                    </div>
                </div>

                <!-- Billing Form (only shows if a patient is found) -->
                <c:if test="${not empty patient}">
                    <div class="card">
                        <div class="card-header bg-primary text-white">
                            Billing Details for Patient: <strong>${patient.fullName} (ID: ${patient.patientId})</strong>
                        </div>
                        <div class="card-body">
                            <form action="billing" method="post" id="billingForm">
                                <input type="hidden" name="action" value="generate">
                                <input type="hidden" name="patientId" value="${patient.patientId}">

                                <!-- Room Charges -->
                                <div class="mb-3 row">
                                    <label for="roomCharges" class="col-sm-3 col-form-label fs-5"><i class="bi bi-door-open"></i> Room Charges</label>
                                    <div class="col-sm-9">
                                        <input type="number" step="0.01" class="form-control form-control-lg bill-item" id="roomCharges" name="roomCharges" placeholder="0.00">
                                    </div>
                                </div>
                                <!-- Pharmacy Charges -->
                                <div class="mb-3 row">
                                    <label for="pharmacyCharges" class="col-sm-3 col-form-label fs-5"><i class="bi bi-capsule"></i> Pharmacy Bill</label>
                                    <div class="col-sm-9">
                                        <input type="number" step="0.01" class="form-control form-control-lg bill-item" id="pharmacyCharges" name="pharmacyCharges" placeholder="0.00">
                                    </div>
                                </div>
                                <!-- Diagnostics Charges -->
                                <div class="mb-3 row">
                                    <label for="diagnosticsCharges" class="col-sm-3 col-form-label fs-5"><i class="bi bi-file-medical"></i> Diagnostics Bill</label>
                                    <div class="col-sm-9">
                                        <input type="number" step="0.01" class="form-control form-control-lg bill-item" id="diagnosticsCharges" name="diagnosticsCharges" placeholder="0.00">
                                    </div>
                                </div>
                                <hr>
                                <!-- Total -->
                                <div class="mb-4 row align-items-center">
                                    <div class="col-sm-3 fs-4 fw-bold">TOTAL BILL</div>
                                    <div class="col-sm-9">
                                        <div id="totalBill" class="fs-2 fw-bold text-success">₹ 0.00</div>
                                    </div>
                                </div>

                                <button type="submit" class="btn btn-success btn-lg">
                                    <i class="bi bi-receipt"></i> Generate Final Bill
                                </button>
                            </form>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Client-side calculation for immediate feedback
        const billItems = document.querySelectorAll('.bill-item');
        const totalBillDisplay = document.getElementById('totalBill');

        function calculateTotal() {
            let total = 0;
            billItems.forEach(item => {
                if (item.value) {
                    total += parseFloat(item.value);
                }
            });
            totalBillDisplay.textContent = '₹ ' + total.toFixed(2);
        }

        billItems.forEach(item => {
            item.addEventListener('input', calculateTotal);
        });
    </script>
</body>
</html>