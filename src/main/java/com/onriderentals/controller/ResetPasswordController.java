package com.onriderentals.controller;

import com.onriderentals.dao.UserDAO;
import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.SessionManager;
import com.onriderentals.util.OtpManager;
import org.mindrot.jbcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

public class ResetPasswordController {

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button resetButton;

    @FXML
    private Button backButton;

    private UserDAO userDAO;
    private String userEmail;

    public void initialize() {
        userDAO = new UserDAO();
        userEmail = SessionManager.getInstance().getCurrentUserEmail();
        
        if (userEmail == null) {
            showAlert("Error", "Invalid session. Please try again.", Alert.AlertType.ERROR);
            SceneManager.switchScene("ForgotPassword");
        }
    }

    @FXML
    public void handleResetPassword() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please enter both password fields", Alert.AlertType.ERROR);
            return;
        }

        if (newPassword.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters long", Alert.AlertType.ERROR);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match", Alert.AlertType.ERROR);
            return;
        }

        // Hash the new password
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Update password in database
        if (userDAO.updatePasswordByEmail(userEmail, hashedPassword)) {
            // Clear OTP after successful reset
            OtpManager.clearOtp(userEmail);
            
            showAlert("Success", "Password has been reset successfully. Please login with your new password.",
                    Alert.AlertType.INFORMATION);
            
            newPasswordField.clear();
            confirmPasswordField.clear();
            
            // Clear session and navigate to login
            SessionManager.getInstance().clearSession();
            SceneManager.switchScene("Login");
        } else {
            showAlert("Error", "Failed to update password. Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleBackToLogin() {
        // Clear OTP and session
        OtpManager.clearOtp(userEmail);
        SessionManager.getInstance().clearSession();
        SceneManager.switchScene("Login");
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
