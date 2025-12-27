package com.example.onride.controller;

import com.example.onride.model.Vehicle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class VehicleListCell extends ListCell<Vehicle> {

    @FXML
    private HBox hBox;

    @FXML
    private ImageView vehicleImageView;

    @FXML
    private Label vehicleNameLabel;

    @FXML
    private Label vehicleTypeLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label availabilityLabel;

    @FXML
    private Button bookNowButton;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Vehicle vehicle, boolean empty) {
        super.updateItem(vehicle, empty);

        if (empty || vehicle == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/com/example/onride/VehicleListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (vehicleNameLabel != null) {
                vehicleNameLabel.setText(vehicle.getBrand() + " " + vehicle.getModel());
            }
            if (vehicleTypeLabel != null) {
                vehicleTypeLabel.setText(vehicle.getType());
            }
            if (priceLabel != null) {
                priceLabel.setText(String.format("$%.2f/day", vehicle.getPricePerDay()));
            }
            if (availabilityLabel != null) {
                availabilityLabel.setText(vehicle.getStatus());
            }
            if (bookNowButton != null) {
                bookNowButton.setDisable(!vehicle.getStatus().equalsIgnoreCase("AVAILABLE"));
            }

            if (bookNowButton != null) {
                bookNowButton.setOnAction(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/onride/BookingDialog.fxml"));
                        Parent parent = loader.load();

                        BookingDialogController controller = loader.getController();
                        controller.setVehicle(vehicle);
                        // This should be replaced with the actual logged-in user's ID
                        controller.setUserId(1);

                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setTitle("Book Vehicle");
                        stage.setScene(new Scene(parent));
                        stage.showAndWait();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            // Since we don't have a direct image URL in the new schema, we can remove this for now.
            // vehicleImageView.setImage(new Image(vehicle.getImageUrl()));

            setText(null);
            if (hBox != null) {
                setGraphic(hBox);
            }
        }
    }
}
