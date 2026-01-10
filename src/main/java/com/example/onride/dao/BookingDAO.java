package com.example.onride.dao;

import com.example.onride.database.Database;
import com.example.onride.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public int getTotalBookings() {
        String sql = "SELECT COUNT(*) FROM bookings";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (vehicle_id, customer_id, start_date, end_date, total_amount, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, booking.getVehicleId());
            preparedStatement.setInt(2, booking.getCustomerId());
            preparedStatement.setDate(3, new java.sql.Date(booking.getStartDate().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(booking.getEndDate().getTime()));
            preparedStatement.setDouble(5, booking.getTotalAmount());
            preparedStatement.setString(6, booking.getStatus());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setBookingId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Booking getBookingById(int id) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractBookingFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                bookings.add(extractBookingFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE customer_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bookings.add(extractBookingFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET vehicle_id = ?, customer_id = ?, start_date = ?, end_date = ?, total_amount = ?, status = ? WHERE booking_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, booking.getVehicleId());
            preparedStatement.setInt(2, booking.getCustomerId());
            preparedStatement.setDate(3, new java.sql.Date(booking.getStartDate().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(booking.getEndDate().getTime()));
            preparedStatement.setDouble(5, booking.getTotalAmount());
            preparedStatement.setString(6, booking.getStatus());
            preparedStatement.setInt(7, booking.getBookingId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBooking(int id) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Booking extractBookingFromResultSet(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(resultSet.getInt("booking_id"));
        booking.setVehicleId(resultSet.getInt("vehicle_id"));
        booking.setCustomerId(resultSet.getInt("customer_id"));
        booking.setStartDate(resultSet.getDate("start_date"));
        booking.setEndDate(resultSet.getDate("end_date"));
        booking.setTotalAmount(resultSet.getDouble("total_amount"));
        booking.setStatus(resultSet.getString("status"));
        booking.setCreatedAt(resultSet.getTimestamp("created_at"));
        return booking;
    }
}
