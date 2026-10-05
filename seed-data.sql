-- Run this manually in MySQL Workbench (or your MySQL client) AFTER the app
-- has started at least once (so the shows/seats/bookings tables already exist).
--
-- Step 1: insert a show, then check its real ID:
INSERT INTO shows (movie_name, show_time, theatre_name)
VALUES ('Test Movie', '2026-09-25 18:00:00', 'Test Theatre');

SELECT * FROM shows;
-- Note the "id" value of the row you just inserted (likely 1 on a fresh DB).

-- Step 2: insert a seat for that show. Replace the "1" below with the show's
-- actual id from Step 1 if it wasn't 1:
INSERT INTO seats (seat_number, status, show_id)
VALUES ('A1', 'AVAILABLE', 1);

SELECT * FROM seats;
-- Note the "id" value of the seat you just inserted - that's the seatId to
-- use in Postman or in ConcurrencyTestSimple.java's BODY field.
