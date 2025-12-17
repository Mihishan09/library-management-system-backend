package com.librarymanagement.library_management_system.controller;

import com.librarymanagement.library_management_system.dto.ReserveBookRequest;
import com.librarymanagement.library_management_system.service.BookService;
import com.librarymanagement.library_management_system.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * REST controller for user-facing endpoints.
 * Handles book browsing, book details, and reservation operations.
 * Accessible to both USER and LIBRARIAN roles.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ReservationService reservationService;

    @PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
    @GetMapping("/books")
    public ResponseEntity<?> listBooks(@RequestParam(required = false) String category,
                                       @RequestParam(required = false) String author,
                                       @RequestParam(required = false) String genre,
                                       @RequestParam(required = false) String language) {
        return ResponseEntity.ok(bookService.searchBooks(category, author, genre, language));
    }

    @PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
    @GetMapping("/books/{id}")
    public ResponseEntity<?> getBook(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }

    @PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
    @PostMapping("/reservations")
    public ResponseEntity<?> reserveBook(@Valid @RequestBody ReserveBookRequest request, Principal principal) {
        return reservationService.reserveBook(request, principal);
    }
}

