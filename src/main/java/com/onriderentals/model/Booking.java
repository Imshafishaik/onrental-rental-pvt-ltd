package com.onriderentals.model;

import com.onriderentals.dao.UserDAO;
import com.onriderentals.dao.VehicleDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {

    private int bookingId;
    private int customerId;
    private int vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;
    private String status;
    private LocalDateTime createdAt;
    private User customer;
    private Vehicle vehicle;

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCustomer() {
        if (customer == null) {
            customer = new UserDAO().getUserById(customerId);
        }
        return customer;
    }

    public Vehicle getVehicle() {
        if (vehicle == null) {
            vehicle = new VehicleDAO().getVehicleById(vehicleId);
        }
        return vehicle;
    }
}
