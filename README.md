# CineBook – Movie Ticket Booking System

A simple Java Swing + MySQL desktop application for browsing movies and booking tickets.

## Features
- Browse movies and filter by genre
- View posters, details and ticket prices
- Book tickets with showtime selection and seat availability check
- View booking history

## Tech Stack
- Java Swing
- MySQL + JDBC

## Prerequisites
- JDK 8 or higher
- MySQL Server
- MySQL JDBC Driver

## Database Setup

```sql
CREATE DATABASE movie_booking;
USE movie_booking;

CREATE TABLE movies (
    movie_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    genre VARCHAR(50),
    language VARCHAR(30),
    duration_minutes INT,
    price DECIMAL(8,2)
);

CREATE TABLE shows (
    show_id INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT,
    show_date DATE,
    show_time TIME,
    seats_available INT DEFAULT 100,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id)
);

CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100) NOT NULL,
    show_id INT,
    tickets INT,
    total_amount DECIMAL(10,2),
    FOREIGN KEY (show_id) REFERENCES shows(show_id)
);
```

## Configuration
Update credentials in `DBConnection.java` if needed:

```java
URL      = "jdbc:mysql://localhost:3306/movie_booking"
USERNAME = "root"
PASSWORD = "your_password"
```

## How to Run
1. Add the MySQL JDBC driver to the classpath.
2. Place movie posters in an `images/` folder.
3. Run `MovieBookingSystem.java`.

## Project Structure
```
├── MovieBookingSystem.java   # Main menu
├── MovieCatalogue.java       # Movie listing
├── BookingForm.java          # Booking form
├── ViewBookings.java         # Booking history
├── DBConnection.java         # Database connection
└── images/                   # Movie posters
```
