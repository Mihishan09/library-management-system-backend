package com.librarymanagement.library_management_system.service;

import com.librarymanagement.library_management_system.dto.AuthResponse;
import com.librarymanagement.library_management_system.dto.LoginRequest;
import com.librarymanagement.library_management_system.dto.SignupRequest;
import com.librarymanagement.library_management_system.entity.User;
import com.librarymanagement.library_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for user authentication and management operations.
 * Handles user signup, login, and JWT token generation.
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    /**
     * Registers a new user in the system.
     *
     * @param signupRequest the signup request containing email, password, and role
     * @return AuthResponse containing JWT token and user info on success, or error message on failure
     */
    public AuthResponse signup(SignupRequest signupRequest) {
        // Check if user already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new AuthResponse("Email already exists");
        }
        
        // Validate role
        User.Role role;
        try {
            role = User.Role.valueOf(signupRequest.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            return new AuthResponse("Invalid role. Must be LIBRARIAN or USER");
        }
        
        // Create new user
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRole(role);
        user.setIsBlacklisted(false);
        
        user = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        
        return new AuthResponse(token, user.getEmail(), user.getRole().name(), user.getId());
    }
    
    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginRequest the login request containing email and password
     * @return AuthResponse containing JWT token and user info on success, or error message on failure
     */
    public AuthResponse login(LoginRequest loginRequest) {
        // Find user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);
        
        if (user == null) {
            return new AuthResponse("Invalid email or password");
        }
        
        // Check if user is blacklisted
        if (user.getIsBlacklisted()) {
            return new AuthResponse("Account has been blacklisted");
        }
        
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new AuthResponse("Invalid email or password");
        }
        
        // Generate JWT token
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        
        return new AuthResponse(token, user.getEmail(), user.getRole().name(), user.getId());
    }
}

