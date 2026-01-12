package com.onriderentals.dao;

import com.onriderentals.model.Favorite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {

    public void addFavorite(int customerId, int vehicleId) throws SQLException {
        String sql = "INSERT IGNORE INTO favorites (customer_id, vehicle_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, vehicleId);
            stmt.executeUpdate();
        }
    }

    public void removeFavorite(int customerId, int vehicleId) throws SQLException {
        String sql = "DELETE FROM favorites WHERE customer_id = ? AND vehicle_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, vehicleId);
            stmt.executeUpdate();
        }
    }

    public boolean isFavorite(int customerId, int vehicleId) {
        String sql = "SELECT 1 FROM favorites WHERE customer_id = ? AND vehicle_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, vehicleId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> getFavoriteVehicleIds(int customerId) {
        List<Integer> vehicleIds = new ArrayList<>();
        String sql = "SELECT vehicle_id FROM favorites WHERE customer_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vehicleIds.add(rs.getInt("vehicle_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicleIds;
    }
}
