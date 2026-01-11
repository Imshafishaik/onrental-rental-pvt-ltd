package com.onriderentals.controller;

import com.onriderentals.dao.VehicleDAO;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class RenterDashboardController {

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
    private TableColumn<Vehicle, String> licensePlateColumn;
    @FXML
    private TableColumn<Vehicle, Double> pricePerDayColumn;
    @FXML
    private TableColumn<Vehicle, String> statusColumn;

    @FXML
    private TextField makeField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField colorField;
    @FXML
    private TextField licensePlateField;
    @FXML
    private TextField vinField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField mileageField;
    @FXML
    private TextField pricePerDayField;

    private VehicleDAO vehicleDAO;
    private ObservableList<Vehicle> vehicleList;

    public void initialize() {
        vehicleDAO = new VehicleDAO();
        vehicleList = FXCollections.observableArrayList();

        // Setup vehicle table columns
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        licensePlateColumn.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        pricePerDayColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Listen for selection changes and show the vehicle details in the form.
        vehicleTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> populateVehicleForm(newValue));

        loadRenterVehicles();
    }

    private void populateVehicleForm(Vehicle vehicle) {
        if (vehicle != null) {
            makeField.setText(vehicle.getMake());
            modelField.setText(vehicle.getModel());
            yearField.setText(String.valueOf(vehicle.getYear()));
            colorField.setText(vehicle.getColor());
            licensePlateField.setText(vehicle.getLicensePlate());
            vinField.setText(vehicle.getVin());
            typeField.setText(vehicle.getType());
            mileageField.setText(String.valueOf(vehicle.getMileage()));
            pricePerDayField.setText(String.valueOf(vehicle.getPricePerDay()));
        } else {
            clearForm();
        }
    }

    private void loadRenterVehicles() {
        int renterId = SessionManager.getInstance().getUserId();
        vehicleList.setAll(vehicleDAO.getVehiclesByRenterId(renterId));
        vehicleTable.setItems(vehicleList);
    }

    @FXML
    public void addVehicle() {
        if (!validateInput()) {
            return;
        }

        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setRenterId(SessionManager.getInstance().getUserId());
            setVehicleFromFields(vehicle);
            vehicle.setStatus("AVAILABLE"); // New vehicles are available by default

            vehicleDAO.addVehicle(vehicle);

            showAlert(Alert.AlertType.INFORMATION, "Vehicle Added", "New vehicle added successfully!");

            loadRenterVehicles();
            clearForm();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for year, mileage, and price per day.");
        }
    }

    @FXML
    public void updateVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert(Alert.AlertType.ERROR, "No Vehicle Selected", "Please select a vehicle to update.");
            return;
        }

        if (!validateInput()) {
            return;
        }

        try {
            setVehicleFromFields(selectedVehicle);

            vehicleDAO.updateVehicle(selectedVehicle);

            showAlert(Alert.AlertType.INFORMATION, "Vehicle Updated", "Vehicle details updated successfully!");

            loadRenterVehicles();
            clearForm();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for year, mileage, and price per day.");
        }
    }

    @FXML
    public void deleteVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert(Alert.AlertType.ERROR, "No Vehicle Selected", "Please select a vehicle to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this vehicle?");
        confirmationAlert.setContentText(selectedVehicle.getMake() + " " + selectedVehicle.getModel());

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            vehicleDAO.deleteVehicle(selectedVehicle.getVehicleId());
            showAlert(Alert.AlertType.INFORMATION, "Vehicle Deleted", "The vehicle has been deleted.");
            loadRenterVehicles();
            clearForm();
        }
    }
    
    private void setVehicleFromFields(Vehicle vehicle) throws NumberFormatException {
        vehicle.setMake(makeField.getText());
        vehicle.setModel(modelField.getText());
        vehicle.setYear(Integer.parseInt(yearField.getText()));
        vehicle.setColor(colorField.getText());
        vehicle.setLicensePlate(licensePlateField.getText());
        vehicle.setVin(vinField.getText());
        vehicle.setType(typeField.getText());
        vehicle.setMileage(Double.parseDouble(mileageField.getText()));
        vehicle.setPricePerDay(Double.parseDouble(pricePerDayField.getText()));
    }
    
    private boolean validateInput() {
        if (makeField.getText().isEmpty() || modelField.getText().isEmpty() || yearField.getText().isEmpty() || 
            colorField.getText().isEmpty() || licensePlateField.getText().isEmpty() || vinField.getText().isEmpty() || 
            typeField.getText().isEmpty() || mileageField.getText().isEmpty() || pricePerDayField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all vehicle details.");
            return false;
        }
        return true;
    }

    @FXML
    public void clearForm() {
        makeField.clear();
        modelField.clear();
        yearField.clear();
        colorField.clear();
        licensePlateField.clear();
        vinField.clear();
        typeField.clear();
        mileageField.clear();
        pricePerDayField.clear();
        vehicleTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
