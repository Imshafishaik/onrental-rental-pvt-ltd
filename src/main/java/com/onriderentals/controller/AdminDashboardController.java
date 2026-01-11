package com.onriderentals.controller;

import com.onriderentals.dao.BookingDAO;
import com.onriderentals.dao.UserDAO;
import com.onriderentals.dao.VehicleDAO;
import com.onriderentals.model.Booking;
import com.onriderentals.model.User;
import com.onriderentals.model.Vehicle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.Month;
import java.util.Map;

public class AdminDashboardController {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> userTypeColumn;
    @FXML
    private TableColumn<User, Void> userActionsColumn;

    @FXML
    private TableView<Booking> bookingTable;
    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;
    @FXML
    private TableColumn<Booking, String> customerNameColumn;
    @FXML
    private TableColumn<Booking, String> vehicleInfoColumn;
    @FXML
    private TableColumn<Booking, String> bookingDatesColumn;
    @FXML
    private TableColumn<Booking, Double> bookingCostColumn;
    @FXML
    private TableColumn<Booking, String> bookingStatusColumn;

    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableColumn<Vehicle, Integer> vehicleIdColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleMakeColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleModelColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML
    private TableColumn<Vehicle, Double> vehiclePriceColumn;
    @FXML
    private TableColumn<Vehicle, String> vehicleStatusColumn;

    @FXML
    private BarChart<String, Number> earningsChart;

    private UserDAO userDAO;
    private BookingDAO bookingDAO;
    private VehicleDAO vehicleDAO;
    private ObservableList<User> userList;
    private ObservableList<Booking> bookingList;
    private ObservableList<Vehicle> vehicleList;

    public void initialize() {
        userDAO = new UserDAO();
        bookingDAO = new BookingDAO();
        vehicleDAO = new VehicleDAO();
        userList = FXCollections.observableArrayList();
        bookingList = FXCollections.observableArrayList();
        vehicleList = FXCollections.observableArrayList();

        setupUserTable();
        setupBookingTable();
        setupVehicleTable();
        loadUsers();
        loadBookings();
        loadVehicles();
        loadEarningsChart();
    }

    private void setupUserTable() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));
        addUserActionsButtonToTable();
    }

    private void setupBookingTable() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        customerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getUsername()));
        vehicleInfoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVehicle().getMake() + " " + cellData.getValue().getVehicle().getModel()));
        bookingDatesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDate().toString() + " to " + cellData.getValue().getEndDate().toString()));
        bookingCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupVehicleTable() {
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        vehicleMakeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        vehicleStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadUsers() {
        userList.setAll(userDAO.getAllUsers());
        userTable.setItems(userList);
    }

    private void loadBookings() {
        bookingList.setAll(bookingDAO.getAllBookings());
        bookingTable.setItems(bookingList);
    }

    private void loadVehicles() {
        vehicleList.setAll(vehicleDAO.getAllVehicles());
        vehicleTable.setItems(vehicleList);
    }

    private void loadEarningsChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Earnings");

        Map<Month, Double> earningsByMonth = bookingDAO.getEarningsByMonth();

        for (Map.Entry<Month, Double> entry : earningsByMonth.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        earningsChart.getData().add(series);
    }

    private void addUserActionsButtonToTable() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            deleteUser(user);
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
        userActionsColumn.setCellFactory(cellFactory);
    }

    private void deleteUser(User user) {
        userDAO.deleteUser(user.getUserId());
        loadUsers(); // Refresh the user table
    }
}
