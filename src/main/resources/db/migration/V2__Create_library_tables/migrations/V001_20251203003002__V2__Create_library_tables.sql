-- ============================================
-- Simple Database Migration Script
-- ============================================

-- Create database
CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

-- Drop tables if they exist (in correct order due to foreign keys)
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS categories;

-- Create categories table
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('LIBRARIAN', 'USER') NOT NULL,
    is_blacklisted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create books table
CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    language VARCHAR(50),
    isbn VARCHAR(13) UNIQUE,
    status ENUM('AVAILABLE', 'RESERVED') NOT NULL DEFAULT 'AVAILABLE',
    image_url VARCHAR(512),
    category_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Create reservations table
CREATE TABLE reservations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    reservation_date DATE NOT NULL,
    due_date DATE NOT NULL,
    status ENUM('ACTIVE', 'RETURNED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- ============================================
-- Insert Sample Data
-- ============================================

-- Insert categories
INSERT INTO categories (name) VALUES 
('Fiction'),
('Science Fiction'),
('Mystery & Thriller'),
('Romance'),
('Science & Technology'),
('Biography'),
('History'),
('Children''s Books'),
('Fantasy'),
('Self-Help');

-- Insert users (use actual hashed passwords in production)
INSERT INTO users (email, password, role) VALUES 
('admin@library.com', 'hashed_password', 'LIBRARIAN'),
('librarian@library.com', 'hashed_password', 'LIBRARIAN'),
('user1@email.com', 'hashed_password', 'USER'),
('user2@email.com', 'hashed_password', 'USER');

-- Insert books
INSERT INTO books (title, author, genre, language, isbn, category_id) VALUES
('To Kill a Mockingbird', 'Harper Lee', 'Classic', 'English', '9780061120084', 1),
('1984', 'George Orwell', 'Dystopian', 'English', '9780451524935', 1),
('Pride and Prejudice', 'Jane Austen', 'Romance', 'English', '9780141439518', 1),
('Dune', 'Frank Herbert', 'Science Fiction', 'English', '9780441172719', 2),
('Foundation', 'Isaac Asimov', 'Science Fiction', 'English', '9780553293357', 2),
('The Girl with the Dragon Tattoo', 'Stieg Larsson', 'Mystery', 'English', '9780307269751', 3),
('Gone Girl', 'Gillian Flynn', 'Thriller', 'English', '9780307588371', 3);

-- Insert sample reservation
INSERT INTO reservations (user_id, book_id, reservation_date, due_date) VALUES
(3, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY));

-- Update book status to RESERVED
UPDATE books SET status = 'RESERVED' WHERE id = 1;

SELECT 'Database migration completed successfully!' as message;