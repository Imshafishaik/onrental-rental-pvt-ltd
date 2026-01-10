package com.example.onride.controller;

import com.example.onride.dao.BookingDAO;
import com.example.onride.model.Booking;
import com.example.onride.model.Vehicle;
import com.example.onride.dao.PaymentDAO;
import com.example.onride.dao.VehicleDAO;
import com.example.onride.model.Payment;
import com.example.onride.model.SessionManager;
import com.example.onride.model.User;
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
    private PaymentDAO paymentDAO = new PaymentDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();

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
        // prefer session user if available
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null) booking.setCustomerId(current.getId());
        else booking.setCustomerId(customerId);
        booking.setVehicleId(vehicle.getVehicleId());
        booking.setStartDate(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        booking.setEndDate(Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        booking.setTotalAmount(totalAmount);
        booking.setStatus("PENDING");

        bookingDAO.createBooking(booking);

        // Mock payment creation (in real app integrate gateway)
        Payment payment = new Payment();
        payment.setBookingId(booking.getBookingId());
        payment.setAmount(totalAmount);
        payment.setPaymentDate(new Date());
        payment.setPaymentMethod("CARD");
        payment.setStatus("SUCCESS");
        paymentDAO.createPayment(payment);

        // Update vehicle status to BOOKED
        vehicleDAO.updateVehicleStatus(vehicle.getVehicleId(), "BOOKED");

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
