About This Project

I built this backend to solve one problem I kept noticing in simple booking apps: what happens when two people try to book the same seat at the same time?

In most basic projects, both requests see the seat as available and both succeed, so the seat gets double-booked. I wanted to handle this properly instead of ignoring it.

Built with Java, Spring Boot, MySQL and REST APIs.


How It Works

1. When a user selects a seat, I hold it for them temporarily.
2. If they don't confirm in time, a scheduled job releases the seat so someone else can book it.
3. If they confirm in time, the hold becomes a real booking.


How I Prevent Double-Booking

I used two layers of protection:

- Row-level locking (SELECT ... FOR UPDATE): while one request is working on a seat, the database makes other requests wait for that seat.
- Optimistic locking (JPA @Version): every seat has a version number. If two requests try to update the same version, only the first one succeeds and the other fails safely.

I used both because they cover different cases. Row locking stops the conflict upfront, and versioning is a safety net if something slips through.


How It Is Better Than Basic Booking Systems

- Basic apps check "is the seat free?" and then book it, which creates a race condition. Mine locks the seat at the database level during the operation.
- In basic apps, two users can end up with the same seat. In mine, only one request wins and the other gets a clean failure.
- In basic apps, a seat can stay stuck if the user leaves midway. In mine, holds expire automatically.
- Basic apps use one layer of protection or none. Mine uses two.


Database Design

- Shows: movie and show timing
- Seats: every seat linked to a show
- Bookings: which user booked which seat


REST APIs

- Lock (hold) a seat
- Confirm a booking
- Release a seat

I tested the APIs using Postman.


What I Learned

- How race conditions actually happen in backend code
- When to use pessimistic vs optimistic locking
- How to build a hold-and-expire flow using scheduled jobs


Tech Stack

Java, Spring Boot, Spring Data JPA, MySQL, REST, Postman


Run Locally

1. Clone the repo
2. Create a MySQL database and update application.properties with your username and password
3. Run: mvn spring-boot:run
4. Test the APIs using Postman


Future Improvements

- Payment integration
- Redis for faster seat holds
- User login with JWT
