package com.adarsh.moviebooking.controller;

import com.adarsh.moviebooking.dto.ConfirmBookingRequest;
import com.adarsh.moviebooking.dto.LockSeatRequest;
import com.adarsh.moviebooking.dto.SeatResponse;
import com.adarsh.moviebooking.entity.Booking;
import com.adarsh.moviebooking.service.SeatLockingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatLockingService seatLockingService;

    public SeatController(SeatLockingService seatLockingService) {
        this.seatLockingService = seatLockingService;
    }

    @PostMapping("/hold")
    public ResponseEntity<SeatResponse> holdSeat(@Valid @RequestBody LockSeatRequest request) {
        return ResponseEntity.ok(new SeatResponse(seatLockingService.holdSeat(request.getSeatId())));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Booking> confirmBooking(@Valid @RequestBody ConfirmBookingRequest request) {
        Booking booking = seatLockingService.confirmBooking(
                request.getSeatId(), request.getCustomerName(), request.getCustomerEmail());
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/release")
    public ResponseEntity<SeatResponse> releaseSeat(@Valid @RequestBody LockSeatRequest request) {
        return ResponseEntity.ok(new SeatResponse(seatLockingService.releaseSeat(request.getSeatId())));
    }
}
