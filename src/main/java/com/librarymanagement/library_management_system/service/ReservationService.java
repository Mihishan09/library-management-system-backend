package com.librarymanagement.library_management_system.service;

import com.librarymanagement.library_management_system.dto.ReserveBookRequest;
import com.librarymanagement.library_management_system.entity.Book;
import com.librarymanagement.library_management_system.entity.Reservation;
import com.librarymanagement.library_management_system.entity.User;
import com.librarymanagement.library_management_system.repository.BookRepository;
import com.librarymanagement.library_management_system.repository.ReservationRepository;
import com.librarymanagement.library_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> reserveBook(ReserveBookRequest request, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String email = principal.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        User user = userOpt.get();
        if (Boolean.TRUE.equals(user.getIsBlacklisted())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is blacklisted");
        }

        Book book = bookRepository.findById(request.getBookId()).orElse(null);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
        if (book.getStatus() == Book.Status.RESERVED) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Book is already reserved");
        }

        int days = request.getDays();
        if (days != 7 && days != 14 && days != 21) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Days must be 7, 14, or 21");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDate.now());
        reservation.setDueDate(LocalDate.now().plusDays(days));
        reservation.setStatus(Reservation.Status.ACTIVE);

        book.setStatus(Book.Status.RESERVED);

        reservationRepository.save(reservation);
        bookRepository.save(book);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }
}

