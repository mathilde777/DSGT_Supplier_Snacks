package be.kuleuven.caffeinerestservice.controllers;

import be.kuleuven.caffeinerestservice.domain.Snack;
import be.kuleuven.caffeinerestservice.service.SnackService;
import be.kuleuven.caffeinerestservice.exceptions.CodeNotCorrectException;
import be.kuleuven.caffeinerestservice.exceptions.DrinkNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/rest")
public class SnacksRestController {

    private final SnackService snackService;

    @Autowired
    SnacksRestController(SnackService snackService) {
        this.snackService = snackService;
    }

    @GetMapping("/items/{code}")
    public CollectionModel<EntityModel<Snack>> getAll(@PathVariable String code) {
        if (checkCode(code)) {
            List<Snack> snacks = snackService.getSnackOptions().stream().toList();
            List<EntityModel<Snack>> snackEntityModels = snacks.stream()
                    .map(snack -> snackToEntityModel(snack.getId(), snack, code))
                    .toList();
            return CollectionModel.of(snackEntityModels,
                    linkTo(methodOn(SnacksRestController.class).getAll(code)).withSelfRel());
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @GetMapping("/itemId/{id}/{code}")
    public EntityModel<Snack> getItem(@PathVariable String id, @PathVariable String code) {
        if (checkCode(code)) {
            Snack snack = snackService.findSnack(id).orElseThrow(() -> new DrinkNotFoundException(id));
            return snackToEntityModel(id, snack, code);
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @GetMapping("/stock/{code}")
    public EntityModel<Map<String, Integer>> getAllStock(@PathVariable String code) {
        if (checkCode(code)) {
            Map<String, Integer> stock = snackService.getStock();
            return EntityModel.of(stock,
                    linkTo(methodOn(SnacksRestController.class).getAllStock(code)).withSelfRel());
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/itemID/{id}/stock/{code}")
    public EntityModel<Map<String, Integer>> getStock(@PathVariable String id, @PathVariable String code) {
        if (checkCode(code)) {
            Map<String, Integer> stock = snackService.findStock(id);
            return EntityModel.of(stock,
                    linkTo(methodOn(SnacksRestController.class).getStock(id, code)).withSelfRel());
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/itemId/{id}/reserve/{reservationId}/{code}")
    public ResponseEntity<String> reserveSnack(@PathVariable String id, @PathVariable String reservationId, @PathVariable String code) {
        if (checkCode(code)) {
            boolean success = snackService.reserveSnack(id, reservationId);
            if (success) {
                Map<String, Integer> stock = snackService.findStock(id); // Get the updated stock
                String message = "Snack with ID " + id + " reserved successfully. " + stock.values() + " left in stock.";
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reserve snack with ID " + id);
            }
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/buy/{reservationId}/{code}")
    public ResponseEntity<String> buySnack(@PathVariable String reservationId, @PathVariable String code) {
        if (checkCode(code)) {
            boolean success = snackService.buyReservation(reservationId);
            if (success) {
                return ResponseEntity.ok("Snack with reservation ID " + reservationId + " purchased successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to purchase snack with reservation ID " + reservationId);
            }
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/itemId/{id}/checkReservation/{reservationId}/{code}")
    public ResponseEntity<Boolean> checkReservation(@PathVariable String id, @PathVariable String reservationId, @PathVariable String code) {
        if (checkCode(code)) {
            boolean exists = snackService.checkReservation(reservationId, id);
            return ResponseEntity.ok(exists);
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/releaseReservation/{reservationId}/{code}")
    public void releaseReservation(@PathVariable String reservationId, @PathVariable String code) {
        if (checkCode(code)) {
            snackService.releaseReservation(reservationId);
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/itemId/{id}/checkAvailability/{code}")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable String id, @PathVariable String code) {
        if (checkCode(code)) {
            boolean available = snackService.checkAvailability(id);
            return ResponseEntity.ok(available);
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    @PostMapping("/getReserved/{code}")
    public ResponseEntity<String> getReserved(@PathVariable String code) {
        if (checkCode(code)) {
            snackService.getReserved();
            return ResponseEntity.ok("Reserved snacks have been printed to the console.");
        } else {
            throw new CodeNotCorrectException(code);
        }
    }

    private boolean checkCode(String code) {
        String verificationCode = "1234";
        return code.equals(verificationCode);
    }

    private EntityModel<Snack> snackToEntityModel(String id, Snack snack, String code) {
        return EntityModel.of(snack,
                linkTo(methodOn(SnacksRestController.class).getAll(code)).withRel("rest/snacks"),
                linkTo(methodOn(SnacksRestController.class).getItem(id, code)).withRel("rest/snackId/{id}"));
    }
}
