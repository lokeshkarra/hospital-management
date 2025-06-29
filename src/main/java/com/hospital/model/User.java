//package com.hospital.model;
//
//
//
//public class User {
//    private int id;
//    private String userId;
//    private String password;
//    private String role;
//    private String createdAt;
//    
//    public User() {}
//    
//    public User(String userId, String password, String role) {
//        this.userId = userId;
//        this.password = password;
//        this.role = role;
//    }
//    
//    // Getters and Setters
//    public int getId() {
//        return id;
//    }
//    
//    public void setId(int id) {
//        this.id = id;
//    }
//    
//    public String getUserId() {
//        return userId;
//    }
//    
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//    
//    public String getPassword() {
//        return password;
//    }
//    
//    public void setPassword(String password) {
//        this.password = password;
//    }
//    
//    public String getRole() {
//        return role;
//    }
//    
//    public void setRole(String role) {
//        this.role = role;
//    }
//    
//    public String getCreatedAt() {
//        return createdAt;
//    }
//    
//    public void setCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//    }
//}


package com.hospital.model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String userId; // for login
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String specialization;
    private String email;
    private String phone;
    private Timestamp createdAt;
    private String status;

    public User() {}

    // Getters and Setters for all fields...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
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
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    // Helper for display
    public String getFullName() {
        return firstName + " " + lastName;
    }
}