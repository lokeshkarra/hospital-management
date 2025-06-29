package com.hospital.dao;

import com.hospital.model.Staff;
import com.hospital.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY last_name, first_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                staffList.add(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
    
    public List<Staff> getAllDoctors() {
        List<Staff> doctors = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE role = 'DOCTOR' AND status = 'ACTIVE' ORDER BY last_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                doctors.add(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public Staff getStaffById(int staffId) {
        String sql = "SELECT * FROM staff WHERE staff_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStaff(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addStaff(Staff staff) {
        // In a real app, hash the password before storing!
        String sql = "INSERT INTO staff (user_id, password, first_name, last_name, role, specialization, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getUserId());
            pstmt.setString(2, staff.getPassword()); // HASH THIS
            pstmt.setString(3, staff.getFirstName());
            pstmt.setString(4, staff.getLastName());
            pstmt.setString(5, staff.getRole());
            pstmt.setString(6, staff.getSpecialization());
            pstmt.setString(7, staff.getEmail());
            pstmt.setString(8, staff.getPhone());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE staff SET first_name = ?, last_name = ?, role = ?, specialization = ?, email = ?, phone = ?, status = ? WHERE staff_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getFirstName());
            pstmt.setString(2, staff.getLastName());
            pstmt.setString(3, staff.getRole());
            pstmt.setString(4, staff.getSpecialization());
            pstmt.setString(5, staff.getEmail());
            pstmt.setString(6, staff.getPhone());
            pstmt.setString(7, staff.getStatus());
            pstmt.setInt(8, staff.getStaffId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStaff(int staffId) {
        String sql = "UPDATE staff SET status = 'INACTIVE' WHERE staff_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Staff mapResultSetToStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setStaffId(rs.getInt("staff_id"));
        staff.setUserId(rs.getString("user_id"));
        staff.setFirstName(rs.getString("first_name"));
        staff.setLastName(rs.getString("last_name"));
        staff.setRole(rs.getString("role"));
        staff.setSpecialization(rs.getString("specialization"));
        staff.setEmail(rs.getString("email"));
        staff.setPhone(rs.getString("phone"));
        staff.setHireDate(rs.getDate("hire_date"));
        staff.setStatus(rs.getString("status"));
        return staff;
    }
}