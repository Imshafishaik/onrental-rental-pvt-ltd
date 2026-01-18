package com.onriderentals.model;

public class SessionManager {

    private static SessionManager instance;
    private int userId;
    private String userRole;
    private String currentUserEmail; // For password reset flow

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }

    public void clearSession() {
        this.userId = 0;
        this.userRole = null;
        this.currentUserEmail = null;
    }

    public boolean isLoggedIn() {
        return userId > 0;
    }
}
