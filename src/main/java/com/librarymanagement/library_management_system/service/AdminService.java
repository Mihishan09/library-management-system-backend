package com.librarymanagement.library_management_system.service;

import com.librarymanagement.library_management_system.dto.BlacklistUserRequest;
import com.librarymanagement.library_management_system.entity.User;
import com.librarymanagement.library_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> blacklistUser(Integer userId, BlacklistUserRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        user.setIsBlacklisted(request.getIsBlacklisted());
        userRepository.save(user);
        return ResponseEntity.ok("User blacklist status updated");
    }
}

