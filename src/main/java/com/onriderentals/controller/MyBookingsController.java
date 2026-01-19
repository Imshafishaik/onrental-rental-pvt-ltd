package com.onriderentals.controller;

import com.onriderentals.dao.BookingDAO;
import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.Booking;
import com.onriderentals.model.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
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
    private TableColumn<Booking, Void> actionColumn;

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

        // Add action button column
        addActionButtonToTable();

        loadBookings();
    }

    private void addActionButtonToTable() {
        Callback<TableColumn<Booking, Void>, TableCell<Booking, Void>> cellFactory = new Callback<TableColumn<Booking, Void>, TableCell<Booking, Void>>() {
            @Override
            public TableCell<Booking, Void> call(final TableColumn<Booking, Void> param) {
                final TableCell<Booking, Void> cell = new TableCell<Booking, Void>() {

                    private final Button completeBtn = new Button("Complete");
                    private final Button viewDetailsBtn = new Button("View Details");

                    {
                        completeBtn.setStyle(
                                "-fx-padding: 8 15; -fx-font-size: 11; -fx-background-color: #28a745; -fx-text-fill: white; -fx-cursor: hand; -fx-border-radius: 5;");
                        viewDetailsBtn.setStyle(
                                "-fx-padding: 8 15; -fx-font-size: 11; -fx-background-color: #007bff; -fx-text-fill: white; -fx-cursor: hand; -fx-border-radius: 5; -fx-margin-right: 5;");

                        completeBtn.setOnAction(event -> {
                            Booking booking = getTableView().getItems().get(getIndex());

                            // Only allow completing CONFIRMED bookings
                            if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                                SceneManager.switchScene("CompleteBooking", booking.getBookingId());
                            } else {
                                showAlert("Info", "Only CONFIRMED bookings can be completed.");
                            }
                        });

                        viewDetailsBtn.setOnAction(event -> {
                            Booking booking = getTableView().getItems().get(getIndex());
                            // You can implement view details functionality here
                            showAlert("Booking #" + booking.getBookingId(),
                                    "Vehicle: " + booking.getVehicleId() + "\n" +
                                            "Status: " + booking.getStatus() + "\n" +
                                            "Start: " + booking.getStartDate() + "\n" +
                                            "End: " + booking.getEndDate() + "\n" +
                                            "Cost: ₹" + String.format("%.2f", booking.getTotalCost()));
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Booking booking = getTableView().getItems().get(getIndex());

                            // Only show Complete button for CONFIRMED bookings
                            if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                                setGraphic(completeBtn);
                            } else {
                                setGraphic(viewDetailsBtn);
                            }

                            setAlignment(Pos.CENTER);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void loadBookings() {
        if (SessionManager.getInstance().isLoggedIn()) {
            int userId = SessionManager.getInstance().getUserId();
            ObservableList<Booking> bookings = FXCollections.observableArrayList(bookingDAO.getBookingsByCustomerId(userId));
            bookingsTable.setItems(bookings);
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}