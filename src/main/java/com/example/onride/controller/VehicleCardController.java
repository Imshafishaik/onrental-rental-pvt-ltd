package com.example.onride.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class VehicleCardController {

    @FXML private ImageView vehicleImage;
    @FXML private Label title;
    @FXML private Label subtitle;
    @FXML private Label location;
    @FXML private Label price;
    @FXML private Label typeBadge;

    public void setData(String t, String sub, String loc, String p, String type, String imgPath) {
        title.setText(t);
        subtitle.setText(sub);
        location.setText(loc);
        price.setText(p);
        typeBadge.setText(type);
        vehicleImage.setImage(new Image(imgPath));
    }
}

