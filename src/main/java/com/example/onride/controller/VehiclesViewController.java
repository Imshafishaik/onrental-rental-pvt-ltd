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
import java.util.ResourceBundle;

public class VehiclesViewController implements Initializable {

    @FXML
    private ListView<Vehicle> vehicleListView;

    private VehicleDAO vehicleDAO = new VehicleDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(vehicleDAO.getAllVehicles());
        vehicleListView.setItems(vehicles);

        vehicleListView.setCellFactory(new Callback<ListView<Vehicle>, ListCell<Vehicle>>() {
            @Override
            public ListCell<Vehicle> call(ListView<Vehicle> param) {
                return new VehicleListCell();
            }
        });
    }
}
