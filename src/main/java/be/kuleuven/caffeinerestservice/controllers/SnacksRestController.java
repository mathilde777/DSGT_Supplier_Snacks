package be.kuleuven.caffeinerestservice.controllers;

import be.kuleuven.caffeinerestservice.domain.Snack;
import be.kuleuven.caffeinerestservice.domain.SnackRepository;
import be.kuleuven.caffeinerestservice.exceptions.CodeNotCorrectException;
import be.kuleuven.caffeinerestservice.exceptions.DrinkNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/rest")
public class SnacksRestController {

    private final SnackRepository snackRepository;

    @Autowired
    SnacksRestController(SnackRepository snackRepository) {
        this.snackRepository = snackRepository;
    }

    @GetMapping("/snacks/{code}")
    public CollectionModel<EntityModel<Snack>> getAll(@PathVariable String code) {
        if (checkCode(code)) {
            Collection<Snack> snacks = snackRepository.getSnackOptions();
            List<EntityModel<Snack>> drinkEntityModels = new ArrayList<>();
            for (Snack d : snacks) {
                EntityModel<Snack> em = snackToEntityModel(d.getId(), d, code);
                drinkEntityModels.add(em);
            }
            return CollectionModel.of(drinkEntityModels,
                    linkTo(methodOn(SnacksRestController.class).getAll(code)).withSelfRel());
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @GetMapping("/snackId/{id}/{code}")
    public EntityModel<Snack> getItem(@PathVariable String id, @PathVariable String code) {
        if (checkCode(code)) {
            Snack d = snackRepository.findSnack(id).orElseThrow(() -> new DrinkNotFoundException(id));
            return snackToEntityModel(id, d, code);
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @GetMapping("/stock/{code}")
    public EntityModel<Map<String, Integer>> getAllStock(@PathVariable String code) {
        if (checkCode(code)) {
            Map<String, Integer> stock = snackRepository.getStock();
            return EntityModel.of(stock,
                    linkTo(methodOn(SnacksRestController.class).getAllStock(code)).withSelfRel());
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/snackID/{id}/{code}")
    public EntityModel<Map<String, Integer>> getStock(@PathVariable String id, @PathVariable String code) {
        if (checkCode(code)) {
            Map<String, Integer> stock = snackRepository.findStock(id);
            return EntityModel.of(stock,
                    linkTo(methodOn(SnacksRestController.class).getStock(id, code)).withSelfRel());
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/snackId/{id}/reserve/{reservationId}/{code}")
    public ResponseEntity<String> reserve(@PathVariable String id, @PathVariable String reservationId, @PathVariable String code) {
        if (checkCode(code)) {
            boolean success = snackRepository.reserveItem(id, reservationId);
            if (success) {
                Map<String, Integer> stock = snackRepository.findStock(id); // Get the updated stock
                String message = "Item with ID " + id + " reserved successfully. " + stock.values() + " left in stock.";
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reserve item with ID " + id);
            }
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/snackId/{reservationId}/buy/{code}")
    public ResponseEntity<String> buy(@PathVariable String reservationId, @PathVariable String code) {
        if (checkCode(code)) {
            boolean success = snackRepository.buyReservation(reservationId);
            if (success) {
                return ResponseEntity.ok("Item with reservation ID " + reservationId + " purchased successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to purchase item with reservation ID " + reservationId);
            }
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/snackId/{id}/checkReservation/{reservationId}/{code}")
    public ResponseEntity<Boolean> checkReservation(@PathVariable String id, @PathVariable String reservationId, @PathVariable String code) {
        if (checkCode(code)) {
            boolean exists = snackRepository.checkReservation(reservationId, id);
            return ResponseEntity.ok(exists);
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/snackId/{id}/checkAvailability/{code}")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable String id, @PathVariable String code) {
        if (checkCode(code)) {
            boolean available = snackRepository.checkAvailability(id);
            return ResponseEntity.ok(available);
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/getReserved/{code}")
    public ResponseEntity<String> getReserved(@PathVariable String code) {
        if (checkCode(code)) {
            snackRepository.getReserved();
            return ResponseEntity.ok("Reserved items have been printed to the console.");
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    private boolean checkCode(String code) {
        System.out.println(code);
        String verificationCode = "1234";
        return code.equals(verificationCode);
    }

    private EntityModel<Snack> snackToEntityModel(String id, Snack snack, String code) {
        return EntityModel.of(snack,
                linkTo(methodOn(SnacksRestController.class).getAll(code)).withRel("rest/snacks"),
                linkTo(methodOn(SnacksRestController.class).getItem(id, code)).withRel("rest/snackId/{id}"));
    }
}

