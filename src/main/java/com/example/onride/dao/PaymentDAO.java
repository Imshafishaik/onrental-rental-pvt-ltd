package com.example.onride.dao;

import com.example.onride.database.Database;
import com.example.onride.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentDAO {

    public void createPayment(Payment payment) {
        String sql = "INSERT INTO payments (booking_id, amount, payment_date, payment_method, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, payment.getBookingId());
            preparedStatement.setDouble(2, payment.getAmount());
            preparedStatement.setDate(3, new java.sql.Date(payment.getPaymentDate().getTime()));
            preparedStatement.setString(4, payment.getPaymentMethod());
            preparedStatement.setString(5, payment.getStatus());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Payment getPaymentByBookingId(int bookingId) {
        Payment payment = null;
        String sql = "SELECT * FROM payments WHERE booking_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                payment = new Payment();
                payment.setId(resultSet.getInt("id"));
                payment.setBookingId(resultSet.getInt("booking_id"));
                payment.setAmount(resultSet.getDouble("amount"));
                payment.setPaymentDate(resultSet.getDate("payment_date"));
                payment.setPaymentMethod(resultSet.getString("payment_method"));
                payment.setStatus(resultSet.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payment;
    }
}
