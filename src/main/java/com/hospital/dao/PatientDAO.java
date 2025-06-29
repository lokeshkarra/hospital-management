//package com.hospital.dao;
//
//import com.hospital.model.Patient;
//import com.hospital.util.DatabaseConnection;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PatientDAO {
//    
//    public List<Patient> getAllPatients() {
//        List<Patient> patients = new ArrayList<>();
//        String sql = "SELECT * FROM patients ORDER BY registration_date DESC";
//        
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {
//            
//            while (rs.next()) {
//                Patient patient = mapResultSetToPatient(rs);
//                patients.add(patient);
//            }
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        
//        return patients;
//    }
//    
//    public Patient getPatientById(int patientId) {
//        String sql = "SELECT * FROM patients WHERE patient_id = ?";
//        
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            
//            pstmt.setInt(1, patientId);
//            ResultSet rs = pstmt.executeQuery();
//            
//            if (rs.next()) {
//                return mapResultSetToPatient(rs);
//            }
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        
//        return null;
//    }
//    
//    public boolean addPatient(Patient patient) {
//        String sql = """
//            INSERT INTO patients (first_name, last_name, email, phone, address, 
//            date_of_birth, gender, blood_group, emergency_contact) 
//            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
//        """;
//        
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            
//            pstmt.setString(1, patient.getFirstName());
//            pstmt.setString(2, patient.getLastName());
//            pstmt.setString(3, patient.getEmail());
//            pstmt.setString(4, patient.getPhone());
//            pstmt.setString(5, patient.getAddress());
//            pstmt.setDate(6, new java.sql.Date(patient.getDateOfBirth().getTime()));
//            pstmt.setString(7, patient.getGender());
//            pstmt.setString(8, patient.getBloodGroup());
//            pstmt.setString(9, patient.getEmergencyContact());
//            
//            int rowsAffected = pstmt.executeUpdate();
//            return rowsAffected > 0;
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    
//    public boolean updatePatient(Patient patient) {
//        String sql = """
//            UPDATE patients SET first_name = ?, last_name = ?, email = ?, 
//            phone = ?, address = ?, date_of_birth = ?, gender = ?, 
//            blood_group = ?, emergency_contact = ?, status = ? 
//            WHERE patient_id = ?
//        """;
//        
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            
//            pstmt.setString(1, patient.getFirstName());
//            pstmt.setString(2, patient.getLastName());
//            pstmt.setString(3, patient.getEmail());
//            pstmt.setString(4, patient.getPhone());
//            pstmt.setString(5, patient.getAddress());
//            pstmt.setDate(6, new java.sql.Date(patient.getDateOfBirth().getTime()));
//            pstmt.setString(7, patient.getGender());
//            pstmt.setString(8, patient.getBloodGroup());
//            pstmt.setString(9, patient.getEmergencyContact());
//            pstmt.setString(10, patient.getStatus());
//            pstmt.setInt(11, patient.getPatientId());
//            
//            int rowsAffected = pstmt.executeUpdate();
//            return rowsAffected > 0;
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    
//    public boolean deletePatient(int patientId) {
//        String sql = "UPDATE patients SET status = 'INACTIVE' WHERE patient_id = ?";
//        
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            
//            pstmt.setInt(1, patientId);
//            int rowsAffected = pstmt.executeUpdate();
//            return rowsAffected > 0;
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    
//    public List<Patient> searchPatients(String searchTerm) {
//        List<Patient> patients = new ArrayList<>();
//        String sql = """
//            SELECT * FROM patients 
//            WHERE (first_name LIKE ? OR last_name LIKE ? OR phone LIKE ? OR email LIKE ?) 
//            AND status = 'ACTIVE'
//            ORDER BY first_name
//        """;
//        
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            
//            String searchPattern = "%" + searchTerm + "%";
//            pstmt.setString(1, searchPattern);
//            pstmt.setString(2, searchPattern);
//            pstmt.setString(3, searchPattern);
//            pstmt.setString(4, searchPattern);
//            
//            ResultSet rs = pstmt.executeQuery();
//            
//            while (rs.next()) {
//                Patient patient = mapResultSetToPatient(rs);
//                patients.add(patient);
//            }
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        
//        return patients;
//    }
//    
//    
//    
//    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
//        Patient patient = new Patient();
//        patient.setPatientId(rs.getInt("patient_id"));
//        patient.setFirstName(rs.getString("first_name"));
//        patient.setLastName(rs.getString("last_name"));
//        patient.setEmail(rs.getString("email"));
//        patient.setPhone(rs.getString("phone"));
//        patient.setAddress(rs.getString("address"));
//        patient.setDateOfBirth(rs.getDate("date_of_birth"));
//        patient.setGender(rs.getString("gender"));
//        patient.setBloodGroup(rs.getString("blood_group"));
//        patient.setEmergencyContact(rs.getString("emergency_contact"));
//        patient.setRegistrationDate(rs.getTimestamp("registration_date"));
//        patient.setStatus(rs.getString("status"));
//        return patient;
//    }
//    
// // Add this method to PatientDAO.java
//
//    public int getTotalPatientCount() {
//        String sql = "SELECT COUNT(*) FROM patients WHERE status = 'ACTIVE'";
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            if (rs.next()) {
//                return rs.getInt(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//}


package com.hospital.dao;

import com.hospital.model.Patient;
import com.hospital.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY registration_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Patient patient = mapResultSetToPatient(rs);
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
    
    public Patient getPatientById(int patientId) {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addPatient(Patient patient) {
        String sql = "INSERT INTO patients (first_name, last_name, email, phone, address, date_of_birth, gender, blood_group, emergency_contact) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getEmail());
            pstmt.setString(4, patient.getPhone());
            pstmt.setString(5, patient.getAddress());
            pstmt.setDate(6, new java.sql.Date(patient.getDateOfBirth().getTime()));
            pstmt.setString(7, patient.getGender());
            pstmt.setString(8, patient.getBloodGroup());
            pstmt.setString(9, patient.getEmergencyContact());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patients SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?, date_of_birth = ?, gender = ?, blood_group = ?, emergency_contact = ?, status = ? WHERE patient_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getEmail());
            pstmt.setString(4, patient.getPhone());
            pstmt.setString(5, patient.getAddress());
            pstmt.setDate(6, new java.sql.Date(patient.getDateOfBirth().getTime()));
            pstmt.setString(7, patient.getGender());
            pstmt.setString(8, patient.getBloodGroup());
            pstmt.setString(9, patient.getEmergencyContact());
            pstmt.setString(10, patient.getStatus());
            pstmt.setInt(11, patient.getPatientId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletePatient(int patientId) {
        String sql = "UPDATE patients SET status = 'INACTIVE' WHERE patient_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- NEW METHOD ---
    public boolean purgePatient(int patientId) {
        // IMPORTANT: This will fail with a foreign key constraint error if the patient
        // has appointments or invoices. This is a safety feature.
        // In a real system, you would delete dependent records first or use ON DELETE CASCADE.
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error purging patient, possibly due to existing related records (e.g., appointments).");
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Patient> searchPatients(String searchTerm) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE (first_name LIKE ? OR last_name LIKE ? OR phone LIKE ? OR email LIKE ?) AND status = 'ACTIVE' ORDER BY first_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
    
    // -- REMOVED DUPLICATE -- only one instance of this method is needed
    public int getTotalPatientCount() {
        String sql = "SELECT COUNT(*) FROM patients WHERE status = 'ACTIVE'";
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
    
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setLastName(rs.getString("last_name"));
        patient.setEmail(rs.getString("email"));
        patient.setPhone(rs.getString("phone"));
        patient.setAddress(rs.getString("address"));
        patient.setDateOfBirth(rs.getDate("date_of_birth"));
        patient.setGender(rs.getString("gender"));
        patient.setBloodGroup(rs.getString("blood_group"));
        patient.setEmergencyContact(rs.getString("emergency_contact"));
        patient.setRegistrationDate(rs.getTimestamp("registration_date"));
        patient.setStatus(rs.getString("status"));
        return patient;
    }
}