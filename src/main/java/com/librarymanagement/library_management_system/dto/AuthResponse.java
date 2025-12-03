package com.librarymanagement.library_management_system.dto;

public class AuthResponse {
    
    private String token;
    private String email;
    private String role;
    private Integer userId;
    private String message;
    
    public AuthResponse() {
    }
    
    public AuthResponse(String token, String email, String role, Integer userId) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.userId = userId;
    }
    
    public AuthResponse(String message) {
        this.message = message;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}

