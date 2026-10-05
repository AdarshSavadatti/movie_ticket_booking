package com.adarsh.moviebooking.dto;

import com.adarsh.moviebooking.entity.Seat;

public class SeatResponse {

    private Long id;
    private String seatNumber;
    private String status;
    private Long showId;

    public SeatResponse(Seat seat) {
        this.id = seat.getId();
        this.seatNumber = seat.getSeatNumber();
        this.status = seat.getStatus().name();
        this.showId = seat.getShow() != null ? seat.getShow().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public Long getShowId() {
        return showId;
    }
}
