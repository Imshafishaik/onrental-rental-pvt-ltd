package com.onriderentals.controller;

import com.onriderentals.model.Review;
import com.onriderentals.model.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ReviewsController {

    @FXML
    private TableView<Review> reviewsTable;

    @FXML
    private TableColumn<Review, String> vehicleColumn;

    @FXML
    private TableColumn<Review, String> userColumn;

    @FXML
    private TableColumn<Review, Integer> ratingColumn;

    @FXML
    private TableColumn<Review, String> reviewColumn;

    @FXML
    public void initialize() {
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        reviewColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // This is a placeholder for fetching real data.
        reviewsTable.setItems(getDummyReviews());
    }

    private ObservableList<Review> getDummyReviews() {
        ObservableList<Review> reviews = FXCollections.observableArrayList();

        Review review1 = new Review();
        review1.setReviewId(1);
        review1.setVehicleId(101);
        review1.setCustomerId(1);
        review1.setRating(5);
        review1.setComment("Excellent bike! Had a great time riding it.");

        Review review2 = new Review();
        review2.setReviewId(2);
        review2.setVehicleId(102);
        review2.setCustomerId(2);
        review2.setRating(4);
        review2.setComment("Good bike, but the seat could be more comfortable.");

        reviews.add(review1);
        reviews.add(review2);

        return reviews;
    }
}
