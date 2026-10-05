package com.adarsh.moviebooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ConfirmBookingRequest {

    @NotNull(message = "seatId is required")
    private Long seatId;

    @NotBlank(message = "customerName is required")
    private String customerName;

    @NotBlank(message = "customerEmail is required")
    @Email(message = "customerEmail must be a valid email")
    private String customerEmail;

    public ConfirmBookingRequest() {
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
