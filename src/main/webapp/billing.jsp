<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
                
                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">${sessionScope.error}<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
                    <c:remove var="error" scope="session"/>
                </c:if>
                 <c:if test="${not empty requestScope.error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">${requestScope.error}<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
                </c:if>

                <!-- Patient Search for NEW BILL -->
                <div class="card mb-4">
                    <div class="card-header">
                        <i class="bi bi-person-plus"></i> Create New Bill
                    </div>
                    <div class="card-body">
                        <form action="billing" method="post" class="d-flex">
                            <input type="hidden" name="action" value="search">
                            <input type="number" name="patientId" class="form-control me-2" placeholder="Enter Patient ID to start a new bill" required value="${patient.patientId}">
                            <button type="submit" class="btn btn-primary">Search Patient</button>
                        </form>
                    </div>
                </div>

                <!-- Billing Form (only shows if a patient for a NEW bill is found) -->
                <c:if test="${not empty patient}">
                    <!-- ... this entire section remains unchanged ... -->
                    <div class="card mb-4">
                        <div class="card-header bg-primary text-white">
                            Billing Details for Patient: <strong>${patient.fullName} (ID: ${patient.patientId})</strong>
                        </div>
                        <div class="card-body">
                            <form action="billing" method="post" id="billingForm">
                                <input type="hidden" name="action" value="generate">
                                <input type="hidden" name="patientId" value="${patient.patientId}">
                                <div class="mb-3 row">
                                    <label for="roomCharges" class="col-sm-3 col-form-label fs-5"><i class="bi bi-door-open"></i> Room Charges</label>
                                    <div class="col-sm-9"><input type="number" step="0.01" min="0" class="form-control form-control-lg bill-item" id="roomCharges" name="roomCharges" placeholder="0.00"></div>
                                </div>
                                <div class="mb-3 row">
                                    <label for="pharmacyCharges" class="col-sm-3 col-form-label fs-5"><i class="bi bi-capsule"></i> Pharmacy Bill</label>
                                    <div class="col-sm-9"><input type="number" step="0.01" min="0" class="form-control form-control-lg bill-item" id="pharmacyCharges" name="pharmacyCharges" placeholder="0.00"></div>
                                </div>
                                <div class="mb-3 row">
                                    <label for="diagnosticsCharges" class="col-sm-3 col-form-label fs-5"><i class="bi bi-file-medical"></i> Diagnostics Bill</label>
                                    <div class="col-sm-9"><input type="number" step="0.01" min="0" class="form-control form-control-lg bill-item" id="diagnosticsCharges" name="diagnosticsCharges" placeholder="0.00"></div>
                                </div>
                                <hr>
                                <div class="mb-4 row align-items-center">
                                    <div class="col-sm-3 fs-4 fw-bold">TOTAL BILL</div>
                                    <div class="col-sm-9"><div id="totalBill" class="fs-2 fw-bold text-success">₹ 0.00</div></div>
                                </div>
                                
                                <hr>
								<div class="mb-3 row">
								    <label class="col-sm-3 col-form-label fs-5"><i class="bi bi-credit-card"></i> Payment Method</label>
								    <div class="col-sm-9">
								        <select name="paymentMethod" id="paymentMethod" class="form-select form-select-lg" required>
								            <option value="">Select Payment Method</option>
								            <option value="CREDIT">Credit Card</option>
								            <option value="DEBIT">Debit Card</option>
								            <option value="UPI">UPI</option>
								            <option value="CASH">Cash</option>
								        </select>
								    </div>
								</div>
								
								<div class="mb-3 row d-none" id="paymentDetailsBlock">
								    <label for="paymentNumber" class="col-sm-3 col-form-label fs-5"><i class="bi bi-hash"></i> Payment Number / UPI ID</label>
								    <div class="col-sm-9">
								        <input type="text" id="paymentNumber" name="paymentNumber" class="form-control form-control-lg" placeholder="">
								        <div class="form-text" id="paymentHelp"></div>
								    </div>
								</div>
								
								<div class="mb-3 row d-none" id="expiryCvvBlock">
								    <label class="col-sm-3 col-form-label fs-5"><i class="bi bi-calendar"></i> Expiry & CVV</label>
								    <div class="col-sm-4">
								        <input type="text" id="expiryDate" name="expiryDate" class="form-control form-control-lg" placeholder="MM/YY">
								    </div>
								    <div class="col-sm-2">
								        <input type="text" id="cvv" name="cvv" class="form-control form-control-lg" placeholder="CVV">
								    </div>
								</div>

								
								<div class="mb-3 row">
								    <label class="col-sm-3 col-form-label fs-5"><i class="bi bi-question-circle"></i> Payment Successful?</label>
								    <div class="col-sm-9">
								        <select name="paymentStatus" class="form-select form-select-lg" required>
								            <option value="">Select</option>
								            <option value="YES">Yes</option>
								            <option value="NO">No</option>
								        </select>
								    </div>
								</div>
                                
                                <button type="submit" class="btn btn-success btn-lg"><i class="bi bi-receipt"></i> Generate Final Bill</button>
                            </form>
                        </div>
                    </div>
                </c:if>

                <!-- NEW: Invoice History Section -->
                <div class="card">
                    <div class="card-header">
                        <i class="bi bi-clock-history"></i> Invoice History
                    </div>
                    <div class="card-body">
                        <!-- History Search Form -->
                        <form action="billing" method="post" class="mb-3">
                            <input type="hidden" name="action" value="history">
                            <div class="input-group">
                                <input type="number" name="searchPatientId" class="form-control" placeholder="Search by Patient ID (leave blank for all)" value="${searchedPatientId}">
                                <button type="submit" class="btn btn-outline-secondary">Search History</button>
                            </div>
                        </form>

                        <!-- History Table -->
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-light">
                                    <tr>
                                        <th>Invoice ID</th>
                                        <th>Patient</th>
                                        <th>Date</th>
                                        <th>Total Amount</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${empty invoiceHistory}">
                                        <tr><td colspan="6" class="text-center">No invoice history found.</td></tr>
                                    </c:if>
                                    <c:forEach var="inv" items="${invoiceHistory}">
                                        <tr>
                                            <td>#${inv.invoiceId}</td>
                                            <td>${inv.patient.fullName} (ID: ${inv.patient.patientId})</td>
                                            <td><fmt:formatDate value="${inv.invoiceDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                                            <td><fmt:formatNumber value="${inv.totalAmount}" type="currency" currencySymbol="₹ "/></td>
                                            <td>
                                                <span class="badge ${inv.status == 'PAID' ? 'bg-success' : 'bg-warning'}">${inv.status}</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-sm btn-info" title="View Details">
                                                    <i class="bi bi-eye"></i> View
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const billItems = document.querySelectorAll('.bill-item');
        const totalBillDisplay = document.getElementById('totalBill');

        function calculateTotal() {
            let total = 0;
            billItems.forEach(item => {
                const value = parseFloat(item.value);
                if (!isNaN(value) && value > 0) {
                    total += value;
                }
            });
            totalBillDisplay.textContent = '₹ ' + total.toFixed(2);
        }

        billItems.forEach(item => {
            item.addEventListener('input', calculateTotal);
        });
    </script>
    <script>
    const paymentMethodSelect = document.getElementById('paymentMethod');
    const paymentDetailsBlock = document.getElementById('paymentDetailsBlock');
    const paymentNumberInput = document.getElementById('paymentNumber');
    const paymentHelp = document.getElementById('paymentHelp');

    paymentMethodSelect.addEventListener('change', () => {
        const method = paymentMethodSelect.value;

        if (method === 'CREDIT' || method === 'DEBIT') {
            paymentDetailsBlock.classList.remove('d-none');
            expiryCvvBlock.classList.remove('d-none');
            paymentNumberInput.required = true;
            document.getElementById('expiryDate').required = true;
            document.getElementById('cvv').required = true;
            paymentNumberInput.placeholder = 'Enter 16-digit card number';
            paymentHelp.textContent = 'Format: 16 digits';
        } else if (method === 'UPI') {
            paymentDetailsBlock.classList.remove('d-none');
            expiryCvvBlock.classList.add('d-none');
            paymentNumberInput.required = true;
            document.getElementById('expiryDate').required = false;
            document.getElementById('expiryDate').value = '';
            document.getElementById('cvv').required = false;
            document.getElementById('cvv').value = '';
            paymentNumberInput.placeholder = 'Enter UPI ID';
            paymentHelp.textContent = 'Format: yourname@bank';
        } else {
            paymentDetailsBlock.classList.add('d-none');
            expiryCvvBlock.classList.add('d-none');
            paymentNumberInput.required = false;
            paymentNumberInput.value = '';
            document.getElementById('expiryDate').required = false;
            document.getElementById('expiryDate').value = '';
            document.getElementById('cvv').required = false;
            document.getElementById('cvv').value = '';
            paymentHelp.textContent = '';
        }
    });


    // Optional: Client-side basic validation before submission
    const billingForm = document.getElementById('billingForm');
    billingForm.addEventListener('submit', (e) => {
        const method = paymentMethodSelect.value;
        const paymentNumber = paymentNumberInput.value.trim();
        const expiryDate = document.getElementById('expiryDate').value.trim();
        const cvv = document.getElementById('cvv').value.trim();

        if ((method === 'CREDIT' || method === 'DEBIT')) {
            if (!/^\d{16}$/.test(paymentNumber)) {
                alert('Please enter a valid 16-digit card number.');
                e.preventDefault();
                return;
            }
            if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(expiryDate)) {
                alert('Please enter expiry date in MM/YY format.');
                e.preventDefault();
                return;
            }
            if (!/^\d{3}$/.test(cvv)) {
                alert('Please enter a valid 3-digit CVV.');
                e.preventDefault();
                return;
            }
        }

        if (method === 'UPI') {
            if (!/^[\w.\-]+@[\w.\-]+$/.test(paymentNumber)) {
                alert('Please enter a valid UPI ID.');
                e.preventDefault();
            }
        }
    });

</script>
    
</body>
</html>