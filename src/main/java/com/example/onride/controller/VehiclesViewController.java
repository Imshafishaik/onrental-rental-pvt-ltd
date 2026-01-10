package com.example.onride.controller;

import com.example.onride.dao.VehicleDAO;
import com.example.onride.model.Vehicle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class VehiclesViewController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filterCombo;

    @FXML
    private ComboBox<String> sortCombo;

    @FXML
    private GridPane vehicleGrid;

    private VehicleDAO vehicleDAO = new VehicleDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filterCombo.getItems().addAll("All", "BIKE", "CAR");
        filterCombo.setValue("All");

        sortCombo.getItems().addAll("Price: Low to High", "Price: High to Low");
        sortCombo.setValue("Price: Low to High");

        loadVehicles();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterAndSortVehicles());
        filterCombo.valueProperty().addListener((observable, oldValue, newValue) -> filterAndSortVehicles());
        sortCombo.valueProperty().addListener((observable, oldValue, newValue) -> filterAndSortVehicles());
    }

    private void loadVehicles() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        displayVehicles(vehicles);
    }

    private void filterAndSortVehicles() {
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();

        // Filter by search text
        String searchText = searchField.getText().toLowerCase();
        if (!searchText.isEmpty()) {
            vehicles = vehicles.stream()
                    .filter(v -> v.getBrand().toLowerCase().contains(searchText) || v.getModel().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
        }

        // Filter by type
        String filterType = filterCombo.getValue();
        if (!"All".equals(filterType)) {
            vehicles = vehicles.stream()
                    .filter(v -> v.getType().equalsIgnoreCase(filterType))
                    .collect(Collectors.toList());
        }

        // Sort by price
        String sortType = sortCombo.getValue();
        if ("Price: Low to High".equals(sortType)) {
            vehicles.sort(Comparator.comparing(Vehicle::getPricePerDay));
        } else if ("Price: High to Low".equals(sortType)) {
            vehicles.sort(Comparator.comparing(Vehicle::getPricePerDay).reversed());
        }

        displayVehicles(vehicles);
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        vehicleGrid.getChildren().clear();

        int row = 0;
        int col = 0;

        for (Vehicle vehicle : vehicles) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/onride/VehicleCard.fxml"));
                VBox vehicleCard = loader.load();
                VehicleCardController controller = loader.getController();
                controller.setVehicle(vehicle);

                vehicleGrid.add(vehicleCard, col, row);

                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleHostVehicleAction() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/HostVehicleView.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Host a New Vehicle");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadVehicles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
