package com.onriderentals.controller;

import com.onriderentals.model.Booking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

public class MyBookingsController {

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;

    @FXML
    private TableColumn<Booking, Integer> vehicleColumn;

    @FXML
    private TableColumn<Booking, LocalDate> startDateColumn;

    @FXML
    private TableColumn<Booking, LocalDate> endDateColumn;

    @FXML
    private TableColumn<Booking, Double> totalCostColumn;

    @FXML
    private TableColumn<Booking, String> statusColumn;

    @FXML
    public void initialize() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // This is a placeholder for fetching real data.
        bookingsTable.setItems(getDummyBookings());
    }

    private ObservableList<Booking> getDummyBookings() {
        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        
        Booking booking1 = new Booking();
        booking1.setBookingId(1);
        booking1.setCustomerId(1);
        booking1.setVehicleId(101);
        booking1.setStartDate(LocalDate.of(2024, 7, 1));
        booking1.setEndDate(LocalDate.of(2024, 7, 5));
        booking1.setTotalCost(500.0);
        booking1.setStatus("Confirmed");

        Booking booking2 = new Booking();
        booking2.setBookingId(2);
        booking2.setCustomerId(1);
        booking2.setVehicleId(102);
        booking2.setStartDate(LocalDate.of(2024, 8, 15));
        booking2.setEndDate(LocalDate.of(2024, 8, 20));
        booking2.setTotalCost(750.0);
        booking2.setStatus("Completed");

        bookings.add(booking1);
        bookings.add(booking2);

        return bookings;
    }
}
