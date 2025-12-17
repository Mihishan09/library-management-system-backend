package com.librarymanagement.library_management_system.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateBookStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

