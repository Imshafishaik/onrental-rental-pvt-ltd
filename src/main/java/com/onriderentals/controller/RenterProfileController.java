package com.onriderentals.controller;

import com.onriderentals.dao.UserDAO;
import com.onriderentals.dao.VehicleDAO;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class RenterProfileController {

    @FXML
    private TextField businessNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private Label memberSinceLabel;
    @FXML
    private Label totalEarningsLabel;
    @FXML
    private Label listedVehiclesLabel;
    @FXML
    private Label activeBookingsLabel;

    private UserDAO userDAO;
    private VehicleDAO vehicleDAO;
    private User currentUser;

    public void initialize() {
        userDAO = new UserDAO();
        vehicleDAO = new VehicleDAO();
        loadUserProfile();
        loadRenterStats();
    }

    private void loadUserProfile() {
        int userId = SessionManager.getInstance().getUserId();
        currentUser = userDAO.getUserById(userId);
        
        if (currentUser != null) {
            businessNameField.setText(currentUser.getName());
            emailField.setText(currentUser.getEmail());
            phoneField.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
            addressField.setText(""); // Could be added as a new field to User model
            memberSinceLabel.setText(currentUser.getCreatedAt() != null ? 
                currentUser.getCreatedAt().toString() : "Unknown");
        }
    }

    private void loadRenterStats() {
        int renterId = SessionManager.getInstance().getUserId();
        
        // Get total vehicles count
        int listedVehicles = vehicleDAO.getVehiclesByRenterId(renterId).size();
        listedVehiclesLabel.setText(String.valueOf(listedVehicles));
        
        // Get total earnings and active bookings
        double totalEarnings = 0.0;
        int activeBookings = 0;
        
        try {
            String sql = "SELECT b.* FROM bookings b JOIN vehicles v ON b.vehicle_id = v.vehicle_id WHERE v.renter_id = ? AND b.status = 'CONFIRMED'";
            try (java.sql.Connection conn = com.onriderentals.dao.Database.getConnection();
                 java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, renterId);
                java.sql.ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    totalEarnings += rs.getDouble("total_amount");
                    // Check if booking is still active
                    java.time.LocalDate endDate = rs.getDate("end_date").toLocalDate();
                    if (!endDate.isBefore(java.time.LocalDate.now())) {
                        activeBookings++;
                    }
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        
        totalEarningsLabel.setText(String.format("$%.0f", totalEarnings));
        activeBookingsLabel.setText(String.valueOf(activeBookings));
    }

    @FXML
    public void updateProfile() {
        if (currentUser == null) return;
        
        String businessName = businessNameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        
        if (businessName == null || businessName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Business name cannot be empty.");
            return;
        }
        
        try {
            currentUser.setName(businessName);
            currentUser.setPhone(phone);
            // Note: Address field would need to be added to User model and database
            
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
