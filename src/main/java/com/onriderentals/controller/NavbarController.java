package com.onriderentals.controller;

import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class NavbarController implements Initializable {

    @FXML private Button dashboardBtn;
    @FXML private Button loginBtn;
    @FXML private Button registerBtn;
    @FXML private Button logoutBtn;
    @FXML private Button profileBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean loggedIn = SessionManager.getInstance().isLoggedIn();
        
        loginBtn.setVisible(!loggedIn);
        loginBtn.setManaged(!loggedIn);
        registerBtn.setVisible(!loggedIn);
        registerBtn.setManaged(!loggedIn);
        
        dashboardBtn.setVisible(loggedIn);
        dashboardBtn.setManaged(loggedIn);
        profileBtn.setVisible(loggedIn);
        profileBtn.setManaged(loggedIn);
        logoutBtn.setVisible(loggedIn);
        logoutBtn.setManaged(loggedIn);
    }

    @FXML
    public void handleHome() {
        try {
            SceneManager.switchScene("Home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBrowseVehicles() {
        try {
            SceneManager.switchScene("VehicleRental");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDashboard() {
        String role = SessionManager.getInstance().getUserRole();
        String sceneName = "CustomerDashboard"; // Default
        
        if (role != null) {
            switch (role.toUpperCase()) {
                case "ADMIN":
                case "-1":
                    sceneName = "AdminDashboard";
                    break;
                case "RENTER":
                case "-2":
                    sceneName = "RenterDashboard";
                    break;
                case "CUSTOMER":
                case "-3":
                default:
                    sceneName = "CustomerDashboard";
                    break;
            }
        }
        
        try {
            SceneManager.switchScene(sceneName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogin() {
        try {
            SceneManager.switchScene("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRegister() {
        try {
            SceneManager.switchScene("Register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleProfile() {
        String role = SessionManager.getInstance().getUserRole();
        String sceneName = "CustomerProfile"; // Default
        
        if (role != null) {
            switch (role.toUpperCase()) {
                case "ADMIN":
                case "-1":
                    sceneName = "AdminProfile";
                    break;
                case "RENTER":
                case "-2":
                    sceneName = "RenterProfile";
                    break;
                case "CUSTOMER":
                case "-3":
                default:
                    sceneName = "CustomerProfile";
                    break;
            }
        }
        
        try {
            SceneManager.switchScene(sceneName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() {
        SessionManager.getInstance().clearSession();
        try {
            SceneManager.switchScene("Home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
