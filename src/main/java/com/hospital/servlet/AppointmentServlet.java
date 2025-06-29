//
//package com.hospital.servlet;
//
//import com.hospital.dao.AppointmentDAO;
//import com.hospital.dao.PatientDAO;
//import com.hospital.dao.UserDAO; // CHANGED
//import com.hospital.model.Appointment;
//import com.hospital.model.Patient;
//import com.hospital.model.User; // CHANGED
//import java.io.IOException;
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@WebServlet("/appointments")
//public class AppointmentServlet extends HttpServlet {
//
//    private AppointmentDAO appointmentDAO;
//    private PatientDAO patientDAO;
//    private UserDAO userDAO; // CHANGED
//
//    @Override
//    public void init() {
//        appointmentDAO = new AppointmentDAO();
//        patientDAO = new PatientDAO();
//        userDAO = new UserDAO(); // CHANGED
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");
//
//        switch (action) {
//            case "new":
//                showNewForm(request, response);
//                break;
//            case "edit":
//                showEditForm(request, response);
//                break;
//            case "cancel":
//                cancelAppointment(request, response);
//                break;
//            default: // "list"
//                listAppointments(request, response);
//                break;
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//
//        switch (action) {
//            case "add":
//                addAppointment(request, response);
//                break;
//            case "update":
//                updateAppointment(request, response);
//                break;
//            default:
//                listAppointments(request, response);
//                break;
//        }
//    }
//
//    private void listAppointments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<Appointment> listAppointments = appointmentDAO.getAllAppointments();
//        request.setAttribute("listAppointments", listAppointments);
//        request.getRequestDispatcher("appointment-list.jsp").forward(request, response);
//    }
//
//    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<Patient> listPatients = patientDAO.getAllPatients();
//        // -- CHANGED: Now uses UserDAO to get a list of Users with the DOCTOR role --
//        List<User> listDoctors = userDAO.getAllDoctors();
//        request.setAttribute("listPatients", listPatients);
//        request.setAttribute("listDoctors", listDoctors);
//        request.getRequestDispatcher("appointment-form.jsp").forward(request, response);
//    }
//
//    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        Appointment existingAppointment = appointmentDAO.getAppointmentById(id);
//        List<Patient> listPatients = patientDAO.getAllPatients();
//        // -- CHANGED: Now uses UserDAO to get a list of Users with the DOCTOR role --
//        List<User> listDoctors = userDAO.getAllDoctors();
//
//        request.setAttribute("appointment", existingAppointment);
//        request.setAttribute("listPatients", listPatients);
//        request.setAttribute("listDoctors", listDoctors);
//        request.getRequestDispatcher("appointment-form.jsp").forward(request, response);
//    }
//
//    // The methods below do not require changes.
//
//    private void addAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Appointment appt = new Appointment();
//        appt.setPatientId(Integer.parseInt(request.getParameter("patientId")));
//        appt.setDoctorId(Integer.parseInt(request.getParameter("doctorId")));
//        appt.setReason(request.getParameter("reason"));
//        appt.setNotes(request.getParameter("notes"));
//
//        String datetimeLocal = request.getParameter("appointmentDate");
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//            Date date = sdf.parse(datetimeLocal);
//            appt.setAppointmentDate(new Timestamp(date.getTime()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (appointmentDAO.addAppointment(appt)) {
//            request.getSession().setAttribute("message", "Appointment scheduled successfully!");
//        } else {
//            request.getSession().setAttribute("error", "Failed to schedule appointment.");
//        }
//        response.sendRedirect("appointments?action=list");
//    }
//
//    private void updateAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Appointment appt = new Appointment();
//        appt.setAppointmentId(Integer.parseInt(request.getParameter("appointmentId")));
//        appt.setPatientId(Integer.parseInt(request.getParameter("patientId")));
//        appt.setDoctorId(Integer.parseInt(request.getParameter("doctorId")));
//        appt.setReason(request.getParameter("reason"));
//        appt.setNotes(request.getParameter("notes"));
//        appt.setStatus(request.getParameter("status"));
//
//        String datetimeLocal = request.getParameter("appointmentDate");
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
//            Date date = sdf.parse(datetimeLocal);
//            appt.setAppointmentDate(new Timestamp(date.getTime()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (appointmentDAO.updateAppointment(appt)) {
//            request.getSession().setAttribute("message", "Appointment updated successfully!");
//        } else {
//            request.getSession().setAttribute("error", "Failed to update appointment.");
//        }
//        response.sendRedirect("appointments?action=list");
//    }
//
//    private void cancelAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        if (appointmentDAO.updateAppointmentStatus(id, "CANCELLED")) {
//            request.getSession().setAttribute("message", "Appointment cancelled successfully.");
//        } else {
//            request.getSession().setAttribute("error", "Failed to cancel appointment.");
//        }
//        response.sendRedirect("appointments?action=list");
//    }
//}

