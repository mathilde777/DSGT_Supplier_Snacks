package be.kuleuven.caffeinerestservice.domain;

import java.time.Instant;


public class Reservation {


    private String reservationId;
    private String snackId;
    private Instant timestamp;


    public Reservation() {}
    public Reservation(String reservationId, String snackId, Instant timestamp) {
        this.reservationId = reservationId;
        this.snackId = snackId;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getSnackId() {
        return snackId;
    }

    public void setSnackId(String snackId) {
        this.snackId = snackId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
