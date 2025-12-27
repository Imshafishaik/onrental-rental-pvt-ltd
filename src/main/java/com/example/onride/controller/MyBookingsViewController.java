package com.example.onride.controller;

import com.example.onride.dao.BookingDAO;
import com.example.onride.model.Booking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class MyBookingsViewController implements Initializable {

    @FXML
    private ListView<Booking> bookingListView;

    private BookingDAO bookingDAO = new BookingDAO();

    // This should be replaced with the actual logged-in user's ID
    private int customerId = 3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Booking> bookings = FXCollections.observableArrayList(bookingDAO.getBookingsByCustomerId(customerId));
        bookingListView.setItems(bookings);

        bookingListView.setCellFactory(new Callback<ListView<Booking>, ListCell<Booking>>() {
            @Override
            public ListCell<Booking> call(ListView<Booking> param) {
                return new BookingListCell(bookingDAO, bookingListView);
            }
        });
    }
}
