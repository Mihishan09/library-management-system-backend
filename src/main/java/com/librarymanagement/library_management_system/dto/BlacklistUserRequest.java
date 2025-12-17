package com.librarymanagement.library_management_system.dto;

import jakarta.validation.constraints.NotNull;

public class BlacklistUserRequest {

    @NotNull(message = "isBlacklisted flag is required")
    private Boolean isBlacklisted;

    public Boolean getIsBlacklisted() {
        return isBlacklisted;
    }

    public void setIsBlacklisted(Boolean isBlacklisted) {
        this.isBlacklisted = isBlacklisted;
    }
}

