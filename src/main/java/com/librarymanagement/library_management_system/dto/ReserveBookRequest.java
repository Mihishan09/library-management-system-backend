package com.librarymanagement.library_management_system.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReserveBookRequest {

    @NotNull(message = "Book ID is required")
    private Integer bookId;

    @NotNull(message = "Days is required")
    @Min(value = 7, message = "Days must be 7, 14, or 21")
    @Max(value = 21, message = "Days must be 7, 14, or 21")
    private Integer days;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}

