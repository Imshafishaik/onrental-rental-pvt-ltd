package com.onriderentals.dao;

import com.onriderentals.model.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReviewDAO {

    public static class RatingStats {
        public double averageRating;
        public int reviewCount;

        public RatingStats(double averageRating, int reviewCount) {
            this.averageRating = averageRating;
            this.reviewCount = reviewCount;
        }
    }

    public RatingStats getRatingStats(int vehicleId) {
        String sql = "SELECT AVG(rating) as avg_rating, COUNT(*) as count FROM reviews WHERE vehicle_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double avg = rs.getDouble("avg_rating");
                int count = rs.getInt("count");
                return new RatingStats(avg > 0 ? avg : 0.0, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new RatingStats(0.0, 0);
    }

    public Map<Integer, RatingStats> getAllRatingStats() {
        Map<Integer, RatingStats> statsMap = new HashMap<>();
        String sql = "SELECT vehicle_id, AVG(rating) as avg_rating, COUNT(*) as count FROM reviews GROUP BY vehicle_id";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int vehicleId = rs.getInt("vehicle_id");
                double avg = rs.getDouble("avg_rating");
                int count = rs.getInt("count");
                statsMap.put(vehicleId, new RatingStats(avg, count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statsMap;
    }

    public boolean addReview(Review review) {
        String sql = "INSERT INTO reviews (vehicle_id, customer_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, review.getVehicleId());
            stmt.setInt(2, review.getCustomerId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.setObject(5, review.getReviewDate());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasReviewForBooking(int bookingId) {
        String sql = "SELECT COUNT(*) FROM reviews WHERE booking_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
