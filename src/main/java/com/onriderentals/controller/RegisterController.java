package com.onriderentals.controller;

import com.onriderentals.dao.UserDAO;
import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ChoiceBox<String> userTypeChoiceBox;

    private UserDAO userDAO;

    public void initialize() {
        System.out.println("RegisterController initialized");
        userDAO = new UserDAO();
        userTypeChoiceBox.setItems(FXCollections.observableArrayList("Customer", "Renter"));
        userTypeChoiceBox.setValue("Customer"); // Default role
    }

    @FXML
    private void register() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password_hash = passwordField.getText();
        String role = userTypeChoiceBox.getValue();
        String dbRole;
        if ("Renter".equals(role)) {
            dbRole = "-2";
        } else {
            dbRole = "-3"; // Customer
        }

        if (username.isEmpty() || email.isEmpty() || password_hash.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all fields.");
            return;
        }

        if (userDAO.getUserByEmail(email) != null) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "An account with this email already exists.");
            return;
        }

        User newUser = new User();
        newUser.setName(username);
        newUser.setEmail(email);
        newUser.setPassword(com.onriderentals.util.PasswordUtils.hashPassword(password_hash));
        newUser.setRole(dbRole);

        try {
            userDAO.addUser(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "You have successfully registered. Please log in.");
            backToLogin();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Database error: " + e.getMessage());
        }
    }

    @FXML
    private void backToLogin() {
        try {
            SceneManager.switchScene("Login");
        } catch (Exception e) {
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
