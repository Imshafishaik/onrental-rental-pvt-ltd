package com.example.onride.dao;

import com.example.onride.database.Database;
import com.example.onride.model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles(type, brand, model, year, registration_number, availability, price_per_day, image_url, renter_id, location, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, vehicle.getType());
            preparedStatement.setString(2, vehicle.getBrand());
            preparedStatement.setString(3, vehicle.getModel());
            preparedStatement.setInt(4, vehicle.getYear());
            preparedStatement.setString(5, vehicle.getRegistrationNumber());
            preparedStatement.setBoolean(6, vehicle.isAvailability());
            preparedStatement.setDouble(7, vehicle.getPricePerDay());
            preparedStatement.setString(8, vehicle.getImageUrl());
            preparedStatement.setInt(9, vehicle.getRenterId());
            preparedStatement.setString(10, vehicle.getLocation());
            preparedStatement.setString(11, vehicle.getStatus());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehicle.setVehicleId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Vehicle getVehicleById(int id) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractVehicleFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                vehicles.add(extractVehicleFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    private Vehicle extractVehicleFromResultSet(ResultSet resultSet) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(resultSet.getInt("vehicle_id"));
        vehicle.setType(resultSet.getString("type"));
        vehicle.setBrand(resultSet.getString("brand"));
        vehicle.setModel(resultSet.getString("model"));
        vehicle.setYear(resultSet.getInt("year"));
        vehicle.setRegistrationNumber(resultSet.getString("registration_number"));
        vehicle.setAvailability(resultSet.getBoolean("availability"));
        vehicle.setPricePerDay(resultSet.getDouble("price_per_day"));
        vehicle.setImageUrl(resultSet.getString("image_url"));
        vehicle.setRenterId(resultSet.getInt("renter_id"));
        vehicle.setLocation(resultSet.getString("location"));
        vehicle.setStatus(resultSet.getString("status"));
        return vehicle;
    }
}
