package com.librarymanagement.library_management_system.service;

import com.librarymanagement.library_management_system.dto.CreateCategoryRequest;
import com.librarymanagement.library_management_system.entity.Category;
import com.librarymanagement.library_management_system.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<?> createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists");
        }
        Category category = new Category();
        category.setName(request.getName());
        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}

