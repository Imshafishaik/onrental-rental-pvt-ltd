package com.example.onride.dao;

import com.example.onride.database.Database;
import com.example.onride.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payments (booking_id, amount, payment_date, payment_method, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, payment.getBookingId());
            preparedStatement.setDouble(2, payment.getAmount());
            preparedStatement.setDate(3, new java.sql.Date(payment.getPaymentDate().getTime()));
            preparedStatement.setString(4, payment.getPaymentMethod());
            preparedStatement.setString(5, payment.getStatus());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Payment getPaymentById(int id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractPaymentFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                payments.add(extractPaymentFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE payments SET booking_id = ?, amount = ?, payment_date = ?, payment_method = ?, status = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, payment.getBookingId());
            preparedStatement.setDouble(2, payment.getAmount());
            preparedStatement.setDate(3, new java.sql.Date(payment.getPaymentDate().getTime()));
            preparedStatement.setString(4, payment.getPaymentMethod());
            preparedStatement.setString(5, payment.getStatus());
            preparedStatement.setInt(6, payment.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePayment(int id) {
        String sql = "DELETE FROM payments WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Payment extractPaymentFromResultSet(ResultSet resultSet) throws SQLException {
        Payment payment = new Payment();
        payment.setId(resultSet.getInt("id"));
        payment.setBookingId(resultSet.getInt("booking_id"));
        payment.setAmount(resultSet.getDouble("amount"));
        payment.setPaymentDate(resultSet.getDate("payment_date"));
        payment.setPaymentMethod(resultSet.getString("payment_method"));
        payment.setStatus(resultSet.getString("status"));
        return payment;
    }
}
