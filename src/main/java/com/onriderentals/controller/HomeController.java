package com.onriderentals.controller;

import com.onriderentals.factory.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class HomeController {

    @FXML
    private Button navVehiclesBtn;
    @FXML
    private Button navLoginBtn;
    @FXML
    private Button navRegisterBtn;
    @FXML
    private TextField searchLocationField;
    @FXML
    private DatePicker pickupDatePicker;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private Button searchButton;
    @FXML
    private Button bikesButton;
    @FXML
    private Button carsButton;
    @FXML
    private Button ctaGetStartedBtn;
    @FXML
    private Button ctaListVehicleBtn;

    @FXML
    public void handleNavVehicles() {
        System.out.println("Navigating to Vehicles...");
        SceneManager.switchScene("VehicleRental");
    }

    @FXML
    public void handleLogin() {
        System.out.println("Navigating to Login...");
        SceneManager.switchScene("Login");
    }

    @FXML
    public void handleRegister() {
        System.out.println("Navigating to Register...");
        SceneManager.switchScene("Register");
    }

    @FXML
    public void handleSearch() {
        String location = searchLocationField.getText();
        if (location.isEmpty()) {
            showAlert("Please enter a location");
            return;
        }
        System.out.println("Searching for vehicles in: " + location);
        // Navigate to vehicle rental with filters
        SceneManager.switchScene("VehicleRental");
    }

    @FXML
    public void handleBikesClick() {
        System.out.println("Browsing Bikes...");
        // Filter for bikes only
        SceneManager.switchScene("VehicleRental");
    }

    @FXML
    public void handleCarsClick() {
        System.out.println("Browsing Cars...");
        // Filter for cars only
        SceneManager.switchScene("VehicleRental");
    }

    @FXML
    public void handleGetStarted() {
        System.out.println("Get Started - Navigating to Register...");
        SceneManager.switchScene("Register");
    }

    @FXML
    public void handleListVehicle() {
        System.out.println("Navigating to Renter Dashboard...");
        SceneManager.switchScene("RenterDashboard");
    }

    private void showAlert(String message) {
        System.out.println("Alert: " + message);
    }
}
