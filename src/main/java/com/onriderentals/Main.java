package com.onriderentals;

import com.onriderentals.factory.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setPrimaryStage(primaryStage);
        primaryStage.setTitle("OnRide Rentals");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);
        SceneManager.loadScene("/com/onriderentals/view/Home.fxml");
        primaryStage.show();
    }
}
