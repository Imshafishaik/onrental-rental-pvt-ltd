package com.example.onride.controller;

import java.io.IOException;

import com.example.onride.dao.UserDAO;
import com.example.onride.model.SessionManager;
import com.example.onride.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpViewController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button loginButton;

    private UserDAO userDAO = new UserDAO();

    

    @FXML
    void handleSignInButtonAction(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter your email and password");
            return;
        }

        User user = userDAO.login(email, password);

        if (user != null) {
            com.example.onride.model.SessionManager.getInstance().setCurrentUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Login Successful!", "Welcome " + user.getName());
            try {
                javafx.scene.Node contentArea = emailField.getScene().lookup("#contentArea");
                if (contentArea instanceof javafx.scene.layout.Pane) {
                    Parent home = FXMLLoader.load(getClass().getResource("/com/example/onride/HomeView.fxml"));
                    ((javafx.scene.layout.Pane) contentArea).getChildren().setAll(home);
                } else {
                    Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/MainView.fxml"));
                    Stage stage = (Stage) emailField.getScene().getWindow();
                    stage.setScene(new Scene(root, 1000, 600));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed!", "Invalid email or password");
        }
    }

    @FXML
    void handleSignUpButtonAction(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all the fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Passwords do not match");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        if (userDAO.signUp(user, password)) {
            User newUser = userDAO.login(email, password);
                if (newUser != null) {
                SessionManager.getInstance().setCurrentUser(newUser);
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful!", "Welcome " + newUser.getName());
                try {
                    javafx.scene.Node contentArea = nameField.getScene().lookup("#contentArea");
                    if (contentArea instanceof javafx.scene.layout.Pane) {
                        Parent home = FXMLLoader.load(getClass().getResource("/com/example/onride/HomeView.fxml"));
                        ((javafx.scene.layout.Pane) contentArea).getChildren().setAll(home);
                    } else {
                        Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/MainView.fxml"));
                        Stage stage = (Stage) nameField.getScene().getWindow();
                        stage.setScene(new Scene(root));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed!", "An unexpected error occurred.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed!", "Email already exists");
        }
    }

    @FXML
    void handleSignInLinkAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/LoginView.fxml"));
            javafx.scene.Node contentArea = nameField.getScene().lookup("#contentArea");
            if (contentArea instanceof javafx.scene.layout.Pane) {
                ((javafx.scene.layout.Pane) contentArea).getChildren().setAll(root);
            } else {
                Stage stage = (Stage) nameField.getScene().getWindow();
                stage.setScene(new Scene(root));
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

    private void loadView(String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
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
    void handleSignUpLinkAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/SignUpView.fxml"));
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
    void handleHomeButtonAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/HomeView.fxml"));
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
}
