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
        // Just show the main billing page
    	request.setAttribute("activePage", "billing");
        request.getRequestDispatcher("billing.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
        	request.setAttribute("activePage", "billing");
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
            default:
                response.sendRedirect("billing");
        }
    }

    private void searchPatient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            Patient patient = patientDAO.getPatientById(patientId);

            if (patient != null) {
                request.setAttribute("patient", patient);
            } else {
                request.setAttribute("error", "Patient with ID " + patientId + " not found.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Patient ID format. Please enter a number.");
        }
        request.setAttribute("activePage", "billing");
        request.getRequestDispatcher("billing.jsp").forward(request, response);
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

            // Add bill items from form
            addInvoiceItem(invoice, "Room Charges", "Room Rent", request.getParameter("roomCharges"));
            addInvoiceItem(invoice, "Pharmacy", "Medicines & Supplies", request.getParameter("pharmacyCharges"));
            addInvoiceItem(invoice, "Diagnostics", "Lab Tests & Scans", request.getParameter("diagnosticsCharges"));

            if (invoice.getItems().isEmpty()) {
                 request.getSession().setAttribute("error", "Cannot generate an empty bill. Please enter at least one charge.");
                 request.setAttribute("activePage", "billing");
                 response.sendRedirect("billing");
                 return;
            }
            
            // Save the invoice to the database
            if (billingDAO.saveInvoice(invoice)) {
                request.setAttribute("invoice", invoice);
                request.setAttribute("activePage", "billing");
                request.getRequestDispatcher("invoice.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "Failed to save the bill to the database. Please try again.");
                request.setAttribute("activePage", "billing");
                response.sendRedirect("billing");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid data submitted. Please check your entries.");
            request.setAttribute("activePage", "billing");
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