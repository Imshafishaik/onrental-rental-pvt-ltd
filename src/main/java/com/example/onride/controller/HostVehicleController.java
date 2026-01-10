package com.example.onride.controller;

import com.example.onride.dao.VehicleDAO;
import com.example.onride.model.SessionManager;
import com.example.onride.model.User;
import com.example.onride.model.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class HostVehicleController {

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField locationField;

    private VehicleDAO vehicleDAO = new VehicleDAO();

    @FXML
    public void initialize() {
        if (typeCombo != null) {
            typeCombo.getItems().addAll("BIKE", "CAR");
            typeCombo.getSelectionModel().selectFirst();
        }
    }

    @FXML
    void handleHostAction(ActionEvent event) {
        String type = (typeCombo != null) ? typeCombo.getValue() : "BIKE";
        String brand = (brandField != null) ? brandField.getText().trim() : "";
        String model = (modelField != null) ? modelField.getText().trim() : "";
        int year = 0;
        double price = 0.0;
        String location = (locationField != null) ? locationField.getText().trim() : "";

        try {
            if (yearField != null && !yearField.getText().trim().isEmpty()) {
                year = Integer.parseInt(yearField.getText().trim());
            }
            if (priceField != null && !priceField.getText().trim().isEmpty()) {
                price = Double.parseDouble(priceField.getText().trim());
            }
        } catch (NumberFormatException nfe) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Year and Price must be numeric");
            return;
        }

        if (brand.isEmpty() || model.isEmpty() || location.isEmpty() || price <= 0) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill required fields with valid values");
            return;
        }

        User current = SessionManager.getInstance().getCurrentUser();
        if (current == null) {
            showAlert(Alert.AlertType.ERROR, "Not signed in", "Please sign in as a renter to host a vehicle");
            return;
        }

        Vehicle v = new Vehicle();
        v.setRenterId(current.getId());
        v.setType(type);
        v.setBrand(brand);
        v.setModel(model);
        v.setYear(year);
        v.setPricePerDay(price);
        v.setLocation(location);
        v.setStatus("AVAILABLE");

        boolean ok = vehicleDAO.addVehicle(v);
        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle hosted successfully");
            // Load vehicles view into content area
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/VehiclesView.fxml"));
                javafx.scene.Node contentArea = brandField.getScene().lookup("#contentArea");
                if (contentArea instanceof javafx.scene.layout.Pane) {
                    ((javafx.scene.layout.Pane) contentArea).getChildren().setAll(root);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not host vehicle. Try again.");
        }
    }

    @FXML
    void handleCancelAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/VehiclesView.fxml"));
            javafx.scene.Node contentArea = brandField.getScene().lookup("#contentArea");
            if (contentArea instanceof javafx.scene.layout.Pane) {
                ((javafx.scene.layout.Pane) contentArea).getChildren().setAll(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String msg) {
        Alert a = new Alert(alertType);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
