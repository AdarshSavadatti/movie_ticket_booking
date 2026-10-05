package com.adarsh.moviebooking.service;

import com.adarsh.moviebooking.entity.Booking;
import com.adarsh.moviebooking.entity.BookingStatus;
import com.adarsh.moviebooking.entity.Seat;
import com.adarsh.moviebooking.entity.SeatStatus;
import com.adarsh.moviebooking.exception.SeatNotFoundException;
import com.adarsh.moviebooking.exception.SeatUnavailableException;
import com.adarsh.moviebooking.repository.BookingRepository;
import com.adarsh.moviebooking.repository.SeatRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatLockingService {


    private static final long HOLD_DURATION_SECONDS = 60;

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    public SeatLockingService(SeatRepository seatRepository, BookingRepository bookingRepository) {
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
    }


    @Transactional
    public Seat holdSeat(Long seatId) {
        Seat seat = seatRepository.findByIdForUpdate(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));

        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new SeatUnavailableException(
                    "Seat " + seatId + " is not available (current status: " + seat.getStatus() + ")");
        }

        seat.setStatus(SeatStatus.HELD);
        seat.setHoldExpiresAt(LocalDateTime.now().plusSeconds(HOLD_DURATION_SECONDS));


        return seat;
    }

    /**
     * Step 2 of booking: turn a HELD seat into a confirmed booking.
     */
    @Transactional
    public Booking confirmBooking(Long seatId, String customerName, String customerEmail) {
        Seat seat = seatRepository.findByIdForUpdate(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));

        if (seat.getStatus() != SeatStatus.HELD) {
            throw new SeatUnavailableException(
                    "Seat " + seatId + " cannot be confirmed (current status: " + seat.getStatus() + ")");
        }

        seat.setStatus(SeatStatus.BOOKED);
        seat.setHoldExpiresAt(null);

        Booking booking = new Booking(seat, customerName, customerEmail, BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }


    @Transactional
    public Seat releaseSeat(Long seatId) {
        Seat seat = seatRepository.findByIdForUpdate(seatId)
                .orElseThrow(() -> new SeatNotFoundException(seatId));

        if (seat.getStatus() != SeatStatus.HELD) {
            throw new SeatUnavailableException(
                    "Seat " + seatId + " is not currently held (current status: " + seat.getStatus() + ")");
        }

        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setHoldExpiresAt(null);
        return seat;
    }


    @Scheduled(fixedRate = 10000)
    @Transactional
    public void releaseExpiredHolds() {
        List<Seat> expiredSeats = seatRepository.findByStatusAndHoldExpiresAtBefore(
                SeatStatus.HELD, LocalDateTime.now());

        for (Seat seat : expiredSeats) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setHoldExpiresAt(null);
        }
    }
}
