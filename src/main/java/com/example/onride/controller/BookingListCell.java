package com.example.onride.controller;

import com.example.onride.dao.BookingDAO;
import com.example.onride.dao.VehicleDAO;
import com.example.onride.model.Booking;
import com.example.onride.model.Vehicle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class BookingListCell extends ListCell<Booking> {

    @FXML
    private HBox hBox;

    @FXML
    private Label vehicleNameLabel;

    @FXML
    private Label bookingDatesLabel;

    @FXML
    private Label totalCostLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button cancelBookingButton;

    private FXMLLoader mLLoader;

    private BookingDAO bookingDAO;
    private ListView<Booking> bookingListView;
    private VehicleDAO vehicleDAO = new VehicleDAO();

    public BookingListCell(BookingDAO bookingDAO, ListView<Booking> bookingListView) {
        this.bookingDAO = bookingDAO;
        this.bookingListView = bookingListView;
    }

    @Override
    protected void updateItem(Booking booking, boolean empty) {
        super.updateItem(booking, empty);

        if (empty || booking == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/com/example/onride/BookingListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Vehicle vehicle = vehicleDAO.getVehicleById(booking.getVehicleId());
            if (vehicle != null) {
                vehicleNameLabel.setText(vehicle.getBrand() + " " + vehicle.getModel());
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            String bookingDates = String.format("%s - %s",
                    dateFormat.format(booking.getStartDate()),
                    dateFormat.format(booking.getEndDate()));
            bookingDatesLabel.setText(bookingDates);

            totalCostLabel.setText(String.format("Total Cost: $%.2f", booking.getTotalAmount()));
            statusLabel.setText("Status: " + booking.getStatus());

            if ("CANCELLED".equalsIgnoreCase(booking.getStatus()) || "CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                cancelBookingButton.setDisable(true);
            }

            cancelBookingButton.setOnAction(event -> {
                bookingDAO.deleteBooking(booking.getBookingId());
                bookingListView.getItems().remove(booking);
            });

            setText(null);
            setGraphic(hBox);
        }
    }
}
