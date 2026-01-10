package com.example.onride.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordViewController {

    @FXML
    private TextField emailField;

    @FXML
    void handleResetPasswordButtonAction(ActionEvent event) {
        String email = emailField.getText();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter your email");
            return;
        }

        // In a real application, you would send a password reset link to the user's email.
        // For this example, we'll just show a confirmation message.
        showAlert(Alert.AlertType.INFORMATION, "Password Reset", "A password reset link has been sent to your email address.");

        // Navigate back to the login view (prefer injecting into contentArea if available)
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/LoginView.fxml"));
            javafx.scene.Node contentArea = emailField.getScene().lookup("#contentArea");
            if (contentArea instanceof javafx.scene.layout.Pane) {
                ((javafx.scene.layout.Pane) contentArea).getChildren().setAll(root);
            } else {
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root, 1000, 600));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBackToLoginLinkAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/LoginView.fxml"));
            javafx.scene.Node contentArea = emailField.getScene().lookup("#contentArea");
            if (contentArea instanceof javafx.scene.layout.Pane) {
                ((javafx.scene.layout.Pane) contentArea).getChildren().setAll(root);
            } else {
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root, 1000, 600));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
