package com.adarsh.moviebooking.repository;

import com.adarsh.moviebooking.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {
}
