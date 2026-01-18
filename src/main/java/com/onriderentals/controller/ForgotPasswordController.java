package com.onriderentals.controller;

import com.onriderentals.dao.UserDAO;
import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.SessionManager;
import com.onriderentals.util.EmailService;
import com.onriderentals.util.OtpManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private Button submitButton;

    @FXML
    private Button backButton;

    private UserDAO userDAO;

    public void initialize() {
        userDAO = new UserDAO();
    }

    @FXML
    public void handleSubmit() {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showAlert("Error", "Please enter your email address", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Please enter a valid email address", Alert.AlertType.ERROR);
            return;
        }

        // Check if user exists with this email
        if (!userDAO.userExistsByEmail(email)) {
            showAlert("Error", "No account found with this email address", Alert.AlertType.ERROR);
            return;
        }

        // Generate OTP (6-digit code with 5-minute validity)
        String otp = OtpManager.generateOtp(email);

        // Send OTP via email
        boolean emailSent = EmailService.sendPasswordResetEmail(email, getUserNameByEmail(email), otp);

        if (emailSent) {
            showAlert("Success",
                    "OTP has been sent to your email. Please check your inbox. OTP is valid for 5 minutes.",
                    Alert.AlertType.INFORMATION);
            emailField.clear();
            // Navigate to OTP verification page
            SessionManager.getInstance().setCurrentUserEmail(email);
            SceneManager.switchScene("VerifyOtp");
        } else {
            showAlert("Error", "Failed to send OTP email. Please try again later.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleBackToLogin() {
        SceneManager.switchScene("Login");
    }

    private String getUserNameByEmail(String email) {
        // This method should retrieve the user's name from database
        // For now, return a generic greeting
        return "User";
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
