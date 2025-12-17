package com.librarymanagement.library_management_system.repository;

import com.librarymanagement.library_management_system.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    boolean existsByIsbn(String isbn);
    Optional<Book> findByIsbn(String isbn);
}

