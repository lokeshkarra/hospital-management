////package com.hospital.servlet;
////
////import com.hospital.dao.BillingDAO;
////import com.hospital.dao.PatientDAO;
////import com.hospital.model.Invoice;
////import com.hospital.model.InvoiceItem;
////import com.hospital.model.Patient;
////
////import javax.servlet.ServletException;
////import javax.servlet.annotation.WebServlet;
////import javax.servlet.http.HttpServlet;
////import javax.servlet.http.HttpServletRequest;
////import javax.servlet.http.HttpServletResponse;
////import java.io.IOException;
////import java.math.BigDecimal;
////
////@WebServlet("/billing")
////public class BillingServlet extends HttpServlet {
////
////    private PatientDAO patientDAO;
////    private BillingDAO billingDAO;
////
////    @Override
////    public void init() {
////        patientDAO = new PatientDAO();
////        billingDAO = new BillingDAO();
////    }
////
////    @Override
////    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
////        // Just show the main billing page
////    	request.setAttribute("activePage", "billing");
////        request.getRequestDispatcher("billing.jsp").forward(request, response);
////    }
////
////    @Override
////    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
////        String action = request.getParameter("action");
////        if (action == null) {
////        	request.setAttribute("activePage", "billing");
////            response.sendRedirect("billing");
////            return;
////        }
////
////        switch (action) {
////            case "search":
////                searchPatient(request, response);
////                break;
////            case "generate":
////                generateBill(request, response);
////                break;
////            default:
////                response.sendRedirect("billing");
////        }
////    }
////
////    private void searchPatient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
////        try {
////            int patientId = Integer.parseInt(request.getParameter("patientId"));
////            Patient patient = patientDAO.getPatientById(patientId);
////
////            if (patient != null) {
////                request.setAttribute("patient", patient);
////            } else {
////                request.setAttribute("error", "Patient with ID " + patientId + " not found.");
////            }
////        } catch (NumberFormatException e) {
////            request.setAttribute("error", "Invalid Patient ID format. Please enter a number.");
////        }
////        request.setAttribute("activePage", "billing");
////        request.getRequestDispatcher("billing.jsp").forward(request, response);
////    }
////
////    private void generateBill(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
////        try {
////            int patientId = Integer.parseInt(request.getParameter("patientId"));
////            Patient patient = patientDAO.getPatientById(patientId);
////
////            if (patient == null) {
////                request.getSession().setAttribute("error", "Cannot generate bill. Patient not found.");
////                response.sendRedirect("billing");
////                return;
////            }
////
////            Invoice invoice = new Invoice();
////            invoice.setPatient(patient);
////
////            // Add bill items from form
////            addInvoiceItem(invoice, "Room Charges", "Room Rent", request.getParameter("roomCharges"));
////            addInvoiceItem(invoice, "Pharmacy", "Medicines & Supplies", request.getParameter("pharmacyCharges"));
////            addInvoiceItem(invoice, "Diagnostics", "Lab Tests & Scans", request.getParameter("diagnosticsCharges"));
////
////            if (invoice.getItems().isEmpty()) {
////                 request.getSession().setAttribute("error", "Cannot generate an empty bill. Please enter at least one charge.");
////                 request.setAttribute("activePage", "billing");
////                 response.sendRedirect("billing");
////                 return;
////            }
////            
////            // Save the invoice to the database
////            if (billingDAO.saveInvoice(invoice)) {
////                request.setAttribute("invoice", invoice);
////                request.setAttribute("activePage", "billing");
////                request.getRequestDispatcher("invoice.jsp").forward(request, response);
////            } else {
////                request.getSession().setAttribute("error", "Failed to save the bill to the database. Please try again.");
////                request.setAttribute("activePage", "billing");
////                response.sendRedirect("billing");
////            }
////
////        } catch (NumberFormatException e) {
////            request.getSession().setAttribute("error", "Invalid data submitted. Please check your entries.");
////            request.setAttribute("activePage", "billing");
////            response.sendRedirect("billing");
////        }
////    }
////
////    private void addInvoiceItem(Invoice invoice, String category, String description, String amountStr) {
////        if (amountStr != null && !amountStr.trim().isEmpty()) {
////            try {
////                BigDecimal amount = new BigDecimal(amountStr);
////                if (amount.compareTo(BigDecimal.ZERO) > 0) {
////                    invoice.addItem(new InvoiceItem(category, description, amount));
////                }
////            } catch (NumberFormatException e) {
////                // Ignore invalid numbers
////            }
////        }
////    }
////}
//
//
//
//package com.hospital.servlet;
//
//import com.hospital.dao.BillingDAO;
//import com.hospital.dao.PatientDAO;
//import com.hospital.model.Invoice;
//import com.hospital.model.InvoiceItem;
//import com.hospital.model.Patient;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.List;
//
//@WebServlet("/billing")
//public class BillingServlet extends HttpServlet {
//
//    private PatientDAO patientDAO;
//    private BillingDAO billingDAO;
//
//    @Override
//    public void init() {
//        patientDAO = new PatientDAO();
//        billingDAO = new BillingDAO();
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // The default GET request should just show the page with the invoice history
//        listInvoiceHistory(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//        if (action == null) {
//            response.sendRedirect("billing"); // Or forward to doGet
//            return;
//        }
//
//        switch (action) {
//            case "search":
//                // This action is for preparing a NEW bill
//                searchPatientForNewBill(request, response);
//                break;
//            case "generate":
//                generateBill(request, response);
//                break;
//            case "history":
//                // This action is for searching the invoice history
//                listInvoiceHistory(request, response);
//                break;
//            default:
//                response.sendRedirect("billing");
//        }
//    }
//
//    /**
//     * Fetches invoice history based on a search query and forwards to the billing page.
//     * It does NOT set a 'patient' attribute for creating a new bill.
//     */
//    private void listInvoiceHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int searchPatientId = 0; // Default to 0 to get all invoices
//        String patientIdStr = request.getParameter("searchPatientId");
//        if (patientIdStr != null && !patientIdStr.isEmpty()) {
//            try {
//                searchPatientId = Integer.parseInt(patientIdStr);
//            } catch (NumberFormatException e) {
//                request.setAttribute("error", "Invalid Patient ID for history search.");
//            }
//        }
//        
//        List<Invoice> invoiceHistory = billingDAO.getInvoiceHistory(searchPatientId);
//        request.setAttribute("invoiceHistory", invoiceHistory);
//        request.setAttribute("searchedPatientId", patientIdStr); // To keep the search term in the input box
//        
//        request.setAttribute("activePage", "billing");
//        request.getRequestDispatcher("billing.jsp").forward(request, response);
//    }
//
//    /**
//     * Searches for a patient specifically to set up the "Create New Bill" form.
//     * It also fetches the invoice history to display at the bottom of the page.
//     */
//    private void searchPatientForNewBill(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            int patientId = Integer.parseInt(request.getParameter("patientId"));
//            Patient patient = patientDAO.getPatientById(patientId);
//
//            if (patient != null) {
//                // This is the key part: set the patient object for the form
//                request.setAttribute("patient", patient);
//            } else {
//                request.setAttribute("error", "Patient with ID " + patientId + " not found for billing.");
//            }
//        } catch (NumberFormatException e) {
//            request.setAttribute("error", "Invalid Patient ID format. Please enter a number.");
//        }
//        
//        // After setting up the top part of the page, also load the history for the bottom part.
//        // We pass the request and response objects so the 'patient' attribute is preserved.
//        listInvoiceHistory(request, response);
//    }
//
//    // The generateBill and addInvoiceItem methods are correct and do not need changes.
//    private void generateBill(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // ... (this method is correct from the previous step)
//        try {
//            int patientId = Integer.parseInt(request.getParameter("patientId"));
//            Patient patient = patientDAO.getPatientById(patientId);
//
//            if (patient == null) {
//                request.getSession().setAttribute("error", "Cannot generate bill. Patient not found.");
//                response.sendRedirect("billing");
//                return;
//            }
//
//            Invoice invoice = new Invoice();
//            invoice.setPatient(patient);
//
//            addInvoiceItem(invoice, "Room Charges", "Room Rent", request.getParameter("roomCharges"));
//            addInvoiceItem(invoice, "Pharmacy", "Medicines & Supplies", request.getParameter("pharmacyCharges"));
//            addInvoiceItem(invoice, "Diagnostics", "Lab Tests & Scans", request.getParameter("diagnosticsCharges"));
//            
//            String paymentMethod = request.getParameter("paymentMethod");
//            String paymentDetails = "";
//            switch (paymentMethod) {
//                case "Credit Card": case "Debit Card":
//                    paymentDetails = request.getParameter("cardNumber"); break;
//                case "UPI":
//                    paymentDetails = request.getParameter("upiId"); break;
//                case "Cash":
//                    paymentDetails = "Paid in Cash"; break;
//                default:
//                    request.getSession().setAttribute("error", "Invalid payment method selected.");
//                    response.sendRedirect("billing");
//                    return;
//            }
//            invoice.setPaymentMethod(paymentMethod + " (" + paymentDetails + ")");
//            
//            if (invoice.getItems().isEmpty()) {
//                 request.getSession().setAttribute("error", "Cannot generate an empty bill. Please enter at least one charge.");
//                 response.sendRedirect("billing");
//                 return;
//            }
//            
//            if (billingDAO.saveInvoice(invoice)) {
//                request.setAttribute("invoice", invoice);
//                request.getRequestDispatcher("invoice.jsp").forward(request, response);
//            } else {
//                request.getSession().setAttribute("error", "Failed to save the bill to the database. Please try again.");
//                response.sendRedirect("billing");
//            }
//        } catch (NumberFormatException e) {
//            request.getSession().setAttribute("error", "Invalid data submitted. Please check your entries.");
//            response.sendRedirect("billing");
//        }
//    }
//
//    private void addInvoiceItem(Invoice invoice, String category, String description, String amountStr) {
//        // ... (this method is correct from the previous step)
//        if (amountStr != null && !amountStr.trim().isEmpty()) {
//            try {
//                BigDecimal amount = new BigDecimal(amountStr);
//                if (amount.compareTo(BigDecimal.ZERO) > 0) {
//                    invoice.addItem(new InvoiceItem(category, description, amount));
//                }
//            } catch (NumberFormatException e) { /* Ignore */ }
//        }
//    }
//}


