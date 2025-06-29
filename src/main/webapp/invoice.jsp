<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Invoice #${invoice.invoiceId}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        @media print {
            .no-print { display: none; }
        }
        .invoice-box { max-width: 800px; margin: auto; padding: 30px; border: 1px solid #eee; box-shadow: 0 0 10px rgba(0, 0, 0, 0.15); font-size: 16px; line-height: 24px; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; color: #555; }
        .invoice-box table { width: 100%; line-height: inherit; text-align: left; }
        .invoice-box table td { padding: 5px; vertical-align: top; }
        .invoice-box table tr.top table td { padding-bottom: 20px; }
        .invoice-box table tr.heading td { background: #eee; border-bottom: 1px solid #ddd; font-weight: bold; }
        .invoice-box table tr.item td { border-bottom: 1px solid #eee; }
        .invoice-box table tr.total td:nth-child(2) { border-top: 2px solid #eee; font-weight: bold; }
    </style>
</head>
<body>
    <div class="container mt-5">
        <div class="invoice-box">
            <table cellpadding="0" cellspacing="0">
                <tr class="top">
                    <td colspan="2">
                        <table>
                            <tr>
                                <td class="title">
                                    <h2>Hospital Management</h2>
                                </td>
                                <td>
                                    Invoice #: ${invoice.invoiceId}<br>
                                    Created: <fmt:formatDate value="${invoice.invoiceDate}" pattern="yyyy-MM-dd HH:mm"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr class="information">
                    <td colspan="2">
                        <table>
                            <tr>
                                <td>
                                    <strong>Billed To:</strong><br>
                                    ${invoice.patient.fullName}<br>
                                    Patient ID: ${invoice.patient.patientId}<br>
                                    ${invoice.patient.address}
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr class="heading">
                    <td>Description</td>
                    <td style="text-align:right;">Price</td>
                </tr>
                <c:forEach var="item" items="${invoice.items}">
                    <tr class="item">
                        <td>${item.category}: ${item.description}</td>
                        <td style="text-align:right;"><fmt:formatNumber value="${item.amount}" type="currency" currencySymbol="₹ "/></td>
                    </tr>
                </c:forEach>
                <tr class="total">
                    <td></td>
                    <td style="text-align:right;">
                       <strong>Total: <fmt:formatNumber value="${invoice.totalAmount}" type="currency" currencySymbol="₹ "/></strong>
                    </td>
                </tr>
            </table>
        </div>
        <div class="text-center mt-4 no-print">
             <div class="alert alert-success">Bill generated successfully!</div>
            <button class="btn btn-primary" onclick="window.print();"><i class="bi bi-printer"></i> Print this invoice</button>
            <a href="billing" class="btn btn-secondary">Create Another Bill</a>
        </div>
    </div>
</body>
</html>