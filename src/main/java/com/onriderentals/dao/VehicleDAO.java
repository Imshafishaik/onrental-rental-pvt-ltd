package com.onriderentals.dao;

import com.onriderentals.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public Vehicle getVehicleById(int vehicleId) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicle.setRenterId(rs.getInt("renter_id"));
                vehicle.setMake(rs.getString("brand"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setYear(rs.getInt("year"));
                vehicle.setType(rs.getString("type"));
                vehicle.setPricePerDay(rs.getDouble("price_per_day"));
                vehicle.setStatus(rs.getString("status"));
                vehicle.setLocation(rs.getString("location"));
                vehicle.setImageKey(rs.getString("image_key"));
                return vehicle;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicle.setRenterId(rs.getInt("renter_id"));
                vehicle.setMake(rs.getString("brand"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setYear(rs.getInt("year"));
                vehicle.setType(rs.getString("type"));
                vehicle.setPricePerDay(rs.getDouble("price_per_day"));
                vehicle.setStatus(rs.getString("status"));
                vehicle.setLocation(rs.getString("location"));
                vehicle.setImageKey(rs.getString("image_key"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public List<Vehicle> getVehiclesByRenterId(int renterId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE renter_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, renterId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleId(rs.getInt("vehicle_id"));
                vehicle.setRenterId(rs.getInt("renter_id"));
                vehicle.setMake(rs.getString("brand"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setYear(rs.getInt("year"));
                vehicle.setType(rs.getString("type"));
                vehicle.setPricePerDay(rs.getDouble("price_per_day"));
                vehicle.setStatus(rs.getString("status"));
                vehicle.setLocation(rs.getString("location"));
                vehicle.setImageKey(rs.getString("image_key"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (renter_id, type, brand, model, year, price_per_day, location, status, image_key) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicle.getRenterId());
            stmt.setString(2, vehicle.getType());
            stmt.setString(3, vehicle.getMake());
            stmt.setString(4, vehicle.getModel());
            stmt.setInt(5, vehicle.getYear());
            stmt.setDouble(6, vehicle.getPricePerDay());
            stmt.setString(7, vehicle.getLocation() != null ? vehicle.getLocation() : "New York");
            stmt.setString(8, vehicle.getStatus());
            stmt.setString(9, vehicle.getImageKey());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET type = ?, brand = ?, model = ?, year = ?, price_per_day = ?, status = ?, location = ?, image_key = ? WHERE vehicle_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getType());
            stmt.setString(2, vehicle.getMake());
            stmt.setString(3, vehicle.getModel());
            stmt.setInt(4, vehicle.getYear());
            stmt.setDouble(5, vehicle.getPricePerDay());
            stmt.setString(6, vehicle.getStatus());
            stmt.setString(7, vehicle.getLocation());
            stmt.setString(8, vehicle.getImageKey());
            stmt.setInt(9, vehicle.getVehicleId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteVehicle(int vehicleId) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";
        
        try(Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vehicleId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
