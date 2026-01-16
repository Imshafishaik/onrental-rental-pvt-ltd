package com.onriderentals.controller;

import com.onriderentals.dao.UserDAO;
import com.onriderentals.dao.BookingDAO;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.application.Platform;
import java.util.Optional;

public class CustomerProfileController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private Label memberSinceLabel;
    @FXML
    private Label totalBookingsLabel;
    @FXML
    private Label loyaltyPointsLabel;

    private UserDAO userDAO;
    private BookingDAO bookingDAO;
    private User currentUser;

    public void initialize() {
        userDAO = new UserDAO();
        bookingDAO = new BookingDAO();
        loadUserProfile();
        loadUserStats();
    }

    private void loadUserProfile() {
        int userId = SessionManager.getInstance().getUserId();
        currentUser = userDAO.getUserById(userId);
        
        if (currentUser != null) {
            nameField.setText(currentUser.getName());
            emailField.setText(currentUser.getEmail());
            phoneField.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
            memberSinceLabel.setText(currentUser.getCreatedAt() != null ? 
                currentUser.getCreatedAt().toString() : "Unknown");
        }
    }

    private void loadUserStats() {
        int customerId = SessionManager.getInstance().getUserId();
        
        // Get total bookings count
        int totalBookings = bookingDAO.getBookingsByCustomerId(customerId).size();
        totalBookingsLabel.setText(String.valueOf(totalBookings));
        
        // Calculate loyalty points (10 points per booking)
        int loyaltyPoints = totalBookings * 10;
        loyaltyPointsLabel.setText(String.valueOf(loyaltyPoints));
    }

    @FXML
    public void updateProfile() {
        if (currentUser == null) return;
        
        String name = nameField.getText();
        String phone = phoneField.getText();
        
        if (name == null || name.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name cannot be empty.");
            return;
        }
        
        try {
            currentUser.setName(name);
            currentUser.setPhone(phone);
            
            userDAO.updateUser(currentUser);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update profile: " + e.getMessage());
        }
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
