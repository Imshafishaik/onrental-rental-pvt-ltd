package com.example.onride.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class VehiclesViewController {

    @FXML
    private FlowPane vehicleGrid;

    @FXML
    public void initialize() {
        loadVehicles();
    }

    private void loadVehicles() {
    try {
        for (int i = 0; i < 6; i++) {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/VehicleCard.fxml"));

            VBox card = loader.load();

            VehicleCardController controller = loader.getController();
            controller.setData(
                    "Sport Cruiser X1",
                    "Kawasaki Ninja 2024",
                    "📍 New York",
                    "$45 / day",
                    "Bike",
                    getClass().getResource("/images/bike.png").toExternalForm()
            );

            vehicleGrid.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


// public class VehiclesViewController implements Initializable {

//     @FXML
//     private ListView<Vehicle> vehicleListView;

//     @FXML
//     private FlowPane vehicleGrid;

//     private VehicleDAO vehicleDAO = new VehicleDAO();
//     private String searchType = "";
//     private String searchLocation = "";

//     @FXML
//     private TextField searchLocationField;

//     @FXML
//     private DatePicker searchDatePicker;

//     @FXML
//     private ComboBox<String> typeComboBox;

//     @FXML
//     private TextField minPriceField;

//     @FXML
//     private TextField maxPriceField;

//     @FXML
//     private CheckBox availableOnlyCheck;

//     @FXML
//     private Button hostVehicleButton;

//     @Override
//     public void initialize(URL location, ResourceBundle resources) {
//         // populate type combo
//         if (typeComboBox != null) {
//             typeComboBox.setItems(FXCollections.observableArrayList("All", "BIKE", "CAR"));
//             typeComboBox.getSelectionModel().selectFirst();
//         }

//         loadVehicles();
        
//         if (vehicleListView != null) {
//             vehicleListView.setCellFactory(new Callback<ListView<Vehicle>, ListCell<Vehicle>>() {
//                 @Override
//                 public ListCell<Vehicle> call(ListView<Vehicle> param) {
//                     return new VehicleListCell();
//                 }
//             });
//         }
//     }

//     @FXML
//     void handleSearchButtonAction(ActionEvent event) {
//         String loc = (searchLocationField != null) ? searchLocationField.getText() : "";
//         String type = (typeComboBox != null && typeComboBox.getValue() != null) ? typeComboBox.getValue() : "All";

//         setSearchParameters(type, loc);
//     }

//     @FXML
//     void handleHostVehicleAction(ActionEvent event) {
//         try {
//             Parent root = FXMLLoader.load(getClass().getResource("/com/example/onride/HostVehicleView.fxml"));
//             javafx.scene.Node contentArea = vehicleListView.getScene().lookup("#contentArea");
//             if (contentArea instanceof Pane) {
//                 ((Pane) contentArea).getChildren().setAll(root);
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     public void setSearchParameters(String type, String location) {
//         this.searchType = type != null ? type : "";
//         this.searchLocation = location != null ? location : "";
//         loadVehicles();
//     }

//     private void loadVehicles() {
//         List<Vehicle> vehicles;
        
//         try {
//             // read filter values
//             String type = (typeComboBox != null && typeComboBox.getValue() != null) ? typeComboBox.getValue() : (searchType.isEmpty() ? "All" : searchType);
//             String location = (searchLocationField != null) ? searchLocationField.getText() : searchLocation;

//             Double minPrice = null;
//             Double maxPrice = null;
//             try {
//                 if (minPriceField != null && !minPriceField.getText().trim().isEmpty()) {
//                     minPrice = Double.parseDouble(minPriceField.getText().trim());
//                 }
//                 if (maxPriceField != null && !maxPriceField.getText().trim().isEmpty()) {
//                     maxPrice = Double.parseDouble(maxPriceField.getText().trim());
//                 }
//             } catch (NumberFormatException nfe) {
//                 // ignore and leave null
//             }

//             String status = null;
//             if (availableOnlyCheck != null && availableOnlyCheck.isSelected()) {
//                 status = "AVAILABLE";
//             }

//             // Filter vehicles based on search parameters
//             vehicles = vehicleDAO.searchVehicles(type, location, minPrice, maxPrice, status);
            
//             // If no vehicles found or database error, add sample data
//             if (vehicles.isEmpty()) {
//                 vehicles = getSampleVehicles();
//             }
            
//         } catch (Exception e) {
//             System.err.println("Error loading vehicles: " + e.getMessage());
//             // Load sample data as fallback
//             vehicles = getSampleVehicles();
//         }
        
//         vehicleListView.setItems(FXCollections.observableArrayList(vehicles));
//     }
    
//     private List<Vehicle> getSampleVehicles() {
//         List<Vehicle> sampleVehicles = new ArrayList<>();
        
//         // Add sample vehicles for demonstration
//         Vehicle bike1 = new Vehicle();
//         bike1.setVehicleId(1);
//         bike1.setBrand("Yamaha");
//         bike1.setModel("MT-07");
//         bike1.setType("Bike");
//         bike1.setYear(2023);
//         bike1.setPricePerDay(45.00);
//         bike1.setLocation("Downtown");
//         bike1.setStatus("AVAILABLE");
//         sampleVehicles.add(bike1);
        
//         Vehicle car1 = new Vehicle();
//         car1.setVehicleId(2);
//         car1.setBrand("Toyota");
//         car1.setModel("Camry");
//         car1.setType("Car");
//         car1.setYear(2023);
//         car1.setPricePerDay(65.00);
//         car1.setLocation("Airport");
//         car1.setStatus("AVAILABLE");
//         sampleVehicles.add(car1);
        
//         Vehicle scooter1 = new Vehicle();
//         scooter1.setVehicleId(3);
//         scooter1.setBrand("Honda");
//         scooter1.setModel("Activa");
//         scooter1.setType("Scooter");
//         scooter1.setYear(2023);
//         scooter1.setPricePerDay(25.00);
//         scooter1.setLocation("City Center");
//         scooter1.setStatus("AVAILABLE");
//         sampleVehicles.add(scooter1);
        
//         return sampleVehicles;
//     }
// }
