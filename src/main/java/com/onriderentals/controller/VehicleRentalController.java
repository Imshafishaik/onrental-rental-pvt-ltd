package com.onriderentals.controller;

import com.onriderentals.dao.VehicleDAO;
import com.onriderentals.factory.SceneManager;
import com.onriderentals.model.SessionManager;
import com.onriderentals.model.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleRentalController {

    @FXML
    private FlowPane vehicleGrid;
    @FXML
    private ComboBox<String> locationCombo;
    @FXML
    private ComboBox<String> sortCombo;
    @FXML
    private Slider priceSlider;
    @FXML
    private Label priceValueLabel;
    @FXML
    private DatePicker pickupDatePicker;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private Label resultsCountLabel;
    @FXML
    private Button filterAllBtn;
    @FXML
    private Button filterBikeBtn;
    @FXML
    private Button filterCarBtn;

    @FXML
    private HBox paginationBox;

    private VehicleDAO vehicleDAO;
    private ObservableList<Vehicle> allVehicles;
    private List<Vehicle> currentFilteredVehicles;
    private String currentTypeFilter = "All";
    private int itemsPerPage = 6;
    private int currentPage = 0;

    private String initialLocation;

    public void initialize() {
        vehicleDAO = new VehicleDAO();
        allVehicles = FXCollections.observableArrayList();

        setupFilters();
        loadVehicles();
        
        // Initial price slider state
        priceSlider.setValue(500.0); // Default max for premium rides
        priceValueLabel.setText("$500");
        
        if (initialLocation != null) {
            locationCombo.setValue(initialLocation);
            filterVehicles();
        }
    }

    public void setInitialLocation(String location) {
        this.initialLocation = location;
        if (locationCombo != null) {
            if (!locationCombo.getItems().contains(location)) {
                locationCombo.getItems().add(location);
            }
            locationCombo.setValue(location);
            filterVehicles();
        }
    }

    private void setupFilters() {
        // Initialize Filter Controls
        // Initialize Filter Controls
        List<String> locations = vehicleDAO.getUniqueLocations();
        ObservableList<String> locationItems = FXCollections.observableArrayList("All Locations");
        locationItems.addAll(locations);
        locationCombo.setItems(locationItems);
        locationCombo.setValue("All Locations");

        sortCombo.setItems(FXCollections.observableArrayList("Recommended", "Price: Low to High", "Price: High to Low", "Rating"));
        sortCombo.setValue("Recommended");

        // Slider Listener
        priceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            priceValueLabel.setText("$" + String.format("%.0f", newVal));
            filterVehicles();
        });
        
        // Combo Listeners
        locationCombo.valueProperty().addListener((obs, oldVal, newVal) -> filterVehicles());
        sortCombo.valueProperty().addListener((obs, oldVal, newVal) -> sortVehicles());
    }

    private void loadVehicles() {
        try {
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            if (vehicles != null) {
                // Filter only available vehicles initially
                 List<Vehicle> available = vehicles.stream()
                    .filter(v -> "Available".equalsIgnoreCase(v.getStatus()))
                    .collect(Collectors.toList());
                allVehicles.setAll(available);
                currentFilteredVehicles = allVehicles;
                renderVehicles();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Show error alert
        }
    }

    private void renderVehicles() {
        vehicleGrid.getChildren().clear();
        
        if (currentFilteredVehicles == null || currentFilteredVehicles.isEmpty()) {
            resultsCountLabel.setText("Showing 0 vehicles");
            Label noResultsLabel = new Label("No vehicles found matching your criteria.");
            noResultsLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #adb5bd; -fx-padding: 50 0 0 0;");
            vehicleGrid.getChildren().add(noResultsLabel);
            updatePagination();
            return;
        }

        int totalItems = currentFilteredVehicles.size();
        resultsCountLabel.setText("Showing " + totalItems + " vehicles");

        int fromIndex = currentPage * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
        
        List<Vehicle> pageItems = currentFilteredVehicles.subList(fromIndex, toIndex);

        for (Vehicle vehicle : pageItems) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/onriderentals/view/VehicleCard.fxml"));
                Parent card = loader.load();
                
                VehicleCardController controller = loader.getController();
                controller.setVehicle(vehicle);
                
                vehicleGrid.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        updatePagination();
    }

    private void updatePagination() {
        paginationBox.getChildren().clear();
        if (currentFilteredVehicles == null || currentFilteredVehicles.size() <= itemsPerPage) {
            return;
        }

        int totalPages = (int) Math.ceil((double) currentFilteredVehicles.size() / itemsPerPage);

        // Previous button
        Button prevBtn = new Button("←");
        prevBtn.getStyleClass().add("button-icon");
        prevBtn.setDisable(currentPage == 0);
        prevBtn.setOnAction(e -> {
            currentPage--;
            renderVehicles();
        });
        paginationBox.getChildren().add(prevBtn);

        // Page numbers
        for (int i = 0; i < totalPages; i++) {
            final int pageIdx = i;
            Button pageBtn = new Button(String.valueOf(i + 1));
            pageBtn.setMinWidth(45);
            pageBtn.setMinHeight(45);
            if (i == currentPage) {
                pageBtn.getStyleClass().add("button-primary");
            } else {
                pageBtn.getStyleClass().add("button-ghost");
            }
            pageBtn.setOnAction(e -> {
                currentPage = pageIdx;
                renderVehicles();
            });
            paginationBox.getChildren().add(pageBtn);
        }

        // Next button
        Button nextBtn = new Button("→");
        nextBtn.getStyleClass().add("button-icon");
        nextBtn.setDisable(currentPage == totalPages - 1);
        nextBtn.setOnAction(e -> {
            currentPage++;
            renderVehicles();
        });
        paginationBox.getChildren().add(nextBtn);
    }

    @FXML
    private void filterByType(javafx.event.ActionEvent event) {
        Button clickedBtn = (Button) event.getSource();
        
        // Reset styles
        filterAllBtn.getStyleClass().remove("selected");
        filterBikeBtn.getStyleClass().remove("selected");
        filterCarBtn.getStyleClass().remove("selected");
        
        // Set selected
        clickedBtn.getStyleClass().add("selected");
        
        if (clickedBtn == filterAllBtn) currentTypeFilter = "All";
        else if (clickedBtn == filterBikeBtn) currentTypeFilter = "Bike";
        else if (clickedBtn == filterCarBtn) currentTypeFilter = "Car";
        
        filterVehicles();
    }

    private void filterVehicles() {
        double maxPrice = priceSlider.getValue();
        String location = locationCombo.getValue();
        
        List<Vehicle> filtered = allVehicles.stream()
                .filter(v -> v.getPricePerDay() <= maxPrice)
                .filter(v -> {
                    if (currentTypeFilter.equals("All")) return true;
                    return v.getType().equalsIgnoreCase(currentTypeFilter);
                })
                .filter(v -> {
                    if (location == null || location.equals("All Locations")) return true;
                    return location.equalsIgnoreCase(v.getLocation());
                })
                .collect(Collectors.toList());
        
        currentFilteredVehicles = filtered;
        sortVehiclesList(currentFilteredVehicles);
        currentPage = 0; // Reset to first page on filter change
        renderVehicles();
    }

    private void sortVehiclesList(List<Vehicle> vehicles) {
        String sortOrder = sortCombo.getValue();
        if (sortOrder == null) return;

        switch (sortOrder) {
            case "Price: Low to High":
                vehicles.sort((v1, v2) -> Double.compare(v1.getPricePerDay(), v2.getPricePerDay()));
                break;
            case "Price: High to Low":
                vehicles.sort((v1, v2) -> Double.compare(v2.getPricePerDay(), v1.getPricePerDay()));
                break;
            case "Rating":
                // Assuming default rating for now as it's not in DB
                break;
            default: // Recommended
                break;
        }
    }
    
    private void sortVehicles() {
        // Implement sorting logic based on sortCombo.getValue()
        // For now just re-rendering
        filterVehicles();
    }

    @FXML
    private void handleResetFilters() {
        priceSlider.setValue(priceSlider.getMax());
        locationCombo.setValue("All Locations");
        currentTypeFilter = "All";
        
        filterAllBtn.getStyleClass().add("selected");
        filterBikeBtn.getStyleClass().remove("selected");
        filterCarBtn.getStyleClass().remove("selected");
        
        sortCombo.setValue("Recommended");
        
        filterVehicles();
    }

    // Navigation Methods
    @FXML
    public void handleHome() {
        // Already on home/browse
    }

    @FXML
    public void handleBookings() {
         try {
            SceneManager.switchScene("MyBookings"); // Assuming MyBookings view exists or will be created
        } catch (Exception e) {
            System.err.println("Navigation error: " + e.getMessage());
        }
    }

    @FXML
    public void handleLogout() {
        SessionManager.getInstance().clearSession();
        try {
            SceneManager.switchScene("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


