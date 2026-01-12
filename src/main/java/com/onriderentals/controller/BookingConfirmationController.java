package com.onriderentals.controller;

import com.onriderentals.dao.BookingDAO;
import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.Booking;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingConfirmationController {

    @FXML private ImageView vehicleImage;
    @FXML private Label vehicleName;
    @FXML private Label vehicleType;
    @FXML private Label pricePerDayLabel;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Label basePriceLabel;
    @FXML private Label serviceFeeLabel;
    @FXML private Label totalAmountLabel;

    private Vehicle vehicle;
    private double totalAmount;

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        vehicleName.setText(vehicle.getMake() + " " + vehicle.getModel());
        vehicleType.setText(vehicle.getType());
        pricePerDayLabel.setText(String.format("%.0f", vehicle.getPricePerDay()));
        
        // Default dates
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusDays(1));
        
        calculatePricing();
        
        // Listeners
        startDatePicker.valueProperty().addListener((obs, oldV, newV) -> calculatePricing());
        endDatePicker.valueProperty().addListener((obs, oldV, newV) -> calculatePricing());

        // Image loading
        if (vehicle.getImageKey() != null && !vehicle.getImageKey().isEmpty()) {
            String imageUrl = com.onriderentals.util.S3Service.getImageUrl(vehicle.getImageKey());
            if (imageUrl != null) {
                vehicleImage.setImage(new javafx.scene.image.Image(imageUrl, true));
            }
        } else {
            String type = vehicle.getType() != null ? vehicle.getType().toLowerCase() : "car";
            String placeholderUrl = type.contains("bike") 
                ? "https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=600&q=80"
                : "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?auto=format&fit=crop&w=600&q=80";
            vehicleImage.setImage(new javafx.scene.image.Image(placeholderUrl, true));
        }
    }

    private void calculatePricing() {
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) return;
        
        long days = ChronoUnit.DAYS.between(startDatePicker.getValue(), endDatePicker.getValue());
        if (days <= 0) days = 1; // Minimum 1 day
        
        double basePrice = vehicle.getPricePerDay() * days;
        double serviceFee = basePrice * 0.05;
        double insurance = 15.00;
        totalAmount = basePrice + serviceFee + insurance;
        
        basePriceLabel.setText(String.format("$%.2f", basePrice));
        serviceFeeLabel.setText(String.format("$%.2f", serviceFee));
        totalAmountLabel.setText(String.format("$%.2f", totalAmount));
    }

    @FXML
    private void handleBack() {
        try {
            SceneManager.switchScene("VehicleRental");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleConfirmBooking() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            showAlert("Login Required", "Please log in to book a vehicle.");
            SceneManager.switchScene("Login");
            return;
        }

        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        
        if (start == null || end == null || start.isAfter(end)) {
            showAlert("Invalid Dates", "Please select valid check-in and check-out dates.");
            return;
        }

        Booking booking = new Booking();
        booking.setCustomerId(SessionManager.getInstance().getUserId());
        booking.setVehicleId(vehicle.getVehicleId());
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setTotalCost(totalAmount);
        booking.setStatus("CONFIRMED");

        try {
            BookingDAO bookingDAO = new BookingDAO();
            bookingDAO.addBooking(booking);
            
            showSuccess("Booking Successful!", "Your ride is ready! You can view it in 'My Bookings'.");
            SceneManager.switchScene("CustomerDashboard");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Execution Error", "Something went wrong while processing your booking.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
