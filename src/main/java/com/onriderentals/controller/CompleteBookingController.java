package com.onriderentals.controller;

import com.onriderentals.dao.BookingDAO;
import com.onriderentals.dao.ReviewDAO;
import com.onriderentals.dao.VehicleDAO;
import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.Booking;
import com.onriderentals.model.Review;
import com.onriderentals.model.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class CompleteBookingController {

    @FXML
    private Label bookingIdLabel;

    @FXML
    private Label vehicleLabel;

    @FXML
    private Label customerLabel;

    @FXML
    private Label dateRangeLabel;

    @FXML
    private Label totalCostLabel;


    @FXML
    private Spinner<Integer> ratingSpinner;

    @FXML
    private TextArea reviewCommentArea;

    @FXML
    private Button completeButton;

    @FXML
    private Button cancelButton;

    private BookingDAO bookingDAO;
    private ReviewDAO reviewDAO;
    private VehicleDAO vehicleDAO;
    private Booking currentBooking;
    private Vehicle currentVehicle;

    public void initialize() {
        bookingDAO = new BookingDAO();
        reviewDAO = new ReviewDAO();
        vehicleDAO = new VehicleDAO();

        // Setup rating spinner (1-5 stars)
        SpinnerValueFactory<Integer> spinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5);
        ratingSpinner.setValueFactory(spinnerFactory);

    }

    public void setBookingId(int bookingId) {
        // Load booking details
        currentBooking = bookingDAO.getBookingById(bookingId);

        if (currentBooking != null) {
            // Load vehicle details
            currentVehicle = vehicleDAO.getVehicleById(currentBooking.getVehicleId());

            // Display booking information
            bookingIdLabel.setText("Booking #" + currentBooking.getBookingId());

            if (currentVehicle != null) {
                vehicleLabel.setText(currentVehicle.getMake() + " " + currentVehicle.getModel() + " ("
                        + currentVehicle.getYear() + ")");
            }

            customerLabel.setText("Customer ID: " + currentBooking.getCustomerId());
            dateRangeLabel.setText(currentBooking.getStartDate() + " to " + currentBooking.getEndDate());
            totalCostLabel.setText("₹" + String.format("%.2f", currentBooking.getTotalCost()));


        } else {
            showAlert("Error", "Booking not found", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleCompleteBooking() {
        if (currentBooking == null) {
            showAlert("Error", "No booking loaded", Alert.AlertType.ERROR);
            return;
        }

        String newVehicleStatus = "AVAILABLE";

        int rating = ratingSpinner.getValue();
        String comment = reviewCommentArea.getText().trim();

        if (comment.isEmpty()) {
            showAlert("Error", "Please provide a review comment", Alert.AlertType.ERROR);
            return;
        }

        if (comment.length() < 10) {
            showAlert("Error", "Review comment must be at least 10 characters", Alert.AlertType.ERROR);
            return;
        }

        // Complete the booking
        boolean bookingCompleted = bookingDAO.completeBooking(
                currentBooking.getBookingId(),
                currentBooking.getVehicleId(),
                newVehicleStatus);

        if (!bookingCompleted) {
            showAlert("Error", "Failed to complete booking", Alert.AlertType.ERROR);
            return;
        }

        // Add review
        Review review = new Review();
        review.setVehicleId(currentBooking.getVehicleId());
        review.setCustomerId(currentBooking.getCustomerId());
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDate.now());

        boolean reviewAdded = reviewDAO.addReview(review);

        if (reviewAdded) {
            showAlert("Success", "Booking completed successfully with review submitted!",
                    Alert.AlertType.INFORMATION);
            // Navigate back to Browse Vehicles (VehicleRental)
            SceneManager.switchScene("VehicleRental");
        } else {
            showAlert("Warning", "Booking completed but failed to save review. Please add review separately.",
                    Alert.AlertType.WARNING);
            SceneManager.switchScene("VehicleRental");
        }
    }

    @FXML
    public void handleCancel() {
        SceneManager.switchScene("MyBookings");
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
