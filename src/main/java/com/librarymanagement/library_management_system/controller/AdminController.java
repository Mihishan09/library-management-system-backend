package com.librarymanagement.library_management_system.controller;

import com.librarymanagement.library_management_system.dto.BlacklistUserRequest;
import com.librarymanagement.library_management_system.dto.CreateBookRequest;
import com.librarymanagement.library_management_system.dto.CreateCategoryRequest;
import com.librarymanagement.library_management_system.dto.UpdateBookStatusRequest;
import com.librarymanagement.library_management_system.service.AdminService;
import com.librarymanagement.library_management_system.service.BookService;
import com.librarymanagement.library_management_system.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for librarian/admin endpoints.
 * All endpoints require LIBRARIAN role authorization.
 * Handles category management, book management, and user blacklisting.
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AdminService adminService;

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/users")
    public ResponseEntity<?> listUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/books")
    public ResponseEntity<?> createBook(@Valid @RequestBody CreateBookRequest request) {
        return bookService.createBook(request);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PatchMapping("/books/{id}/status")
    public ResponseEntity<?> updateBookStatus(@PathVariable Integer id,
                                              @Valid @RequestBody UpdateBookStatusRequest request) {
        return bookService.updateStatus(id, request);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PatchMapping("/users/{id}/blacklist")
    public ResponseEntity<?> blacklistUser(@PathVariable Integer id,
                                           @Valid @RequestBody BlacklistUserRequest request) {
        return adminService.blacklistUser(id, request);
    }
}

