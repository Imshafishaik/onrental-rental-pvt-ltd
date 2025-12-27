package com.example.onride.controller;

import com.example.onride.dao.BookingDAO;
import com.example.onride.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminViewController {

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label totalRidesLabel;

    private UserDAO userDAO = new UserDAO();
    private BookingDAO bookingDAO = new BookingDAO();

    @FXML
    public void initialize() {
        totalUsersLabel.setText(String.valueOf(userDAO.getTotalUsers()));
        totalRidesLabel.setText(String.valueOf(bookingDAO.getTotalBookings()));
    }
}
