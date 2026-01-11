package com.onriderentals.model;

public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String userType;
    private String phone;
    private boolean isActive;

    // Default constructor
    public User() {}

    public User(int userId, String username, String email, String password, String userType, String phone, boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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
