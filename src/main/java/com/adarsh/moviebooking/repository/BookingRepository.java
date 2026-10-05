package com.adarsh.moviebooking.repository;

import com.adarsh.moviebooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
