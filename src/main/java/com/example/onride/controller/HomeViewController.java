package com.example.onride.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeViewController {

    @FXML
    private Button loginButton;
    
    @FXML
    private Button signupButton;
    
    @FXML
    private ComboBox<String> vehicleTypeCombo;
    
    @FXML
    private TextField locationField;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private Button bikesButton;
    
    @FXML
    private Button carsButton;
    
    @FXML
    private Button scootersButton;

    @FXML
    public void initialize() {
        // Initialize vehicle type combo box
        vehicleTypeCombo.getItems().addAll("All", "Bikes", "Cars", "Scooters");
        vehicleTypeCombo.setValue("All");
    }

    @FXML
    void handleLoginButtonAction(ActionEvent event) {
        try {
            loadView("/com/example/onride/LoginView.fxml", "OnRide - Login");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load login page: " + e.getMessage());
        }
    }

    @FXML
    void handleSignupButtonAction(ActionEvent event) {
        try {
            loadView("/com/example/onride/SignUpView.fxml", "OnRide - Sign Up");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load sign up page: " + e.getMessage());
        }
    }

    @FXML
    void handleSearchButtonAction(ActionEvent event) {
        String vehicleType = vehicleTypeCombo.getValue();
        String location = locationField.getText();
        
        if (location == null || location.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Search Warning", "Please enter a location to search.");
            return;
        }
        
        try {
            // Load vehicles view with search parameters
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/onride/VehiclesView.fxml"));
            Parent root = loader.load();
            
            // Get the controller and pass search parameters
            VehiclesViewController controller = loader.getController();
            controller.setSearchParameters(vehicleType, location.trim());
            
            Stage stage = (Stage) searchButton.getScene().getWindow();
            stage.setTitle("OnRide - Available Vehicles");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load vehicles page: " + e.getMessage());
        }
    }

    @FXML
    void handleBikesButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/onride/VehiclesView.fxml"));
            Parent root = loader.load();
            
            VehiclesViewController controller = loader.getController();
            controller.setSearchParameters("Bikes", "");
            
            Stage stage = (Stage) bikesButton.getScene().getWindow();
            stage.setTitle("OnRide - Bikes");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load bikes page: " + e.getMessage());
        }
    }

    @FXML
    void handleCarsButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/onride/VehiclesView.fxml"));
            Parent root = loader.load();
            
            VehiclesViewController controller = loader.getController();
            controller.setSearchParameters("Cars", "");
            
            Stage stage = (Stage) carsButton.getScene().getWindow();
            stage.setTitle("OnRide - Cars");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load cars page: " + e.getMessage());
        }
    }

    @FXML
    void handleScootersButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/onride/VehiclesView.fxml"));
            Parent root = loader.load();
            
            VehiclesViewController controller = loader.getController();
            controller.setSearchParameters("Scooters", "");
            
            Stage stage = (Stage) scootersButton.getScene().getWindow();
            stage.setTitle("OnRide - Scooters");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load scooters page: " + e.getMessage());
        }
    }

    private void loadView(String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
