package com.example.onride.controller;

import com.example.onride.model.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {

    private Pane contentArea;

    public void setContentArea(Pane contentArea) {
        this.contentArea = contentArea;
    }

    @FXML
    void navigateToDashboard(ActionEvent event) {
        loadView("/com/example/onride/DashboardView.fxml");
    }

    @FXML
    void navigateToVehicles(ActionEvent event) {
        loadView("/com/example/onride/VehiclesView.fxml");
    }

    @FXML
    void navigateToBookings(ActionEvent event) {
        loadView("/com/example/onride/MyBookingsView.fxml");
    }

    @FXML
    void navigateToHostVehicle(ActionEvent event) {
        loadView("/com/example/onride/HostVehicleView.fxml");
    }

    @FXML
    void navigateToHelp(ActionEvent event) {
        loadView("/com/example/onride/HelpView.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        SessionManager.getInstance().setCurrentUser(null);
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/example/onride/LoginView.fxml"));
            Scene scene = new Scene(loginView);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
