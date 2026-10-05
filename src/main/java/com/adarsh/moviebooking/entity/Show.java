package com.adarsh.moviebooking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_name", nullable = false)
    private String movieName;

    @Column(name = "show_time", nullable = false)
    private LocalDateTime showTime;

    @Column(name = "theatre_name", nullable = false)
    private String theatreName;

    public Show() {
    }

    public Show(String movieName, LocalDateTime showTime, String theatreName) {
        this.movieName = movieName;
        this.showTime = showTime;
        this.theatreName = theatreName;
    }

    public Long getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }
}
