package com.hospital.servlet;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.dao.UserDAO;
import com.hospital.model.Appointment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;
    private UserDAO userDAO;

    @Override
    public void init() {
        patientDAO = new PatientDAO();
        appointmentDAO = new AppointmentDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch all the dynamic data
        int totalPatients = patientDAO.getTotalPatientCount();
        int todaysAppointments = appointmentDAO.getTodaysAppointmentCount();
        int activeStaff = userDAO.getActiveStaffCount();
        List<Appointment> recentAppointments = appointmentDAO.getRecentAppointments(3); // Get latest 3

        // Set attributes for the JSP
        request.setAttribute("totalPatients", totalPatients);
        request.setAttribute("todaysAppointments", todaysAppointments);
        request.setAttribute("activeStaff", activeStaff);
        request.setAttribute("recentAppointments", recentAppointments);
        
        // This is a placeholder until a bed management feature is added.
        request.setAttribute("availableBeds", 89); 

        // CRITICAL: Set the active page for the sidebar
        request.setAttribute("activePage", "dashboard");

        // Forward to the JSP
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}