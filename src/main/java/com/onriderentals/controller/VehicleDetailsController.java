package com.onriderentals.controller;

import com.onriderentals.model.Vehicle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class VehicleDetailsController {

    @FXML
    private Label vehicleMakeModelLabel;

    @FXML
    private Label vehicleDetailsLabel;

    @FXML
    private Button backButton;

    private Vehicle vehicle;

    public void initData(Vehicle vehicle) {
        this.vehicle = vehicle;
        vehicleMakeModelLabel.setText(vehicle.getMake() + " " + vehicle.getModel());
        vehicleDetailsLabel.setText(
                "Year: " + vehicle.getYear() + "\n" +
                "Color: " + vehicle.getColor() + "\n" +
                "License Plate: " + vehicle.getLicensePlate() + "\n" +
                "VIN: " + vehicle.getVin() + "\n" +
                "Type: " + vehicle.getType() + "\n" +
                "Mileage: " + vehicle.getMileage() + "\n" +
                "Price Per Day: " + vehicle.getPricePerDay()
        );
    }

    @FXML
    void handleBackButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
