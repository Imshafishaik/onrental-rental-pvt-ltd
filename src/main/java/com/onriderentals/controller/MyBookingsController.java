package com.onriderentals.controller;

import com.onriderentals.dao.BookingDAO;
import com.onriderentals.model.Booking;
import com.onriderentals.model.SessionManager;
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

    private BookingDAO bookingDAO;

    @FXML
    public void initialize() {
        bookingDAO = new BookingDAO();
        
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadBookings();
    }

    private void loadBookings() {
        if (SessionManager.getInstance().isLoggedIn()) {
            int userId = SessionManager.getInstance().getUserId();
            ObservableList<Booking> bookings = FXCollections.observableArrayList(bookingDAO.getBookingsByCustomerId(userId));
            bookingsTable.setItems(bookings);
        }
    }
}
