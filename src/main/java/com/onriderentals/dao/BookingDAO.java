package com.onriderentals.dao;

import com.onriderentals.model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BookingDAO {

    public void addBooking(Booking booking) {
        String bookingSql = "INSERT INTO bookings (customer_id, vehicle_id, start_date, end_date, total_amount, status) VALUES (?, ?, ?, ?, ?, ?)";
        String vehicleSql = "UPDATE vehicles SET status = ? WHERE vehicle_id = ?";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            // Create booking
            try (PreparedStatement bookingStmt = conn.prepareStatement(bookingSql)) {
                bookingStmt.setInt(1, booking.getCustomerId());
                bookingStmt.setInt(2, booking.getVehicleId());
                bookingStmt.setObject(3, booking.getStartDate());
                bookingStmt.setObject(4, booking.getEndDate());
                bookingStmt.setDouble(5, booking.getTotalCost());
                bookingStmt.setString(6, booking.getStatus());
                bookingStmt.executeUpdate();
            }

            // Update vehicle availability
            try (PreparedStatement vehicleStmt = conn.prepareStatement(vehicleSql)) {
                vehicleStmt.setString(1, "Booked");
                vehicleStmt.setInt(2, booking.getVehicleId());
                vehicleStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle rollback in case of error
        }
    }

    public List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE customer_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setCustomerId(rs.getInt("customer_id"));
                booking.setVehicleId(rs.getInt("vehicle_id"));
                booking.setStartDate(rs.getDate("start_date").toLocalDate());
                booking.setEndDate(rs.getDate("end_date").toLocalDate());
                booking.setTotalCost(rs.getDouble("total_amount"));
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setCustomerId(rs.getInt("customer_id"));
                booking.setVehicleId(rs.getInt("vehicle_id"));
                booking.setStartDate(rs.getDate("start_date").toLocalDate());
                booking.setEndDate(rs.getDate("end_date").toLocalDate());
                booking.setTotalCost(rs.getDouble("total_amount"));
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Map<Month, Double> getEarningsByMonth() {
        Map<Month, Double> earningsByMonth = new EnumMap<>(Month.class);
        String sql = "SELECT EXTRACT(MONTH FROM start_date) as month, SUM(total_amount) as earnings " +
                     "FROM bookings " +
                     "WHERE status = 'CONFIRMED' " +
                     "GROUP BY EXTRACT(MONTH FROM start_date)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Month month = Month.of(rs.getInt("month"));
                double earnings = rs.getDouble("earnings");
                earningsByMonth.put(month, earnings);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return earningsByMonth;
    }
}
