package com.example.onride.controller;

import com.example.onride.dao.BookingDAO;
import com.example.onride.dao.PaymentDAO;
import com.example.onride.dao.UserDAO;
import com.example.onride.dao.VehicleDAO;
import com.example.onride.model.Booking;
import com.example.onride.model.Payment;
import com.example.onride.model.User;
import com.example.onride.model.Vehicle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;

public class AdminViewController {

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label totalRidesLabel;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableView<Vehicle> vehiclesTable;

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableView<Payment> paymentsTable;

    private final UserDAO userDAO = new UserDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    @FXML
    public void initialize() {
        totalUsersLabel.setText(String.valueOf(userDAO.getTotalUsers()));
        totalRidesLabel.setText(String.valueOf(bookingDAO.getTotalBookings()));

        setupUserTable();
        setupVehicleTable();
        setupBookingTable();
        setupPaymentTable();

        loadUsers();
        loadVehicles();
        loadBookings();
        loadPayments();
    }

    private void setupUserTable() {
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<User, String> userTypeCol = new TableColumn<>("User Type");
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));

        usersTable.getColumns().addAll(idCol, nameCol, emailCol, phoneCol, userTypeCol);
    }

    private void setupVehicleTable() {
        TableColumn<Vehicle, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));

        TableColumn<Vehicle, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Vehicle, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Vehicle, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Vehicle, Double> priceCol = new TableColumn<>("Price/Day");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));

        TableColumn<Vehicle, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        vehiclesTable.getColumns().addAll(idCol, typeCol, brandCol, modelCol, yearCol, priceCol, statusCol);
    }

    private void setupBookingTable() {
        TableColumn<Booking, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

        TableColumn<Booking, Integer> vehicleIdCol = new TableColumn<>("Vehicle ID");
        vehicleIdCol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));

        TableColumn<Booking, Integer> customerIdCol = new TableColumn<>("Customer ID");
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        TableColumn<Booking, Date> startDateCol = new TableColumn<>("Start Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Booking, Date> endDateCol = new TableColumn<>("End Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Booking, Double> totalAmountCol = new TableColumn<>("Total Amount");
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        bookingsTable.getColumns().addAll(idCol, vehicleIdCol, customerIdCol, startDateCol, endDateCol, totalAmountCol, statusCol);
    }

    private void setupPaymentTable() {
        TableColumn<Payment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Payment, Integer> bookingIdCol = new TableColumn<>("Booking ID");
        bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

        TableColumn<Payment, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Payment, Date> paymentDateCol = new TableColumn<>("Payment Date");
        paymentDateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        TableColumn<Payment, String> paymentMethodCol = new TableColumn<>("Payment Method");
        paymentMethodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<Payment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        paymentsTable.getColumns().addAll(idCol, bookingIdCol, amountCol, paymentDateCol, paymentMethodCol, statusCol);
    }

    private void loadUsers() {
        List<User> users = userDAO.getAllUsers();
        usersTable.setItems(FXCollections.observableArrayList(users));
    }

    private void loadVehicles() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        vehiclesTable.setItems(FXCollections.observableArrayList(vehicles));
    }

    private void loadBookings() {
        List<Booking> bookings = bookingDAO.getAllBookings();
        bookingsTable.setItems(FXCollections.observableArrayList(bookings));
    }

    private void loadPayments() {
        List<Payment> payments = paymentDAO.getAllPayments();
        paymentsTable.setItems(FXCollections.observableArrayList(payments));
    }
}
