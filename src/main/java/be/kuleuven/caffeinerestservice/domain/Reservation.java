package be.kuleuven.caffeinerestservice.domain;

import java.time.Instant;

public class Reservation {
    private final String snackId;
    private final Instant timestamp;
    private final String reservationId;


    public Reservation(String itemId, Instant timestamp, String resId) {
        this.snackId = itemId;
        this.timestamp = timestamp;
        this. reservationId = resId;

    }

    public String getSnackId() {
        return snackId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
    public String getReservationId() {
        return reservationId;
    }

}
