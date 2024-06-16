package be.kuleuven.caffeinerestservice.service;

import be.kuleuven.caffeinerestservice.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Service
public class SnackService {

    @Autowired
    private SnackRepository snackRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional
    public boolean reserveSnack(String snackId, String reservationId) {
        reservationRepository.releaseExpiredReservations(snackRepository);
        boolean reserved = snackRepository.reserveSnack(snackId, reservationId);
        if (reserved) {
            reservationRepository.addReservation(snackId, reservationId);
            return true;
        } else {
            throw new RuntimeException("Could not reserve snack");
        }
    }

    @Transactional
    public boolean buyReservation(String reservationId) {
        reservationRepository.releaseExpiredReservations(snackRepository);
        boolean bought = reservationRepository.buyReservation(reservationId);
        if (!bought) {
            throw new RuntimeException("Reservation not found");
        }
        return true;
    }

    @Transactional(readOnly = true)
    public void  getStock() {
        reservationRepository.releaseExpiredReservations(snackRepository);
         snackRepository.printCurrentStock();
    }

    @Transactional(readOnly = true)
    public Map<String, Integer> findStock(String id) {
        reservationRepository.releaseExpiredReservations(snackRepository);
        return snackRepository.findStock(id);
    }

    @Transactional(readOnly = true)
    public Collection<Snack> getSnackOptions() {
        return snackRepository.getSnackOptions();
    }

    @Transactional(readOnly = true)
    public Optional<Snack> findSnack(String id) {
        return snackRepository.findSnack(id);
    }

    @Transactional(readOnly = true)
    public boolean checkReservation(String reservationId, String snackId) {
        reservationRepository.releaseExpiredReservations(snackRepository);
        for (Reservation res : reservationRepository.getReservations()) {
            if (res.getReservationId().equals(reservationId) && res.getSnackId().equals(snackId)) {
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean checkAvailability(String id) {
        reservationRepository.releaseExpiredReservations(snackRepository);
        return snackRepository.getStock().getOrDefault(id, 0) > 0;
    }

    @Transactional
    public void releaseReservation(String reservationId) {
        reservationRepository.releaseExpiredReservations(snackRepository);
        Iterator<Reservation> iterator = reservationRepository.getReservations().iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getReservationId().equals(reservationId)) {
                snackRepository.releaseReservation(reservation.getSnackId());
                iterator.remove();
            }
        }
    }

    @Transactional
    public void releaseExpiredReservations() {
        reservationRepository.releaseExpiredReservations(snackRepository);
    }

    public void getReserved() {
        System.out.println("Reserved list ");
        reservationRepository.releaseExpiredReservations(snackRepository);
        reservationRepository.getReservations().forEach(reservation -> {
            System.out.println("Reserved " + reservation.getSnackId() + " | Reservation ID: " + reservation.getReservationId() +" | Reservation MADE AT " + reservation.getTimestamp());
        });
    }
}
