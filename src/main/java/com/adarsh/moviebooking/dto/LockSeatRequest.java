package com.adarsh.moviebooking.dto;

import jakarta.validation.constraints.NotNull;

public class LockSeatRequest {

    @NotNull(message = "seatId is required")
    private Long seatId;

    public LockSeatRequest() {
    }

    public LockSeatRequest(Long seatId) {
        this.seatId = seatId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }
}
