package com.onriderentals.controller;

import com.onriderentals.dao.BookingDAO;
import com.onriderentals.dao.VehicleDAO;
import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.Booking;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleRentalController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableColumn<Vehicle, String> makeColumn;
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    @FXML
    private TableColumn<Vehicle, Integer> yearColumn;
    @FXML
    private TableColumn<Vehicle, String> typeColumn;
    @FXML
    private TableColumn<Vehicle, Double> rateColumn;
    @FXML
    private TableColumn<Vehicle, Void> detailsColumn;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    // My Bookings Table
    @FXML
    private TableView<Booking> bookingTable;
    @FXML
    private TableColumn<Booking, String> bookingMakeColumn;
    @FXML
    private TableColumn<Booking, String> bookingModelColumn;
    @FXML
    private TableColumn<Booking, LocalDate> bookingStartDateColumn;
    @FXML
    private TableColumn<Booking, LocalDate> bookingEndDateColumn;
    @FXML
    private TableColumn<Booking, Double> bookingCostColumn;
    @FXML
    private TableColumn<Booking, String> bookingStatusColumn;

    private VehicleDAO vehicleDAO;
    private BookingDAO bookingDAO;
    private ObservableList<Vehicle> vehicleList;
    private ObservableList<Booking> bookingList;

    public void initialize() {
        try {
            vehicleDAO = new VehicleDAO();
            bookingDAO = new BookingDAO();
            vehicleList = FXCollections.observableArrayList();
            bookingList = FXCollections.observableArrayList();

            // Setup vehicle table
            makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
            modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
            yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            rateColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));

            // Setup booking table
            bookingMakeColumn.setCellValueFactory(
                    cellData -> new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().getVehicle().getMake()));
            bookingModelColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getVehicle().getModel()));
            bookingStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            bookingEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            bookingCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
            bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

            addDetailsButtonToTable();
            loadVehicles();
            loadUserBookings();
        } catch (Exception e) {
            System.err.println("Error initializing VehicleRentalController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addDetailsButtonToTable() {
        Callback<TableColumn<Vehicle, Void>, TableCell<Vehicle, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Vehicle, Void> call(final TableColumn<Vehicle, Void> param) {
                final TableCell<Vehicle, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Details");

                    {
                        btn.setOnAction(event -> {
                            Vehicle vehicle = getTableView().getItems().get(getIndex());
                            showVehicleDetails(vehicle);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        detailsColumn.setCellFactory(cellFactory);
    }

    private void showVehicleDetails(Vehicle vehicle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/onriderentals/view/VehicleDetails.fxml"));
            AnchorPane page = loader.load();

            Stage detailsStage = new Stage();
            detailsStage.setTitle("Vehicle Details");
            detailsStage.initModality(Modality.WINDOW_MODAL);
            detailsStage.initOwner(vehicleTable.getScene().getWindow());

            Scene scene = new Scene(page);
            detailsStage.setScene(scene);

            VehicleDetailsController controller = loader.getController();
            controller.initData(vehicle);

            detailsStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadVehicles() {
        try {
            List<Vehicle> allVehicles = vehicleDAO.getAllVehicles();
            if (allVehicles == null) {
                System.out.println("No vehicles found");
                vehicleList.clear();
                return;
            }
            List<Vehicle> availableVehicles = allVehicles.stream()
                    .filter(vehicle -> "Available".equalsIgnoreCase(vehicle.getStatus()))
                    .collect(Collectors.toList());
            vehicleList.setAll(availableVehicles);
            vehicleTable.setItems(vehicleList);
            System.out.println("Loaded " + availableVehicles.size() + " vehicles");
        } catch (Exception e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
            e.printStackTrace();
            vehicleList.clear();
        }
    }

    private void loadUserBookings() {
        try {
            int customerId = SessionManager.getInstance().getUserId();
            if (customerId <= 0) {
                System.out.println("User not logged in");
                bookingList.clear();
                return;
            }
            List<Booking> bookings = bookingDAO.getBookingsByCustomerId(customerId);
            if (bookings != null) {
                bookingList.setAll(bookings);
                bookingTable.setItems(bookingList);
                System.out.println("Loaded " + bookings.size() + " bookings");
            }
        } catch (Exception e) {
            System.err.println("Error loading bookings: " + e.getMessage());
            e.printStackTrace();
            bookingList.clear();
        }
    }

    @FXML
    public void searchVehicles() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText == null || searchText.isEmpty()) {
            vehicleTable.setItems(vehicleList);
            return;
        }

        ObservableList<Vehicle> filteredList = vehicleList.stream()
                .filter(vehicle -> vehicle.getMake().toLowerCase().contains(searchText) ||
                        vehicle.getModel().toLowerCase().contains(searchText) ||
                        vehicle.getType().toLowerCase().contains(searchText))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        vehicleTable.setItems(filteredList);
    }

    @FXML
    public void bookVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (selectedVehicle == null || startDate == null || endDate == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a vehicle and booking dates.");
            return;
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid Dates", "End date must be after start date.");
            return;
        }

        double totalCost = selectedVehicle.getPricePerDay() * days;

        Booking booking = new Booking();
        booking.setCustomerId(SessionManager.getInstance().getUserId());
        booking.setVehicleId(selectedVehicle.getVehicleId());
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setTotalCost(totalCost);
        booking.setStatus("Booked");

        bookingDAO.addBooking(booking);

        showAlert(Alert.AlertType.INFORMATION, "Booking Successful", "Vehicle booked successfully!");

        loadVehicles();
        loadUserBookings(); // Refresh the bookings table
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleLoginButton() {
        SceneManager.loadScene("/com/onriderentals/view/Login.fxml");
    }

    @FXML
    public void handleRegisterButton() {
        SceneManager.loadScene("/com/onriderentals/view/Register.fxml");
    }

    // New navigation methods for modern UI
    @FXML
    public void handleHome() {
        SceneManager.switchScene("Home");
    }

    @FXML
    public void handleFavorites() {
        SceneManager.switchScene("Favorites");
    }

    @FXML
    public void handleBookings() {
        SceneManager.switchScene("MyBookings");
    }

    @FXML
    public void handleProfile() {
        System.out.println("Profile clicked");
    }

    @FXML
    public void handleLogout() {
        SessionManager.getInstance().clearSession();
        SceneManager.switchScene("Login");
    }

    @FXML
    public void handleApplyFilters() {
        System.out.println("Filters applied");
        loadVehicles();
    }

    @FXML
    public void handleClearFilters() {
        System.out.println("Filters cleared");
        loadVehicles();
    }
}
