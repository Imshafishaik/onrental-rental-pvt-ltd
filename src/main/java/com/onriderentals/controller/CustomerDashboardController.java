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
import javafx.geometry.Pos;
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
    private javafx.scene.layout.FlowPane favoritesGrid;
    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    private ObservableList<Vehicle> vehicleList;

    // Stats Labels
    @FXML
    private Label totalBookingsLabel;
    @FXML
    private Label activeRentalsLabel;
    @FXML
    private Label rentalStatus;
    @FXML
    private Label loyaltyPointsLabel;
    @FXML
    private Label memberStatus;

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
    @FXML
    private TableColumn<Booking, Void> bookingActionColumn;

    private VehicleDAO vehicleDAO;
    private BookingDAO bookingDAO;
    private FavoriteDAO favoriteDAO;
    private ObservableList<Booking> bookingList;

    public void initialize() {
        vehicleDAO = new VehicleDAO();
        bookingDAO = new BookingDAO();
        favoriteDAO = new FavoriteDAO();
        bookingList = FXCollections.observableArrayList();
        vehicleList = FXCollections.observableArrayList();

        // Setup booking table
        bookingMakeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getVehicle().getMake()));
        bookingModelColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getVehicle().getModel()));
        bookingStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        bookingEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        bookingCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        addBookingActionColumn();
        loadUserBookings();
        loadFavorites();
        loadCustomerStats();
    }

    private void addBookingActionColumn() {
        Callback<TableColumn<Booking, Void>, TableCell<Booking, Void>> cellFactory = new Callback<TableColumn<Booking, Void>, TableCell<Booking, Void>>() {
            @Override
            public TableCell<Booking, Void> call(final TableColumn<Booking, Void> param) {
                final TableCell<Booking, Void> cell = new TableCell<Booking, Void>() {

                    private final Button completeBtn = new Button("Complete & Review");
                    private final Button viewBtn = new Button("View Details");

                    {
                        completeBtn.setStyle(
                                "-fx-padding: 8 15; -fx-font-size: 11; -fx-background-color: #28a745; -fx-text-fill: white; -fx-cursor: hand;");
                        viewBtn.setStyle(
                                "-fx-padding: 8 15; -fx-font-size: 11; -fx-background-color: #007bff; -fx-text-fill: white; -fx-cursor: hand;");

                        completeBtn.setOnAction(event -> {
                            Booking booking = getTableView().getItems().get(getIndex());
                            if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                                SceneManager.switchScene("CompleteBooking", booking.getBookingId());
                            } else {
                                showAlert(Alert.AlertType.INFORMATION, "Info",
                                        "Only CONFIRMED bookings can be completed.");
                            }
                        });

                        viewBtn.setOnAction(event -> {
                            Booking booking = getTableView().getItems().get(getIndex());
                            showAlert(Alert.AlertType.INFORMATION, "Booking #" + booking.getBookingId(),
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
                            if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                                setGraphic(completeBtn);
                            } else {
                                setGraphic(viewBtn);
                            }
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
                return cell;
            }
        };

        bookingActionColumn.setCellFactory(cellFactory);
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

        // loadVehicles();
        loadUserBookings(); // Refresh the bookings table
        loadCustomerStats(); // Refresh the stats cards
    }

    private void loadCustomerStats() {
        int customerId = SessionManager.getInstance().getUserId();
        
        // Get total bookings count
        List<Booking> bookings = bookingDAO.getBookingsByCustomerId(customerId);
        totalBookingsLabel.setText(String.valueOf(bookings.size()));
        
        // Get active rentals (confirmed bookings that are ongoing)
        long activeRentals = bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()) && 
                          !b.getEndDate().isBefore(java.time.LocalDate.now()))
                .count();
        activeRentalsLabel.setText(String.valueOf(activeRentals));
        
        if (activeRentals > 0) {
            rentalStatus.setText("Active rentals");
        } else {
            rentalStatus.setText("No active rentals");
        }
        
        // Calculate loyalty points (simplified: 10 points per booking)
        int loyaltyPoints = bookings.size() * 10;
        loyaltyPointsLabel.setText(String.valueOf(loyaltyPoints));
        
        // Determine member status
        if (loyaltyPoints >= 500) {
            memberStatus.setText("Gold Member");
        } else if (loyaltyPoints >= 200) {
            memberStatus.setText("Silver Member");
        } else {
            memberStatus.setText("Bronze Member");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}