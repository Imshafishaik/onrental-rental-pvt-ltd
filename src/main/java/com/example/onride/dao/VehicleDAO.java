package com.example.onride.dao;

import com.example.onride.database.Database;
import com.example.onride.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleId(resultSet.getInt("vehicle_id"));
                vehicle.setRenterId(resultSet.getInt("renter_id"));
                vehicle.setType(resultSet.getString("type"));
                vehicle.setBrand(resultSet.getString("brand"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setYear(resultSet.getInt("year"));
                vehicle.setPricePerDay(resultSet.getDouble("price_per_day"));
                vehicle.setLocation(resultSet.getString("location"));
                vehicle.setStatus(resultSet.getString("status"));
                vehicle.setCreatedAt(resultSet.getTimestamp("created_at"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public Vehicle getVehicleById(int vehicleId) {
        Vehicle vehicle = null;
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, vehicleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                vehicle = new Vehicle();
                vehicle.setVehicleId(resultSet.getInt("vehicle_id"));
                vehicle.setRenterId(resultSet.getInt("renter_id"));
                vehicle.setType(resultSet.getString("type"));
                vehicle.setBrand(resultSet.getString("brand"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setYear(resultSet.getInt("year"));
                vehicle.setPricePerDay(resultSet.getDouble("price_per_day"));
                vehicle.setLocation(resultSet.getString("location"));
                vehicle.setStatus(resultSet.getString("status"));
                vehicle.setCreatedAt(resultSet.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

    public List<Vehicle> searchVehicles(String type, String location, Double minPrice, Double maxPrice, String status) {
        List<Vehicle> vehicles = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM vehicles WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (type != null && !type.isEmpty() && !type.equalsIgnoreCase("All")) {
            sql.append(" AND LOWER(type) = LOWER(?)");
            parameters.add(type);
        }

        if (location != null && !location.trim().isEmpty()) {
            sql.append(" AND LOWER(location) LIKE LOWER(?)");
            parameters.add("%" + location.trim() + "%");
        }

        if (minPrice != null) {
            sql.append(" AND price_per_day >= ?");
            parameters.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND price_per_day <= ?");
            parameters.add(maxPrice);
        }

        if (status != null && !status.isEmpty()) {
            // status expected to be AVAILABLE or empty for any
            sql.append(" AND status = ?");
            parameters.add(status);
        }

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleId(resultSet.getInt("vehicle_id"));
                vehicle.setRenterId(resultSet.getInt("renter_id"));
                vehicle.setType(resultSet.getString("type"));
                vehicle.setBrand(resultSet.getString("brand"));
                vehicle.setModel(resultSet.getString("model"));
                vehicle.setYear(resultSet.getInt("year"));
                vehicle.setPricePerDay(resultSet.getDouble("price_per_day"));
                vehicle.setLocation(resultSet.getString("location"));
                vehicle.setStatus(resultSet.getString("status"));
                vehicle.setCreatedAt(resultSet.getTimestamp("created_at"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (renter_id, type, brand, model, year, price_per_day, location, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, vehicle.getRenterId());
            preparedStatement.setString(2, vehicle.getType());
            preparedStatement.setString(3, vehicle.getBrand());
            preparedStatement.setString(4, vehicle.getModel());
            preparedStatement.setInt(5, vehicle.getYear());
            preparedStatement.setDouble(6, vehicle.getPricePerDay());
            preparedStatement.setString(7, vehicle.getLocation());
            preparedStatement.setString(8, vehicle.getStatus() != null ? vehicle.getStatus() : "AVAILABLE");

            int affected = preparedStatement.executeUpdate();
            if (affected == 0) return false;

            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                vehicle.setVehicleId(keys.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteVehicle(int vehicleId) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, vehicleId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateVehicleStatus(int vehicleId, String status) {
        String sql = "UPDATE vehicles SET status = ? WHERE vehicle_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, vehicleId);
            int updated = preparedStatement.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
