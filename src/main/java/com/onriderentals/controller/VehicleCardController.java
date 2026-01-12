package com.onriderentals.controller;

import com.onriderentals.dao.FavoriteDAO;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.Vehicle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.onriderentals.factory.SceneManager;

public class VehicleCardController {

    @FXML private ImageView vehicleImage;
    @FXML private Label vehicleName;
    @FXML private Label vehicleModel;
    @FXML private Label locationLabel;
    @FXML private Label typeBadge;
    @FXML private Label ratingLabel;
    @FXML private Label reviewCountLabel;
    @FXML private Label priceLabel;
    @FXML private Button bookButton;
    @FXML private Button favoriteButton;

    private Vehicle vehicle;
    private FavoriteDAO favoriteDAO = new FavoriteDAO();
    private boolean isFavorited = false;

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        
        vehicleName.setText(vehicle.getMake()); // Using make as title for now
        vehicleModel.setText(vehicle.getModel() + " - " + vehicle.getYear());
        typeBadge.setText(vehicle.getType());
        priceLabel.setText("$" + String.format("%.0f", vehicle.getPricePerDay()));
        
        // Real data from model
        locationLabel.setText(vehicle.getLocation() != null ? vehicle.getLocation() : "Unknown"); 
        ratingLabel.setText("4.5"); // Keep rating mock as it's not in DB yet
        reviewCountLabel.setText("(120 reviews)");

        // Image loading logic (S3 integration)
        try {
            if (vehicle.getImageKey() != null && !vehicle.getImageKey().isEmpty()) {
                String imageUrl = com.onriderentals.util.S3Service.getImageUrl(vehicle.getImageKey());
                if (imageUrl != null) {
                    vehicleImage.setImage(new Image(imageUrl, true)); // Load in background
                }
            } else {
                // High-quality dummy image from Unsplash
                String type = vehicle.getType() != null ? vehicle.getType().toLowerCase() : "car";
                String imageUrl = type.contains("bike") 
                    ? "https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=400&q=80"
                    : "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?auto=format&fit=crop&w=400&q=80";
                
                vehicleImage.setImage(new Image(imageUrl, true));
            }
        } catch (Exception e) {
            System.err.println("Could not load image for vehicle: " + vehicle.getMake());
        }

        checkFavoriteStatus();
    }

    private void checkFavoriteStatus() {
        if (SessionManager.getInstance().isLoggedIn()) {
            int userId = SessionManager.getInstance().getUserId();
            isFavorited = favoriteDAO.isFavorite(userId, vehicle.getVehicleId());
            updateFavoriteButton();
        }
    }

    private void updateFavoriteButton() {
        Platform.runLater(() -> {
            if (isFavorited) {
                favoriteButton.setText("♥");
                favoriteButton.setStyle("-fx-text-fill: #e63946; -fx-font-size: 18px;");
            } else {
                favoriteButton.setText("♡");
                favoriteButton.setStyle("-fx-text-fill: #adb5bd; -fx-font-size: 18px;");
            }
        });
    }

    @FXML
    private void handleFavorite() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            SceneManager.switchScene("Login");
            return;
        }

        int userId = SessionManager.getInstance().getUserId();
        try {
            if (isFavorited) {
                favoriteDAO.removeFavorite(userId, vehicle.getVehicleId());
                isFavorited = false;
            } else {
                favoriteDAO.addFavorite(userId, vehicle.getVehicleId());
                isFavorited = true;
            }
            updateFavoriteButton();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleBook() {
        if (vehicle != null) {
            SceneManager.switchScene("BookingConfirmation", vehicle);
        }
    }

    @FXML
    private void handleViewDetails() {
        if (vehicle != null) {
            SceneManager.switchScene("VehicleDetails", vehicle);
        }
    }
}
