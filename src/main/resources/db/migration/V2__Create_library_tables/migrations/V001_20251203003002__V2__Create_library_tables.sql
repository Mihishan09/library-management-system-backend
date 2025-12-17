-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('LIBRARIAN', 'USER') NOT NULL,
    is_blacklisted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create books table
CREATE TABLE IF NOT EXISTS books (
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

-- Add created_at column if it doesn't exist (for existing tables created before this migration)
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'books' 
    AND COLUMN_NAME = 'created_at');
SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE books ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP', 
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Create reservations table
CREATE TABLE IF NOT EXISTS reservations (
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
-- Insert Sample Data (using INSERT IGNORE to avoid duplicates)
-- ============================================

-- Insert categories (ignore duplicates)
INSERT IGNORE INTO categories (name) VALUES 
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

-- Insert users (ignore duplicates - use actual hashed passwords in production)
INSERT IGNORE INTO users (email, password, role) VALUES 
('admin@library.com', 'hashed_password', 'LIBRARIAN'),
('librarian@library.com', 'hashed_password', 'LIBRARIAN'),
('user1@email.com', 'hashed_password', 'USER'),
('user2@email.com', 'hashed_password', 'USER');

-- Insert books (ignore duplicates)
INSERT IGNORE INTO books (title, author, genre, language, isbn, category_id) VALUES
('To Kill a Mockingbird', 'Harper Lee', 'Classic', 'English', '9780061120084', 1),
('1984', 'George Orwell', 'Dystopian', 'English', '9780451524935', 1),
('Pride and Prejudice', 'Jane Austen', 'Romance', 'English', '9780141439518', 1),
('Dune', 'Frank Herbert', 'Science Fiction', 'English', '9780441172719', 2),
('Foundation', 'Isaac Asimov', 'Science Fiction', 'English', '9780553293357', 2),
('The Girl with the Dragon Tattoo', 'Stieg Larsson', 'Mystery', 'English', '9780307269751', 3),
('Gone Girl', 'Gillian Flynn', 'Thriller', 'English', '9780307588371', 3);

-- Insert sample reservation (ignore duplicates)
INSERT IGNORE INTO reservations (user_id, book_id, reservation_date, due_date) 
SELECT 3, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY)
WHERE EXISTS (SELECT 1 FROM users WHERE id = 3) 
  AND EXISTS (SELECT 1 FROM books WHERE id = 1)
  AND NOT EXISTS (SELECT 1 FROM reservations WHERE user_id = 3 AND book_id = 1);

-- Update book status to RESERVED (only if book exists and is available)
UPDATE books SET status = 'RESERVED' 
WHERE id = 1 AND status = 'AVAILABLE' 
AND EXISTS (SELECT 1 FROM reservations WHERE book_id = 1);

SELECT 'Database migration completed successfully!' as message;