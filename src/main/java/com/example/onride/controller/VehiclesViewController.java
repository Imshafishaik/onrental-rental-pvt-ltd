package com.example.onride.controller;

import com.example.onride.dao.VehicleDAO;
import com.example.onride.model.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VehiclesViewController implements Initializable {

    @FXML
    private ListView<Vehicle> vehicleListView;

    private VehicleDAO vehicleDAO = new VehicleDAO();
    private String searchType = "";
    private String searchLocation = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadVehicles();
        
        vehicleListView.setCellFactory(new Callback<ListView<Vehicle>, ListCell<Vehicle>>() {
            @Override
            public ListCell<Vehicle> call(ListView<Vehicle> param) {
                return new VehicleListCell();
            }
        });
    }

    public void setSearchParameters(String type, String location) {
        this.searchType = type != null ? type : "";
        this.searchLocation = location != null ? location : "";
        loadVehicles();
    }

    private void loadVehicles() {
        List<Vehicle> vehicles;
        
        try {
            if (searchType.isEmpty() && searchLocation.isEmpty()) {
                // Load all vehicles
                vehicles = vehicleDAO.getAllVehicles();
            } else {
                // Filter vehicles based on search parameters
                vehicles = vehicleDAO.searchVehicles(searchType, searchLocation);
            }
            
            // If no vehicles found or database error, add sample data
            if (vehicles.isEmpty()) {
                vehicles = getSampleVehicles();
            }
            
        } catch (Exception e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
            // Load sample data as fallback
            vehicles = getSampleVehicles();
        }
        
        vehicleListView.setItems(FXCollections.observableArrayList(vehicles));
    }
    
    private List<Vehicle> getSampleVehicles() {
        List<Vehicle> sampleVehicles = new ArrayList<>();
        
        // Add sample vehicles for demonstration
        Vehicle bike1 = new Vehicle();
        bike1.setVehicleId(1);
        bike1.setBrand("Yamaha");
        bike1.setModel("MT-07");
        bike1.setType("Bike");
        bike1.setYear(2023);
        bike1.setPricePerDay(45.00);
        bike1.setLocation("Downtown");
        bike1.setStatus("AVAILABLE");
        sampleVehicles.add(bike1);
        
        Vehicle car1 = new Vehicle();
        car1.setVehicleId(2);
        car1.setBrand("Toyota");
        car1.setModel("Camry");
        car1.setType("Car");
        car1.setYear(2023);
        car1.setPricePerDay(65.00);
        car1.setLocation("Airport");
        car1.setStatus("AVAILABLE");
        sampleVehicles.add(car1);
        
        Vehicle scooter1 = new Vehicle();
        scooter1.setVehicleId(3);
        scooter1.setBrand("Honda");
        scooter1.setModel("Activa");
        scooter1.setType("Scooter");
        scooter1.setYear(2023);
        scooter1.setPricePerDay(25.00);
        scooter1.setLocation("City Center");
        scooter1.setStatus("AVAILABLE");
        sampleVehicles.add(scooter1);
        
        return sampleVehicles;
    }
}
