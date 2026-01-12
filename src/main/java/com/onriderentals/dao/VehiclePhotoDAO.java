package com.onriderentals.dao;

import com.onriderentals.model.VehiclePhoto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiclePhotoDAO {

    public void addPhoto(VehiclePhoto photo) throws SQLException {
        String sql = "INSERT INTO vehicle_photos (vehicle_id, photo_url) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, photo.getVehicleId());
            stmt.setString(2, photo.getPhotoUrl());
            stmt.executeUpdate();
        }
    }

    public List<VehiclePhoto> getPhotosByVehicleId(int vehicleId) {
        List<VehiclePhoto> photos = new ArrayList<>();
        String sql = "SELECT * FROM vehicle_photos WHERE vehicle_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                VehiclePhoto photo = new VehiclePhoto();
                photo.setPhotoId(rs.getInt("photo_id"));
                photo.setVehicleId(rs.getInt("vehicle_id"));
                photo.setPhotoUrl(rs.getString("photo_url"));
                photos.add(photo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photos;
    }

    public void deletePhoto(int photoId) throws SQLException {
        String sql = "DELETE FROM vehicle_photos WHERE photo_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, photoId);
            stmt.executeUpdate();
        }
    }

    public void deleteAllPhotosForVehicle(int vehicleId) throws SQLException {
        String sql = "DELETE FROM vehicle_photos WHERE vehicle_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);
            stmt.executeUpdate();
        }
    }
}
