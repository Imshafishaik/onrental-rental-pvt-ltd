package com.onriderentals.model;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password_hash;
    private String role;
    private String phone;
    private boolean isActive;

    // Default constructor
    public User() {
        this.isActive = true;
    }

    public User(int userId, String name, String email, String password_hash, String role, String phone,
            boolean isActive) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.phone = phone;
        this.isActive = isActive;
    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password_hash;
    }

    public void setPassword(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
