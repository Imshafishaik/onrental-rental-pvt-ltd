package com.onriderentals.controller;

import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.User;
import com.onriderentals.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton; // Assuming you have a register button in your FXML

    @FXML
    private Label messageLabel;

    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    @FXML
    public void login() {
        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                messageLabel.setText("Please enter email and password");
                return;
            }

            User user = userDAO.getUserByEmail(email);

            if (user != null && user.getPassword().equals(password)) {
                SessionManager.getInstance().setUserId(user.getUserId());
                String userType = user.getUserType();
                switch (userType) {
                    case "Admin":
                        SceneManager.switchScene("AdminDashboard");
                        break;
                    case "Renter":
                        SceneManager.switchScene("RenterDashboard");
                        break;
                    case "Customer":
                        SceneManager.switchScene("CustomerDashboard");
                        break;
                    default:
                        messageLabel.setText("Invalid role.");
                        break;
                }
            } else {
                messageLabel.setText("Invalid email or password.");
            }
        } catch (Exception e) {
            messageLabel.setText("Login error: " + e.getMessage());
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void goToRegister() {
        try {
            SceneManager.switchScene("Register");
        } catch (Exception e) {
            System.err.println("Error navigating to Register: " + e.getMessage());
        }
    }

    @FXML
    public void goToVehicleRental() {
        try {
            SceneManager.switchScene("VehicleRental");
        } catch (Exception e) {
            System.err.println("Error navigating to VehicleRental: " + e.getMessage());
        }
    }
}
