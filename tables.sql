-- ============================
-- DATABASE
-- ============================
CREATE DATABASE IF NOT EXISTS onride_rentals;
USE onride_rentals;

-- ============================
-- USERS (Admin, Renter, Customer)
-- role: -1 = Admin, -2 = Renter, -3 = Customer (default)
-- ============================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('-1', '-2', '-3') DEFAULT '-3',
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ============================
-- VEHICLES
-- ============================
CREATE TABLE vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    renter_id INT NOT NULL,
    type ENUM('BIKE', 'CAR') NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year INT,
    price_per_day DOUBLE NOT NULL,
    location VARCHAR(150) NOT NULL,
    status ENUM('AVAILABLE', 'BOOKED', 'MAINTENANCE') DEFAULT 'AVAILABLE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (renter_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- ============================
-- VEHICLE PHOTOS
-- ============================
CREATE TABLE vehicle_photos (
    photo_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    photo_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
        ON DELETE CASCADE
);

-- ============================
-- BOOKINGS
-- ============================
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    customer_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_amount DOUBLE NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED') DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
        ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- ============================
-- PAYMENTS
-- ============================
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    amount DOUBLE NOT NULL,
    method ENUM('CARD', 'UPI', 'NETBANKING', 'CASH'),
    status ENUM('SUCCESS', 'FAILED', 'REFUNDED') DEFAULT 'SUCCESS',
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
        ON DELETE CASCADE
);

-- ============================
-- PAYMENT HISTORY (OPTIONAL)
-- ============================
CREATE TABLE payment_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    payment_id INT NOT NULL,
    status ENUM('SUCCESS', 'FAILED', 'REFUNDED'),
    note VARCHAR(255),
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_id) REFERENCES payments(payment_id)
        ON DELETE CASCADE
);

-- ============================
-- FAVORITES
-- ============================
CREATE TABLE favorites (
    favorite_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
        ON DELETE CASCADE,
    UNIQUE (customer_id, vehicle_id)
);

-- ============================
-- REVIEWS & RATINGS
-- ============================
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    customer_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    review_date DATE DEFAULT CURRENT_DATE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
        ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

