package com.onriderentals.controller;

import com.onriderentals.dao.FavoriteDAO;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.Vehicle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;

public class VehicleDetailsController {

    @FXML
    private Label vehicleMakeModelLabel;

    @FXML
    private Label vehicleDetailsLabel;
    
    @FXML
    private Label vehicleIdLabel;
    
    @FXML
    private Label typeLabel;
    
    @FXML
    private Label locationLabel;
    
    @FXML
    private Label priceLabel;
    
    @FXML
    private Label ratingLabel;

    @FXML
    private Label reviewCountLabel;

    @FXML
    private ImageView vehicleImageView;

    @FXML
    private Button bookButton;

    @FXML
    private Button favoriteButton;

    @FXML
    private javafx.scene.layout.HBox photoGallery;

    private Vehicle vehicle;
    private FavoriteDAO favoriteDAO = new FavoriteDAO();
    private com.onriderentals.dao.VehiclePhotoDAO photoDAO = new com.onriderentals.dao.VehiclePhotoDAO();
    private boolean isFavorited = false;

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        vehicleMakeModelLabel.setText(vehicle.getMake() + " " + vehicle.getModel());
        vehicleIdLabel.setText("#V-" + vehicle.getVehicleId());
        typeLabel.setText(vehicle.getType() != null ? vehicle.getType().toUpperCase() : "VEHICLE");
        locationLabel.setText("📍 " + (vehicle.getLocation() != null ? vehicle.getLocation() : "Unknown Location"));
        priceLabel.setText("$" + (int)vehicle.getPricePerDay());
        
        vehicleDetailsLabel.setText(
                "Year: " + vehicle.getYear() + "\n" +
                "Color: " + vehicle.getColor() + "\n" +
                "Mileage: " + vehicle.getMileage() + " km\n" +
                "License Plate: " + vehicle.getLicensePlate() + "\n" +
                "Status: " + vehicle.getStatus()
        );

        // Rating stats
        com.onriderentals.dao.ReviewDAO reviewDAO = new com.onriderentals.dao.ReviewDAO();
        com.onriderentals.dao.ReviewDAO.RatingStats stats = reviewDAO.getRatingStats(vehicle.getVehicleId());
        
        if (stats.reviewCount > 0) {
            ratingLabel.setText(String.format("%.1f", stats.averageRating));
            reviewCountLabel.setText("(" + stats.reviewCount + " reviews)");
        } else {
            ratingLabel.setText("N/A");
            reviewCountLabel.setText("(No reviews)");
        }

        // Image loading
        if (vehicle.getImageKey() != null && !vehicle.getImageKey().isEmpty()) {
            String imageUrl = com.onriderentals.util.S3Service.getImageUrl(vehicle.getImageKey());
            if (imageUrl != null) {
                vehicleImageView.setImage(new javafx.scene.image.Image(imageUrl, true));
            }
        } else {
            String type = vehicle.getType() != null ? vehicle.getType().toLowerCase() : "car";
            String placeholderUrl = type.contains("bike") 
                ? "https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&w=800&q=80"
                : "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?auto=format&fit=crop&w=800&q=80";
            vehicleImageView.setImage(new javafx.scene.image.Image(placeholderUrl, true));
        }

        // Load and display gallery photos
        loadGallery();

        checkFavoriteStatus();
    }

    private void loadGallery() {
        if (photoGallery == null || vehicle == null) return;
        photoGallery.getChildren().clear();

        java.util.List<com.onriderentals.model.VehiclePhoto> photos = photoDAO.getPhotosByVehicleId(vehicle.getVehicleId());
        
        // Add main image as the first thumbnail
        if (vehicle.getImageKey() != null && !vehicle.getImageKey().isEmpty()) {
            addThumbnail(com.onriderentals.util.S3Service.getImageUrl(vehicle.getImageKey()));
        }

        for (com.onriderentals.model.VehiclePhoto photo : photos) {
            addThumbnail(photo.getPhotoUrl());
        }
    }

    private void addThumbnail(String url) {
        if (url == null || url.isEmpty()) return;
        
        ImageView thumb = new ImageView();
        thumb.setFitHeight(60);
        thumb.setFitWidth(80);
        thumb.setPreserveRatio(true);
        thumb.setCursor(javafx.scene.Cursor.HAND);
        thumb.setStyle("-fx-border-color: #eee; -fx-border-width: 1; -fx-background-color: white;");

        javafx.scene.image.Image img = new javafx.scene.image.Image(url, true);
        thumb.setImage(img);

        thumb.setOnMouseClicked(e -> {
            vehicleImageView.setImage(img);
        });

        photoGallery.getChildren().add(thumb);
    }

    private void checkFavoriteStatus() {
        if (com.onriderentals.model.SessionManager.getInstance().isLoggedIn() && vehicle != null) {
            int userId = com.onriderentals.model.SessionManager.getInstance().getUserId();
            isFavorited = favoriteDAO.isFavorite(userId, vehicle.getVehicleId());
            updateFavoriteButton();
        }
    }

    private void updateFavoriteButton() {
        Platform.runLater(() -> {
            if (isFavorited) {
                favoriteButton.setText("♥");
                favoriteButton.setStyle("-fx-text-fill: #e63946; -fx-font-size: 24px; -fx-padding: 10;");
            } else {
                favoriteButton.setText("♡");
                favoriteButton.setStyle("-fx-text-fill: #adb5bd; -fx-font-size: 24px; -fx-padding: 10;");
            }
        });
    }

    @FXML
    private void handleFavorite() {
        if (!com.onriderentals.model.SessionManager.getInstance().isLoggedIn()) {
            com.onriderentals.factory.SceneManager.switchScene("Login");
            return;
        }

        int userId = com.onriderentals.model.SessionManager.getInstance().getUserId();
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

    @Deprecated
    public void initData(Vehicle vehicle) {
        setVehicle(vehicle);
    }

    @FXML
    void handleBackButtonAction(ActionEvent event) {
        try {
            com.onriderentals.factory.SceneManager.switchScene("VehicleRental");
        } catch (Exception e) {
            // Fallback for modal
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
    
    @FXML
    private void handleBook() {
        if (vehicle != null) {
            com.onriderentals.factory.SceneManager.switchScene("BookingConfirmation", vehicle);
        }
    }
}
