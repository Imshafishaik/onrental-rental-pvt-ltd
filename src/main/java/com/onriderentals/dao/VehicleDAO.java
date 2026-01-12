package com.onriderentals.dao;

import com.onriderentals.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                vehicle.setColor(rs.getString("color"));
                vehicle.setLicensePlate(rs.getString("license_plate"));
                vehicle.setVin(rs.getString("registration_number"));
                vehicle.setMileage(rs.getDouble("total_km_driven"));
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
                vehicle.setColor(rs.getString("color"));
                vehicle.setLicensePlate(rs.getString("license_plate"));
                vehicle.setVin(rs.getString("registration_number"));
                vehicle.setMileage(rs.getDouble("total_km_driven"));
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
                vehicle.setColor(rs.getString("color"));
                vehicle.setLicensePlate(rs.getString("license_plate"));
                vehicle.setVin(rs.getString("registration_number"));
                vehicle.setMileage(rs.getDouble("total_km_driven"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (renter_id, type, brand, model, year, price_per_day, location, status, image_key, color, license_plate, registration_number, total_km_driven) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, vehicle.getRenterId());
            stmt.setString(2, vehicle.getType());
            stmt.setString(3, vehicle.getMake());
            stmt.setString(4, vehicle.getModel());
            stmt.setInt(5, vehicle.getYear());
            stmt.setDouble(6, vehicle.getPricePerDay());
            stmt.setString(7, vehicle.getLocation() != null ? vehicle.getLocation() : "Unknown");
            stmt.setString(8, vehicle.getStatus());
            stmt.setString(9, vehicle.getImageKey());
            stmt.setString(10, vehicle.getColor());
            stmt.setString(11, vehicle.getLicensePlate());
            stmt.setString(12, vehicle.getVin());
            stmt.setDouble(13, vehicle.getMileage());

            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                vehicle.setVehicleId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET type = ?, brand = ?, model = ?, year = ?, price_per_day = ?, status = ?, location = ?, image_key = ?, color = ?, license_plate = ?, registration_number = ?, total_km_driven = ? WHERE vehicle_id = ?";

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
            stmt.setString(9, vehicle.getColor());
            stmt.setString(10, vehicle.getLicensePlate());
            stmt.setString(11, vehicle.getVin());
            stmt.setDouble(12, vehicle.getMileage());
            stmt.setInt(13, vehicle.getVehicleId());

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

    public List<String> getUniqueLocations() {
        List<String> locations = new ArrayList<>();
        String sql = "SELECT DISTINCT location FROM vehicles WHERE location IS NOT NULL AND status = 'AVAILABLE'";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                locations.add(rs.getString("location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }

    public int getTotalVehiclesCount() {
        String sql = "SELECT COUNT(*) FROM vehicles";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
