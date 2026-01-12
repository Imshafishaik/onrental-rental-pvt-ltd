package com.onriderentals.controller;

import com.onriderentals.dao.FavoriteDAO;
import com.onriderentals.dao.BookingDAO;
import com.onriderentals.dao.VehicleDAO;
import com.onriderentals.model.Booking;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.Vehicle;
import com.onriderentals.factory.SceneManager;
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

public class CustomerDashboardController {

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
    @FXML
    private javafx.scene.layout.FlowPane favoritesGrid;

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
    private FavoriteDAO favoriteDAO;
    private ObservableList<Vehicle> vehicleList;
    private ObservableList<Booking> bookingList;

    public void initialize() {
        vehicleDAO = new VehicleDAO();
        bookingDAO = new BookingDAO();
        favoriteDAO = new FavoriteDAO();
        vehicleList = FXCollections.observableArrayList();
        bookingList = FXCollections.observableArrayList();

        // Setup vehicle table
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));

        // Setup booking table
        bookingMakeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getVehicle().getMake()));
        bookingModelColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getVehicle().getModel()));
        bookingStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        bookingEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        bookingCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        addDetailsButtonToTable();
        loadVehicles();
        loadUserBookings();
        loadFavorites();
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
        SceneManager.switchScene("VehicleDetails", vehicle);
    }

    private void loadVehicles() {
        List<Vehicle> allVehicles = vehicleDAO.getAllVehicles();
        List<Vehicle> availableVehicles = allVehicles.stream()
                .filter(vehicle -> "Available".equalsIgnoreCase(vehicle.getStatus()))
                .collect(Collectors.toList());
        vehicleList.setAll(availableVehicles);
        vehicleTable.setItems(vehicleList);
    }

    private void loadUserBookings() {
        int customerId = SessionManager.getInstance().getUserId();
        bookingList.setAll(bookingDAO.getBookingsByCustomerId(customerId));
        bookingTable.setItems(bookingList);
    }

    private void loadFavorites() {
        favoritesGrid.getChildren().clear();
        if (!SessionManager.getInstance().isLoggedIn()) return;

        int userId = SessionManager.getInstance().getUserId();
        List<Integer> favIds = favoriteDAO.getFavoriteVehicleIds(userId);
        
        for (int vehicleId : favIds) {
            Vehicle v = vehicleDAO.getVehicleById(vehicleId);
            if (v != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/onriderentals/view/VehicleCard.fxml"));
                    javafx.scene.Parent card = loader.load();
                    VehicleCardController controller = loader.getController();
                    controller.setVehicle(v);
                    favoritesGrid.getChildren().add(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
                        vehicle.getModel().toLowerCase().contains( searchText) ||
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
        booking.setStatus("CONFIRMED");

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
}
