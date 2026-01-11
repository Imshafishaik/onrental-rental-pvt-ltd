package com.onriderentals.controller;

import com.onriderentals.model.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class FavoritesController {

    @FXML
    private ListView<Vehicle> favoritesList;

    @FXML
    public void initialize() {
        // This is a placeholder for fetching real data.
        favoritesList.setItems(getDummyFavorites());

        favoritesList.setCellFactory(param -> new ListCell<Vehicle>() {
            @Override
            protected void updateItem(Vehicle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMake() + " " + item.getModel());
                }
            }
        });
    }

    private ObservableList<Vehicle> getDummyFavorites() {
        ObservableList<Vehicle> favorites = FXCollections.observableArrayList();

        Vehicle vehicle1 = new Vehicle();
        vehicle1.setVehicleId(101);
        vehicle1.setMake("Honda");
        vehicle1.setModel("CBR1000RR");

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setVehicleId(103);
        vehicle2.setMake("Yamaha");
        vehicle2.setModel("YZF-R1");

        favorites.add(vehicle1);
        favorites.add(vehicle2);

        return favorites;
    }
}
