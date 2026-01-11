package com.onriderentals.controller;

import com.onriderentals.model.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;

public class VehicleDetailsController {

    @FXML
    private Label vehicleMakeModelLabel;

    @FXML
    private Label vehicleDetailsLabel;
    
    @FXML
    private Label vehicleIdLabel;
    
    @FXML
    private Label typeLabel;
    
    @FXML
    private Label locationLabel;
    
    @FXML
    private Label priceLabel;
    
    @FXML
    private ImageView vehicleImageView;

    @FXML
    private Button backButton;

    private Vehicle vehicle;

    public void initData(Vehicle vehicle) {
        this.vehicle = vehicle;
        vehicleMakeModelLabel.setText(vehicle.getMake() + " " + vehicle.getModel());
        vehicleIdLabel.setText("#V-" + vehicle.getVehicleId());
        typeLabel.setText(vehicle.getType() != null ? vehicle.getType().toUpperCase() : "VEHICLE");
        locationLabel.setText("📍 " + (vehicle.getLocation() != null ? vehicle.getLocation() : "Unknown Location"));
        priceLabel.setText("$" + (int)vehicle.getPricePerDay());
        
        vehicleDetailsLabel.setText(
                "Year: " + vehicle.getYear() + "\n" +
                "Color: " + vehicle.getColor() + "\n" +
                "Mileage: " + vehicle.getMileage() + " km\n" +
                "License Plate: " + vehicle.getLicensePlate() + "\n" +
                "Status: " + vehicle.getStatus()
        );
        
        // Note: In a real app, we would load the image from an URL or file path
        // vehicleImageView.setImage(new Image(vehicle.getImagePath()));
    }

    @FXML
    void handleBackButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
