# Movie Ticket Booking System — Concurrent Seat Locking

Clean rebuild of the project, same package/table names/endpoints as before,
with the seat-locking logic fixed so it no longer throws
"Could not commit JPA transaction" under concurrent requests.

## What changed vs. the old version

- `SeatRepository.findByIdForUpdate()` now uses Spring Data's `@Lock(LockModeType.PESSIMISTIC_WRITE)`
  annotation instead of a hand-written native `SELECT ... FOR UPDATE` query.
  Hibernate needs to know a pessimistic lock is being taken through its own
  API so its internal version/lock bookkeeping stays consistent — a raw
  native query bypasses that bookkeeping, which is what was causing the
  transaction commit failure.
- `SeatLockingService.holdSeat()` now explicitly checks
  `seat.getStatus() != SeatStatus.AVAILABLE` **after** acquiring the lock,
  and throws a clean `SeatUnavailableException` if someone already grabbed
  the seat. Without this check, a thread could acquire the lock late and
  still blindly overwrite a seat that was already held.
- Added `GlobalExceptionHandler` (`@RestControllerAdvice`) so:
  - Seat already held/booked → `409 Conflict` with a clear message
  - Seat ID doesn't exist → `404 Not Found`
  - Invalid request body → `400 Bad Request`
  - Anything truly unexpected → `500`, but with the real exception message
    in the response body instead of a generic one

## Setup

1. **Extract this zip** and open the folder in IntelliJ as a Maven project
   (`File → Open`, select the folder, let it import).
2. **Set your MySQL password** in `src/main/resources/application.properties`:
   ```
   spring.datasource.password=yourpassword
   ```
3. **Make sure MySQL is running** locally on port 3306.
4. **Run `MovieBookingApplication.main()`**. On first run it creates the
   `moviebooking` database and the `shows`, `seats`, `bookings` tables
   automatically.
5. **Seed one show and one seat** — open `seed-data.sql` (in the project
   root) in MySQL Workbench and run it statement by statement, noting the
   real seat ID it creates.
6. **Update the seat ID** in `ConcurrencyTestSimple.java`'s `BODY` field to
   match what you seeded.
7. **With the app still running**, run `ConcurrencyTestSimple.main()`
   separately (it opens its own console tab). You should now see:
   - Exactly 1 request with `status code: 200`
   - The other 19 with `status code: 409` and a body like
     `"Seat 1 is not available (current status: HELD)"`

That 1-success/19-conflict result, with real 409s instead of 500s, is your
proof the locking logic works correctly under concurrency — safe to use as
the evidence number in your resume bullet.

## Endpoints

- `POST /api/seats/hold` — body `{"seatId": 1}` — holds a seat for 60 seconds
- `POST /api/seats/confirm` — body `{"seatId": 1, "customerName": "...", "customerEmail": "..."}` — confirms a held seat
- `POST /api/seats/release` — body `{"seatId": 1}` — releases a held seat early

A background job runs every 10 seconds and automatically releases any seat
that's been `HELD` longer than 60 seconds without being confirmed.
