package com.example.onride.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HomeViewController {

    // These should match the fx:id in your new FXML
    @FXML private TextField locationField;
    @FXML private DatePicker pickUpDate;
    
    @FXML
    private Button loginButton;

    @FXML private ImageView heroImage;
    @FXML private StackPane heroStack;

    @FXML
    public void initialize() {
        // Fits the background image to the window width
        heroImage.fitWidthProperty().bind(heroStack.widthProperty());
        heroImage.setFitHeight(750);
        heroImage.setPreserveRatio(false);
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
    void handleVehiclesButtonAction(ActionEvent event) {
        try {
            loadView("/com/example/onride/VehiclesView.fxml", "OnRide - Vehicles");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load vehicles page: " + e.getMessage());
        }
    }

    // @FXML
    // void handleSignupButtonAction(ActionEvent event) {
    //     try {
    //         loadView("/com/example/onride/SignUpView.fxml", "OnRide - Sign Up");
    //     } catch (IOException e) {
    //         showAlert(Alert.AlertType.ERROR, "Error", "Could not load sign up page: " + e.getMessage());
    //     }
    // }

    @FXML
    void handleSearchButtonAction(ActionEvent event) {
        // Example logic for the new search bar
        String location = (locationField != null) ? locationField.getText() : "";
        
        if (location.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Search Warning", "Please enter a location.");
            return;
        }
        
        loadVehiclesView("All", location);
    }

    // Helper method to handle navigation
    private void loadVehiclesView(String type, String location) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/onride/VehiclesView.fxml"));
            Parent root = loader.load();
            
            // VehiclesViewController controller = loader.getController();
            // controller.setSearchParameters(type, location);
            
            Stage stage = (Stage) locationField.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "View not found.");
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
        alert.setContentText(message);
        alert.showAndWait();
    }
}