package com.onriderentals.controller;

import com.onriderentals.dao.UserDAO;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class AdminProfileController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private Label adminIdLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label memberSinceLabel;

    private UserDAO userDAO;
    private User currentUser;

    public void initialize() {
        userDAO = new UserDAO();
        loadUserProfile();
    }

    private void loadUserProfile() {
        int userId = SessionManager.getInstance().getUserId();
        currentUser = userDAO.getUserById(userId);
        
        if (currentUser != null) {
            adminIdLabel.setText(String.valueOf(userId));
            nameField.setText(currentUser.getName());
            emailField.setText(currentUser.getEmail());
            roleLabel.setText("System Administrator");
            memberSinceLabel.setText(currentUser.getCreatedAt() != null ? 
                currentUser.getCreatedAt().toString() : "Unknown");
        }
    }

    @FXML
    public void updateProfile() {
        if (currentUser == null) return;
        
        String name = nameField.getText();
        
        if (name == null || name.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name cannot be empty.");
            return;
        }
        
        try {
            currentUser.setName(name);
            userDAO.updateUser(currentUser);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update profile: " + e.getMessage());
        }
    }

    @FXML
    public void systemSettings() {
        showAlert(Alert.AlertType.INFORMATION, "System Settings", 
            "System settings functionality would be implemented here.\n\n" +
            "This could include:\n" +
            "• Database configuration\n" +
            "• Email server settings\n" +
            "• Backup configurations\n" +
            "• Security policies\n" +
            "• System maintenance options");
    }

    @FXML
    public void changePassword() {
        TextInputDialog oldPasswordDialog = new TextInputDialog();
        oldPasswordDialog.setTitle("Change Password");
        oldPasswordDialog.setHeaderText("Enter your current password:");
        oldPasswordDialog.setContentText("Current Password:");

        Optional<String> oldPasswordResult = oldPasswordDialog.showAndWait();
        
        if (!oldPasswordResult.isPresent() || oldPasswordResult.get().isEmpty()) {
            return;
        }
        
        String oldPassword = oldPasswordResult.get();
        
        // Verify current password
        if (!com.onriderentals.util.PasswordUtils.checkPassword(oldPassword, currentUser.getPassword())) {
            showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Current password is incorrect.");
            return;
        }
        
        TextInputDialog newPasswordDialog = new TextInputDialog();
        newPasswordDialog.setTitle("Change Password");
        newPasswordDialog.setHeaderText("Enter your new password:");
        newPasswordDialog.setContentText("New Password:");

        Optional<String> newPasswordResult = newPasswordDialog.showAndWait();
        
        if (newPasswordResult.isPresent() && !newPasswordResult.get().isEmpty()) {
            String newPassword = newPasswordResult.get();
            String hashedPassword = com.onriderentals.util.PasswordUtils.hashPassword(newPassword);
            
            try {
                currentUser.setPassword(hashedPassword);
                userDAO.updateUser(currentUser);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to change password: " + e.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
