//package com.hospital.dao;
//
//import com.hospital.model.Appointment;
//import com.hospital.util.DatabaseConnection;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AppointmentDAO {
//
//    public List<Appointment> getAllAppointments() {
//        List<Appointment> appointments = new ArrayList<>();
//        // JOIN to get patient and doctor names directly
//        String sql = """
//            SELECT a.*, p.first_name AS p_fname, p.last_name AS p_lname, s.first_name AS d_fname, s.last_name AS d_lname 
//            FROM appointments a 
//            JOIN patients p ON a.patient_id = p.patient_id 
//            JOIN staff s ON a.doctor_id = s.staff_id
//            ORDER BY a.appointment_date DESC
//        """;
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            while (rs.next()) {
//                Appointment appt = mapResultSetToAppointment(rs);
//                // Set the joined names
//                appt.setPatientName(rs.getString("p_fname") + " " + rs.getString("p_lname"));
//                appt.setDoctorName("Dr. " + rs.getString("d_fname") + " " + rs.getString("d_lname"));
//                appointments.add(appt);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return appointments;
//    }
//
//    public Appointment getAppointmentById(int id) {
//        String sql = """
//            SELECT a.*, p.first_name AS p_fname, p.last_name AS p_lname, s.first_name AS d_fname, s.last_name AS d_lname 
//            FROM appointments a 
//            JOIN patients p ON a.patient_id = p.patient_id 
//            JOIN staff s ON a.doctor_id = s.staff_id
//            WHERE a.appointment_id = ?
//        """;
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, id);
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                Appointment appt = mapResultSetToAppointment(rs);
//                appt.setPatientName(rs.getString("p_fname") + " " + rs.getString("p_lname"));
//                appt.setDoctorName("Dr. " + rs.getString("d_fname") + " " + rs.getString("d_lname"));
//                return appt;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public boolean addAppointment(Appointment appt) {
//        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, reason, notes) VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, appt.getPatientId());
//            pstmt.setInt(2, appt.getDoctorId());
//            pstmt.setTimestamp(3, new Timestamp(appt.getAppointmentDate().getTime()));
//            pstmt.setString(4, appt.getReason());
//            pstmt.setString(5, appt.getNotes());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean updateAppointment(Appointment appt) {
//        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_date = ?, reason = ?, notes = ?, status = ? WHERE appointment_id = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, appt.getPatientId());
//            pstmt.setInt(2, appt.getDoctorId());
//            pstmt.setTimestamp(3, new Timestamp(appt.getAppointmentDate().getTime()));
//            pstmt.setString(4, appt.getReason());
//            pstmt.setString(5, appt.getNotes());
//            pstmt.setString(6, appt.getStatus());
//            pstmt.setInt(7, appt.getAppointmentId());
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean updateAppointmentStatus(int appointmentId, String status) {
//        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, status);
//            pstmt.setInt(2, appointmentId);
//            return pstmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
//        Appointment appt = new Appointment();
//        appt.setAppointmentId(rs.getInt("appointment_id"));
//        appt.setPatientId(rs.getInt("patient_id"));
//        appt.setDoctorId(rs.getInt("doctor_id"));
//        appt.setAppointmentDate(rs.getTimestamp("appointment_date"));
//        appt.setReason(rs.getString("reason"));
//        appt.setNotes(rs.getString("notes"));
//        appt.setStatus(rs.getString("status"));
//        appt.setCreatedAt(rs.getTimestamp("created_at"));
//        return appt;
//    }
//}


package com.hospital.dao;

import com.hospital.model.Appointment;
import com.hospital.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        // -- CHANGED: The JOIN now uses the 'users' table with alias 'u' --
        String sql = """
            SELECT a.*, p.first_name AS p_fname, p.last_name AS p_lname, u.first_name AS d_fname, u.last_name AS d_lname 
            FROM appointments a 
            JOIN patients p ON a.patient_id = p.patient_id 
            JOIN users u ON a.doctor_id = u.id
            ORDER BY a.appointment_date DESC
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Appointment appt = mapResultSetToAppointment(rs);
                // Set the joined names
                appt.setPatientName(rs.getString("p_fname") + " " + rs.getString("p_lname"));
                appt.setDoctorName("Dr. " + rs.getString("d_fname") + " " + rs.getString("d_lname"));
                appointments.add(appt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public Appointment getAppointmentById(int id) {
        // -- CHANGED: The JOIN now uses the 'users' table with alias 'u' --
        String sql = """
            SELECT a.*, p.first_name AS p_fname, p.last_name AS p_lname, u.first_name AS d_fname, u.last_name AS d_lname 
            FROM appointments a 
            JOIN patients p ON a.patient_id = p.patient_id 
            JOIN users u ON a.doctor_id = u.id
            WHERE a.appointment_id = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Appointment appt = mapResultSetToAppointment(rs);
                appt.setPatientName(rs.getString("p_fname") + " " + rs.getString("p_lname"));
                appt.setDoctorName("Dr. " + rs.getString("d_fname") + " " + rs.getString("d_lname"));
                return appt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // The methods below do not require changes as they don't use JOINs.

    public boolean addAppointment(Appointment appt) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, reason, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appt.getPatientId());
            pstmt.setInt(2, appt.getDoctorId());
            pstmt.setTimestamp(3, new Timestamp(appt.getAppointmentDate().getTime()));
            pstmt.setString(4, appt.getReason());
            pstmt.setString(5, appt.getNotes());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAppointment(Appointment appt) {
        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_date = ?, reason = ?, notes = ?, status = ? WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appt.getPatientId());
            pstmt.setInt(2, appt.getDoctorId());
            pstmt.setTimestamp(3, new Timestamp(appt.getAppointmentDate().getTime()));
            pstmt.setString(4, appt.getReason());
            pstmt.setString(5, appt.getNotes());
            pstmt.setString(6, appt.getStatus());
            pstmt.setInt(7, appt.getAppointmentId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, appointmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appt = new Appointment();
        appt.setAppointmentId(rs.getInt("appointment_id"));
        appt.setPatientId(rs.getInt("patient_id"));
        appt.setDoctorId(rs.getInt("doctor_id"));
        appt.setAppointmentDate(rs.getTimestamp("appointment_date"));
        appt.setReason(rs.getString("reason"));
        appt.setNotes(rs.getString("notes"));
        appt.setStatus(rs.getString("status"));
        appt.setCreatedAt(rs.getTimestamp("created_at"));
        return appt;
    }
    
 // Add these two methods to AppointmentDAO.java

    public int getTodaysAppointmentCount() {
        // DATE() function extracts the date part from a timestamp
        String sql = "SELECT COUNT(*) FROM appointments WHERE DATE(appointment_date) = CURRENT_DATE AND status <> 'CANCELLED'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Appointment> getRecentAppointments(int limit) {
        List<Appointment> appointments = new ArrayList<>();
        // Using FETCH FIRST ... ROWS ONLY for Derby DB to limit results
        String sql = """
            SELECT a.*, p.first_name AS p_fname, p.last_name AS p_lname, u.first_name AS d_fname, u.last_name AS d_lname 
            FROM appointments a 
            JOIN patients p ON a.patient_id = p.patient_id 
            JOIN users u ON a.doctor_id = u.id
            ORDER BY a.created_at DESC FETCH FIRST ? ROWS ONLY
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appt = mapResultSetToAppointment(rs);
                    appt.setPatientName(rs.getString("p_fname") + " " + rs.getString("p_lname"));
                    appt.setDoctorName("Dr. " + rs.getString("d_fname") + " " + rs.getString("d_lname"));
                    appointments.add(appt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
}