package com.example.onride.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class HelpViewController {

    @FXML
    private void handleContactSupportAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contact Support");
        alert.setHeaderText("Support Channel Opened");
        alert.setContentText("Our support team will get back to you within 24 hours. Please check your email for a confirmation.");
        alert.showAndWait();
    }
}
