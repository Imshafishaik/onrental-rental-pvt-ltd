package com.example.onride.dao;

import com.example.onride.database.Database;
import com.example.onride.model.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public void addReview(Review review) {
        String sql = "INSERT INTO reviews (user_id, vehicle_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, review.getUserId());
            preparedStatement.setInt(2, review.getVehicleId());
            preparedStatement.setInt(3, review.getRating());
            preparedStatement.setString(4, review.getComment());
            preparedStatement.setDate(5, new java.sql.Date(review.getReviewDate().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Review> getReviewsByVehicleId(int vehicleId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE vehicle_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, vehicleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Review review = new Review();
                review.setId(resultSet.getInt("id"));
                review.setUserId(resultSet.getInt("user_id"));
                review.setVehicleId(resultSet.getInt("vehicle_id"));
                review.setRating(resultSet.getInt("rating"));
                review.setComment(resultSet.getString("comment"));
                review.setReviewDate(resultSet.getDate("review_date"));
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
