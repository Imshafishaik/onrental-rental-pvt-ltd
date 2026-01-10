package com.example.onride.controller;

import com.example.onride.model.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VehicleCardController {

    @FXML
    private ImageView vehicleImageView;

    @FXML
    private Label vehicleNameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Button bookButton;

    private Vehicle vehicle;

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        updateView();
    }

    private void updateView() {
        if (vehicle != null) {
            vehicleNameLabel.setText(vehicle.getBrand() + " " + vehicle.getModel());
            priceLabel.setText(String.format("$%.2f / day", vehicle.getPricePerDay()));

            if (vehicle.getImageUrl() != null && !vehicle.getImageUrl().isEmpty()) {
                try {
                    Image image = new Image(vehicle.getImageUrl());
                    vehicleImageView.setImage(image);
                } catch (Exception e) {
                    // Use a default image if the URL is invalid
                    Image defaultImage = new Image(getClass().getResourceAsStream("/images/default_vehicle.png"));
                    vehicleImageView.setImage(defaultImage);
                }
            } else {
                // Use a default image if no URL is provided
                Image defaultImage = new Image(getClass().getResourceAsStream("/images/default_vehicle.png"));
                vehicleImageView.setImage(defaultImage);
            }
        }
    }

    @FXML
    private void handleBookAction() {
        System.out.println("Book now for vehicle: " + vehicle.getVehicleId());
    }
}
