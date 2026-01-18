package com.onriderentals.controller;

import com.onriderentals.dao.UserDAO;
import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.SessionManager;
import com.onriderentals.util.OtpManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.concurrent.atomic.AtomicBoolean;

public class VerifyOtpController {

    @FXML
    private TextField otpField;

    @FXML
    private Label timerLabel;

    @FXML
    private Button verifyButton;

    @FXML
    private Button backButton;

    private UserDAO userDAO;
    private String userEmail;
    private Thread timerThread;
    private AtomicBoolean isVerificationInProgress = new AtomicBoolean(false);
    private AtomicBoolean hasOtpExpired = new AtomicBoolean(false);

    public void initialize() {
        userDAO = new UserDAO();
        userEmail = SessionManager.getInstance().getCurrentUserEmail();

        if (userEmail == null) {
            showAlert("Error", "Invalid session. Please try again.", Alert.AlertType.ERROR);
            SceneManager.switchScene("ForgotPassword");
            return;
        }

        startOtpTimer();
    }

    @FXML
    public void handleVerifyOtp() {
        // Prevent multiple simultaneous verifications
        if (isVerificationInProgress.getAndSet(true)) {
            return;
        }

        // Check if OTP has expired
        if (hasOtpExpired.get()) {
            showAlert("Error", "OTP has expired. Please request a new one.", Alert.AlertType.ERROR);
            isVerificationInProgress.set(false);
            return;
        }

        String otp = otpField.getText().trim();

        if (otp.isEmpty()) {
            showAlert("Error", "Please enter the OTP", Alert.AlertType.ERROR);
            isVerificationInProgress.set(false);
            return;
        }

        if (!otp.matches("\\d{6}")) {
            showAlert("Error", "OTP must be a 6-digit number", Alert.AlertType.ERROR);
            isVerificationInProgress.set(false);
            return;
        }

        // Verify OTP
        if (OtpManager.verifyOtp(userEmail, otp)) {
            showAlert("Success", "OTP verified successfully. Please set a new password.",
                    Alert.AlertType.INFORMATION);
            otpField.clear();
            SceneManager.switchScene("ResetPassword");
        } else {
            showAlert("Error", "Invalid or expired OTP. Please try again or request a new one.", Alert.AlertType.ERROR);
            otpField.clear();
            isVerificationInProgress.set(false);
        }
    }

    @FXML
    public void handleBackToForgotPassword() {
        if (timerThread != null) {
            timerThread.interrupt();
        }
        OtpManager.clearOtp(userEmail);
        SceneManager.switchScene("ForgotPassword");
    }

    private void startOtpTimer() {
        timerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                int remainingSeconds = OtpManager.getOtpRemainingSeconds(userEmail);

                if (remainingSeconds <= 0) {
                    // Mark OTP as expired to prevent verification
                    hasOtpExpired.set(true);

                    javafx.application.Platform.runLater(() -> {
                        timerLabel.setText("OTP Expired");
                        timerLabel.setStyle("-fx-text-fill: #dc3545;");
                        verifyButton.setDisable(true);
                        otpField.setDisable(true);

                        // Only show alert and navigate if verification is not in progress
                        if (!isVerificationInProgress.get()) {
                            showAlert("Error", "OTP has expired. Please request a new one.", Alert.AlertType.ERROR);
                            handleBackToForgotPassword();
                        }
                    });
                    break;
                }

                int minutes = remainingSeconds / 60;
                int seconds = remainingSeconds % 60;
                String timeString = String.format("Valid for %d:%02d", minutes, seconds);

                javafx.application.Platform.runLater(() -> {
                    timerLabel.setText(timeString);
                    if (remainingSeconds <= 60) {
                        timerLabel.setStyle("-fx-text-fill: #ff6b6b;");
                    } else {
                        timerLabel.setStyle("-fx-text-fill: #28a745;");
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
