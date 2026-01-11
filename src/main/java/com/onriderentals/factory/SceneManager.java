package com.onriderentals.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private static Stage primaryStage;
    private static Map<String, String> sceneMap = new HashMap<>();
    private static Map<String, Scene> cachedScenes = new HashMap<>();

    static {
        // Register all scenes
        sceneMap.put("Home", "/com/onriderentals/view/Home.fxml");
        sceneMap.put("VehicleRental", "/com/onriderentals/view/VehicleRental.fxml");
        sceneMap.put("Login", "/com/onriderentals/view/Login.fxml");
        sceneMap.put("Register", "/com/onriderentals/view/Register.fxml");
        sceneMap.put("CustomerDashboard", "/com/onriderentals/view/CustomerDashboard.fxml");
        sceneMap.put("RenterDashboard", "/com/onriderentals/view/RenterDashboard.fxml");
        sceneMap.put("AdminDashboard", "/com/onriderentals/view/AdminDashboard.fxml");
        sceneMap.put("VehicleDetails", "/com/onriderentals/view/VehicleDetails.fxml");
        sceneMap.put("MyBookings", "/com/onriderentals/view/MyBookings.fxml");
        sceneMap.put("Favorites", "/com/onriderentals/view/Favorites.fxml");
        sceneMap.put("Reviews", "/com/onriderentals/view/Reviews.fxml");
        sceneMap.put("Notifications", "/com/onriderentals/view/Notifications.fxml");
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Error loading scene: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void switchScene(String sceneName) {
        String fxmlPath = sceneMap.getOrDefault(sceneName, "/com/onriderentals/view/Home.fxml");
        if (sceneMap.containsKey(sceneName)) {
            loadScene(fxmlPath);
        } else {
            System.err.println("Scene not found: " + sceneName);
        }
    }

    public static Scene getScene(String fxmlPath) throws IOException {
        if (!cachedScenes.containsKey(fxmlPath)) {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            cachedScenes.put(fxmlPath, new Scene(root));
        }
        return cachedScenes.get(fxmlPath);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
