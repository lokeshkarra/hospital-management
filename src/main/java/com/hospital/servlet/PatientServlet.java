package com.hospital.servlet;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {

    private PatientDAO patientDAO;

    @Override
    public void init() {
        patientDAO = new PatientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Default action
        }

        switch (action) {
            case "list":
                listPatients(request, response);
                break;
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete": // This is for deactivating
                deletePatient(request, response);
                break;
            case "purge": // --- NEW CASE ---
                purgePatient(request, response);
                break;
            case "search":
                searchPatients(request, response);
                break;
            default:
                listPatients(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "add"; // Default action for POST
        }

        switch (action) {
            case "add":
                addPatient(request, response);
                break;
            case "update":
                updatePatient(request, response);
                break;
            default:
                response.sendRedirect("patients?action=list");
                break;
        }
    }

    private void listPatients(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Patient> listPatients = patientDAO.getAllPatients();
        request.setAttribute("listPatients", listPatients);
        request.setAttribute("activePage", "patients"); // Set active page before forwarding
        request.getRequestDispatcher("patient-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.setAttribute("activePage", "patients"); // Set active page before forwarding
        request.getRequestDispatcher("patient-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int patientId = Integer.parseInt(request.getParameter("id"));
        Patient existingPatient = patientDAO.getPatientById(patientId);
        request.setAttribute("patient", existingPatient);
        request.setAttribute("activePage", "patients"); // Set active page before forwarding
        request.getRequestDispatcher("patient-form.jsp").forward(request, response);
    }

    private void addPatient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Patient newPatient = extractPatientFromRequest(request);
        if (patientDAO.addPatient(newPatient)) {
            request.getSession().setAttribute("message", "Patient added successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to add patient.");
        }
        response.sendRedirect("patients?action=list"); // Redirect, no need to set activePage
    }

    private void updatePatient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Patient patient = extractPatientFromRequest(request);
        patient.setPatientId(Integer.parseInt(request.getParameter("patientId")));
        if (patientDAO.updatePatient(patient)) {
            request.getSession().setAttribute("message", "Patient updated successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to update patient.");
        }
        response.sendRedirect("patients?action=list"); // Redirect, no need to set activePage
    }

    private void deletePatient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int patientId = Integer.parseInt(request.getParameter("id"));
        if (patientDAO.deletePatient(patientId)) {
            request.getSession().setAttribute("message", "Patient status set to INACTIVE successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to set patient status to INACTIVE.");
        }
        response.sendRedirect("patients?action=list"); // Redirect, no need to set activePage
    }

    // --- NEW METHOD ---
    private void purgePatient(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int patientId = Integer.parseInt(request.getParameter("id"));
        if (patientDAO.purgePatient(patientId)) {
            request.getSession().setAttribute("message", "Patient record has been PERMANENTLY DELETED.");
        } else {
            request.getSession().setAttribute("error", "Could not permanently delete patient. They may have existing appointments or other records linked to them.");
        }
        response.sendRedirect("patients?action=list"); // Redirect, no need to set activePage
    }

    private void searchPatients(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        List<Patient> searchResults = patientDAO.searchPatients(searchTerm);
        request.setAttribute("listPatients", searchResults);
        request.setAttribute("activePage", "patients"); // Set active page before forwarding
        request.getRequestDispatcher("patient-list.jsp").forward(request, response);
    }
    
    // This helper method is fine, no changes needed
    private Patient extractPatientFromRequest(HttpServletRequest request) {
        // ... (no changes needed here)
        Patient patient = new Patient();
        patient.setFirstName(request.getParameter("firstName"));
        patient.setLastName(request.getParameter("lastName"));
        patient.setEmail(request.getParameter("email"));
        patient.setPhone(request.getParameter("phone"));
        patient.setAddress(request.getParameter("address"));
        try {
            String dobString = request.getParameter("dateOfBirth");
            if (dobString != null && !dobString.isEmpty()) {
                patient.setDateOfBirth(Date.valueOf(dobString));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        patient.setGender(request.getParameter("gender"));
        patient.setBloodGroup(request.getParameter("bloodGroup"));
        patient.setEmergencyContact(request.getParameter("emergencyContact"));
        if (request.getParameter("status") != null) {
            patient.setStatus(request.getParameter("status"));
        } else {
            patient.setStatus("ACTIVE");
        }
        return patient;
    }
}