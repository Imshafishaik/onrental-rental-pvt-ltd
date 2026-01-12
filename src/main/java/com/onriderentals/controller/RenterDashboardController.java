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
    @FXML
    private TextField locationField;
    @FXML
    private TextField imageKeyField;

    @FXML
    private Label photoCountLabel;

    private VehicleDAO vehicleDAO;
    private com.onriderentals.dao.VehiclePhotoDAO photoDAO;
    private ObservableList<Vehicle> vehicleList;
    private java.util.List<com.onriderentals.model.VehiclePhoto> tempPhotos = new java.util.ArrayList<>();

    public void initialize() {
        vehicleDAO = new VehicleDAO();
        photoDAO = new com.onriderentals.dao.VehiclePhotoDAO();
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
            locationField.setText(vehicle.getLocation() != null ? vehicle.getLocation() : "");
            imageKeyField.setText(vehicle.getImageKey() != null ? vehicle.getImageKey() : "");
            
            // Load photos
            tempPhotos = photoDAO.getPhotosByVehicleId(vehicle.getVehicleId());
            photoCountLabel.setText(String.valueOf(tempPhotos.size()));
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
            
            // Save additional photos
            try {
                for (com.onriderentals.model.VehiclePhoto photo : tempPhotos) {
                    photo.setVehicleId(vehicle.getVehicleId());
                    photoDAO.addPhoto(photo);
                }
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

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
            
            // Update additional photos (simple sync: delete all and re-add)
            try {
                photoDAO.deleteAllPhotosForVehicle(selectedVehicle.getVehicleId());
                for (com.onriderentals.model.VehiclePhoto photo : tempPhotos) {
                    photo.setVehicleId(selectedVehicle.getVehicleId());
                    photoDAO.addPhoto(photo);
                }
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

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
        vehicle.setLocation(locationField.getText());
        vehicle.setImageKey(imageKeyField.getText());
    }
    
    private boolean validateInput() {
        if (makeField.getText().isEmpty() || modelField.getText().isEmpty() || yearField.getText().isEmpty() || 
            colorField.getText().isEmpty() || licensePlateField.getText().isEmpty() || vinField.getText().isEmpty() || 
            typeField.getText().isEmpty() || mileageField.getText().isEmpty() || pricePerDayField.getText().isEmpty() ||
            locationField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all vehicle details including location.");
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
        locationField.clear();
        imageKeyField.clear();
        tempPhotos.clear();
        photoCountLabel.setText("0");
        vehicleTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleManagePhotos() {
        // Choice dialog for Upload vs URL
        Alert choice = new Alert(Alert.AlertType.CONFIRMATION);
        choice.setTitle("Add Photo");
        choice.setHeaderText("How would you like to add a photo?");
        choice.setContentText("Choose an option:");

        ButtonType btnUpload = new ButtonType("Upload File");
        ButtonType btnUrl = new ButtonType("Enter URL/Key");
        ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        choice.getButtonTypes().setAll(btnUpload, btnUrl, btnCancel);

        Optional<ButtonType> option = choice.showAndWait();
        if (option.get() == btnUpload) {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            java.io.File file = fileChooser.showOpenDialog(photoCountLabel.getScene().getWindow());
            if (file != null) {
                String key = com.onriderentals.util.S3Service.uploadImage(file);
                if (key != null) {
                    tempPhotos.add(new com.onriderentals.model.VehiclePhoto(0, key));
                    photoCountLabel.setText(String.valueOf(tempPhotos.size()));
                } else {
                    showAlert(Alert.AlertType.ERROR, "Upload Failed", "Could not upload image to S3. Check your credentials.");
                }
            }
        } else if (option.get() == btnUrl) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Photo");
            dialog.setHeaderText("Enter Photo URL or S3 Key");
            dialog.setContentText("URL:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(url -> {
                if (!url.isEmpty()) {
                    tempPhotos.add(new com.onriderentals.model.VehiclePhoto(0, url));
                    photoCountLabel.setText(String.valueOf(tempPhotos.size()));
                }
            });
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
