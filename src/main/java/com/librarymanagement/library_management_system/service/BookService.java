package com.librarymanagement.library_management_system.service;

import com.librarymanagement.library_management_system.dto.CreateBookRequest;
import com.librarymanagement.library_management_system.dto.UpdateBookStatusRequest;
import com.librarymanagement.library_management_system.entity.Book;
import com.librarymanagement.library_management_system.entity.Category;
import com.librarymanagement.library_management_system.repository.BookRepository;
import com.librarymanagement.library_management_system.repository.CategoryRepository;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Service class for book management operations.
 * Handles book creation, status updates, search/filtering, and retrieval.
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<?> createBook(CreateBookRequest request) {
        if (StringUtils.hasText(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Book with same ISBN already exists");
        }

        Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category not found");
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setLanguage(request.getLanguage());
        book.setIsbn(request.getIsbn());
        book.setImageUrl(request.getImageUrl());
        book.setCategory(categoryOpt.get());

        bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    public ResponseEntity<?> updateStatus(Integer bookId, UpdateBookStatusRequest request) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
        Book.Status status;
        try {
            status = Book.Status.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status. Use AVAILABLE or RESERVED");
        }
        book.setStatus(status);
        bookRepository.save(book);
        return ResponseEntity.ok(book);
    }

    public ResponseEntity<?> getBookById(Integer id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
        return ResponseEntity.ok(book);
    }

    public List<Book> searchBooks(String category, String author, String genre, String language) {
        Specification<Book> spec = (root, query, cb) -> {
            query.distinct(true);
            return cb.conjunction();
        };

        if (StringUtils.hasText(category)) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.join("category", JoinType.LEFT).get("name")),
                            category.toLowerCase()));
        }
        if (StringUtils.hasText(author)) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
        }
        if (StringUtils.hasText(genre)) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("genre")), "%" + genre.toLowerCase() + "%"));
        }
        if (StringUtils.hasText(language)) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("language")), "%" + language.toLowerCase() + "%"));
        }

        return bookRepository.findAll(spec);
    }
}