package com.hospital.servlet;

import com.hospital.dao.BillingDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.model.Invoice;
import com.hospital.model.InvoiceItem;
import com.hospital.model.Patient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List; // Import List

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private PatientDAO patientDAO;
    private BillingDAO billingDAO;

    @Override
    public void init() {
        patientDAO = new PatientDAO();
        billingDAO = new BillingDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // By default, when visiting the billing page, show the history of all invoices.
        listInvoiceHistory(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("billing");
            return;
        }

        switch (action) {
            case "search":
                searchPatient(request, response);
                break;
            case "generate":
                generateBill(request, response);
                break;
            case "history": // NEW ACTION
                listInvoiceHistory(request, response);
                break;
            default:
                response.sendRedirect("billing");
        }
    }

    /**
     * NEW METHOD: Fetches and displays invoice history.
     */
    private void listInvoiceHistory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int patientId = 0; // Default to 0 to get all invoices
        String patientIdStr = request.getParameter("searchPatientId");
        if (patientIdStr != null && !patientIdStr.isEmpty()) {
            try {
                patientId = Integer.parseInt(patientIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid Patient ID for history search.");
            }
        }
        
        List<Invoice> invoiceHistory = billingDAO.getInvoiceHistory(patientId);
        request.setAttribute("invoiceHistory", invoiceHistory);
        request.setAttribute("searchedPatientId", patientIdStr); // To keep the search term in the input box
        request.setAttribute("activePage", "billing");
        request.getRequestDispatcher("billing.jsp").forward(request, response);
    }


    private void searchPatient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // This method is for preparing a NEW bill, it should not affect the history view
        try {
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            Patient patient = patientDAO.getPatientById(patientId);

            if (patient != null) {
                request.setAttribute("patient", patient);
            } else {
                request.setAttribute("error", "Patient with ID " + patientId + " not found for billing.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Patient ID format. Please enter a number.");
        }
        
        // Also load the history for the bottom of the page
        listInvoiceHistory(request, response);
    }


    
    private void generateBill(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            Patient patient = patientDAO.getPatientById(patientId);

            if (patient == null) {
                request.getSession().setAttribute("error", "Cannot generate bill. Patient not found.");
                response.sendRedirect("billing");
                return;
            }

            Invoice invoice = new Invoice();
            invoice.setPatient(patient);

            // Extract payment parameters
            String paymentMethod = request.getParameter("paymentMethod");
            String paymentNumber = request.getParameter("paymentNumber");
            String paymentStatus = request.getParameter("paymentStatus");
            String expiryDate = request.getParameter("expiryDate");
            String cvv = request.getParameter("cvv");


            // Validate payment
            boolean paymentValid = true;
            String paymentError = null;

            if (paymentMethod == null || paymentMethod.isEmpty()) {
                paymentValid = false;
                paymentError = "Please select a payment method.";
            }
            if (!"YES".equalsIgnoreCase(paymentStatus)) {
                paymentValid = false;
                paymentError = "Payment must be marked successful to generate the bill.";
            }
            if (paymentValid && ("CREDIT".equalsIgnoreCase(paymentMethod) || "DEBIT".equalsIgnoreCase(paymentMethod))) {
                if (paymentNumber == null || !paymentNumber.matches("\\d{16}")) {
                    paymentValid = false;
                    paymentError = "Invalid card number. Must be 16 digits.";
                }
                if (expiryDate == null || !expiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
                    paymentValid = false;
                    paymentError = "Invalid expiry date format. Use MM/YY.";
                }
                if (cvv == null || !cvv.matches("\\d{3}")) {
                    paymentValid = false;
                    paymentError = "Invalid CVV. Must be 3 digits.";
                }
            }

            if (paymentValid && "UPI".equalsIgnoreCase(paymentMethod)) {
                if (paymentNumber == null || !paymentNumber.matches("^[\\w.\\-]+@[\\w.\\-]+$")) {
                    paymentValid = false;
                    paymentError = "Invalid UPI ID format.";
                }
            }

            if (!paymentValid) {
                // Repopulate patient and invoice history
                request.setAttribute("patient", patient);
                request.setAttribute("error", paymentError);
                listInvoiceHistory(request, response);
                return;
            }

            // (Optional) store payment info
            invoice.setPaymentMethod(paymentMethod);
            // invoice.setPaymentReference(paymentNumber);

            addInvoiceItem(invoice, "Room Charges", "Room Rent", request.getParameter("roomCharges"));
            addInvoiceItem(invoice, "Pharmacy", "Medicines & Supplies", request.getParameter("pharmacyCharges"));
            addInvoiceItem(invoice, "Diagnostics", "Lab Tests & Scans", request.getParameter("diagnosticsCharges"));

            if (invoice.getItems().isEmpty()) {
                request.getSession().setAttribute("error", "Cannot generate an empty bill. Please enter at least one charge.");
                response.sendRedirect("billing");
                return;
            }

            if (billingDAO.saveInvoice(invoice)) {
                request.setAttribute("invoice", invoice);
                request.getRequestDispatcher("invoice.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "Failed to save the bill to the database. Please try again.");
                response.sendRedirect("billing");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid data submitted. Please check your entries.");
            response.sendRedirect("billing");
        }
    }


    private void addInvoiceItem(Invoice invoice, String category, String description, String amountStr) {
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                BigDecimal amount = new BigDecimal(amountStr);
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    invoice.addItem(new InvoiceItem(category, description, amount));
                }
            } catch (NumberFormatException e) {
                // Ignore invalid numbers
            }
        }
    }
}