package com.hospital.model;

import java.util.Date;

public class Staff {
    private int staffId;
    private String userId; // For login
    private String password;
    private String firstName;
    private String lastName;
    private String role; // e.g., 'ADMIN', 'DOCTOR', 'NURSE'
    private String specialization;
    private String email;
    private String phone;
    private Date hireDate;
    private String status; // 'ACTIVE', 'INACTIVE'

    public Staff() {}
    
    // Getters and Setters for all fields...

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}