package com.hospital.servlet;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.dao.UserDAO;
import com.hospital.model.Appointment;
import com.hospital.model.Patient;
import com.hospital.model.User;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet {

    private AppointmentDAO appointmentDAO;
    private PatientDAO patientDAO;
    private UserDAO userDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
        patientDAO = new PatientDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "cancel":
                cancelAppointment(request, response);
                break;
            default: // "list"
                listAppointments(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addAppointment(request, response);
                break;
            case "update":
                updateAppointment(request, response);
                break;
            default:
                listAppointments(request, response);
                break;
        }
    }

    private void listAppointments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Appointment> listAppointments = appointmentDAO.getAllAppointments();
        request.setAttribute("listAppointments", listAppointments);
        // Set active page before forwarding
        request.setAttribute("activePage", "appointments"); // <-- ADD THIS LINE
        request.getRequestDispatcher("appointment-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Patient> listPatients = patientDAO.getAllPatients();
        List<User> listDoctors = userDAO.getAllDoctors();
        request.setAttribute("listPatients", listPatients);
        request.setAttribute("listDoctors", listDoctors);
        // Set active page before forwarding
        request.setAttribute("activePage", "appointments"); // <-- ADD THIS LINE
        request.getRequestDispatcher("appointment-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Appointment existingAppointment = appointmentDAO.getAppointmentById(id);
        List<Patient> listPatients = patientDAO.getAllPatients();
        List<User> listDoctors = userDAO.getAllDoctors();

        request.setAttribute("appointment", existingAppointment);
        request.setAttribute("listPatients", listPatients);
        request.setAttribute("listDoctors", listDoctors);
        // Set active page before forwarding
        request.setAttribute("activePage", "appointments"); // <-- ADD THIS LINE
        request.getRequestDispatcher("appointment-form.jsp").forward(request, response);
    }

    // The POST methods and the cancel method do not need the attribute
    // because they redirect, which starts a new request. The attribute
    // only needs to be set for server-side forwards.

    private void addAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ... (no changes needed here)
        Appointment appt = new Appointment();
        appt.setPatientId(Integer.parseInt(request.getParameter("patientId")));
        appt.setDoctorId(Integer.parseInt(request.getParameter("doctorId")));
        appt.setReason(request.getParameter("reason"));
        appt.setNotes(request.getParameter("notes"));

        String datetimeLocal = request.getParameter("appointmentDate");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = sdf.parse(datetimeLocal);
            appt.setAppointmentDate(new Timestamp(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (appointmentDAO.addAppointment(appt)) {
            request.getSession().setAttribute("message", "Appointment scheduled successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to schedule appointment.");
        }
        response.sendRedirect("appointments?action=list");
    }

    private void updateAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ... (no changes needed here)
        Appointment appt = new Appointment();
        appt.setAppointmentId(Integer.parseInt(request.getParameter("appointmentId")));
        appt.setPatientId(Integer.parseInt(request.getParameter("patientId")));
        appt.setDoctorId(Integer.parseInt(request.getParameter("doctorId")));
        appt.setReason(request.getParameter("reason"));
        appt.setNotes(request.getParameter("notes"));
        appt.setStatus(request.getParameter("status"));

        String datetimeLocal = request.getParameter("appointmentDate");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = sdf.parse(datetimeLocal);
            appt.setAppointmentDate(new Timestamp(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (appointmentDAO.updateAppointment(appt)) {
            request.getSession().setAttribute("message", "Appointment updated successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to update appointment.");
        }
        response.sendRedirect("appointments?action=list");
    }

    private void cancelAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ... (no changes needed here)
        int id = Integer.parseInt(request.getParameter("id"));
        if (appointmentDAO.updateAppointmentStatus(id, "CANCELLED")) {
            request.getSession().setAttribute("message", "Appointment cancelled successfully.");
        } else {
            request.getSession().setAttribute("error", "Failed to cancel appointment.");
        }
        response.sendRedirect("appointments?action=list");
    }
}