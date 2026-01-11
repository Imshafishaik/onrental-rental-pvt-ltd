package com.onriderentals.model;

public class SessionManager {

    private static SessionManager instance;
    private int userId;
    private String userRole;

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

    public void clearSession() {
        this.userId = 0;
        this.userRole = null;
    }

    public boolean isLoggedIn() {
        return userId > 0;
    }
}
