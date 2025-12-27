package com.example.onride.controller;

import com.example.onride.dao.BookingDAO;
import com.example.onride.model.Booking;
import com.example.onride.model.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class BookingDialogController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Button confirmBookingButton;

    private Vehicle vehicle;
    private int customerId;

    private BookingDAO bookingDAO = new BookingDAO();

    public void initialize() {
        // Add listeners to update total cost dynamically
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateTotalAmount());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateTotalAmount());
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setUserId(int userId) {
        this.customerId = userId;
    }

    @FXML
    private void confirmBooking() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        double totalAmount = calculateTotalAmount(startDate, endDate);

        Booking booking = new Booking();
        booking.setCustomerId(customerId);
        booking.setVehicleId(vehicle.getVehicleId());
        booking.setStartDate(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        booking.setEndDate(Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        booking.setTotalAmount(totalAmount);
        booking.setStatus("PENDING");

        bookingDAO.createBooking(booking);

        // Close the dialog
        confirmBookingButton.getScene().getWindow().hide();
    }

    private void updateTotalAmount() {
        try {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            double totalAmount = calculateTotalAmount(startDate, endDate);
            totalAmountLabel.setText(String.format("Total Amount: $%.2f", totalAmount));
        } catch (Exception e) {
            // Handle parsing errors
            totalAmountLabel.setText("Total Amount: $0.00");
        }
    }

    private double calculateTotalAmount(LocalDate start, LocalDate end) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(start, end);
        return days * vehicle.getPricePerDay();
    }
}
