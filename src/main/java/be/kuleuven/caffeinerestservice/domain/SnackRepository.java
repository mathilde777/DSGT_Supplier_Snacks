package be.kuleuven.caffeinerestservice.domain;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SnackRepository {
    private final Map<String, Snack> snacks = new HashMap<>();
    private final Map<String, Integer> stock = new HashMap<>();

    @PostConstruct
    public void initData() {
        Snack n1 = new Snack("qw1Bb1rjrEM2bDJfo6W3", "Aiki Chicken",
                "Aïki chicken, the winner among noodles in a handy cup to take with you. Add boiling water, wait 3 minutes and you're done!",
                Brand.AIKI, 250.0, 50, 2.48, 20);

        Snack n2 = new Snack("W3ckU0jMdgjM0X0QK3xL", "Lay's Naturel",
                "Delicious potatoes, crispy fried with a little salt, a true classic in its iconic red bag.",
                Brand.LAYS, 250.0, 50, 3.78, 20);

        Snack n3 = new Snack("zUCsIhTuwLjgP2o329jX", "Aiki Hot & Spicy",
                "Aïki, the winner among noodles in a handy cup to take with you. Add boiling water, wait 3 minutes and you're done!",
                Brand.AIKI, 250.0, 50, 2.09, 20);

        Snack n4 = new Snack("2SvJNCAcCfjfnuVADpr2", "Lay's Paprika",
                "A bit sweet and nicely spiced, a favorite for years.",
                Brand.LAYS, 250.0, 50, 2.99, 20);

        snacks.put(n1.getId(), n1);
        stock.put(n1.getId(), n1.getStock());

        snacks.put(n2.getId(), n2);
        stock.put(n2.getId(), n2.getStock());

        snacks.put(n3.getId(), n3);
        stock.put(n3.getId(), n3.getStock());

        snacks.put(n4.getId(), n4);
        stock.put(n4.getId(), n4.getStock());


    }

    public Map<String, Integer> getStock() {
        return new HashMap<>(stock);
    }
    public void printCurrentStock() {
        System.out.println("Current stock list:");
        stock.forEach((snackId, quantity) -> {
            String snackName = snacks.get(snackId).getName();
            System.out.println("Snack: " + snackName + " | Stock: " + quantity);
        });
    }

    public Map<String, Integer> findStock(String id) {
        int quantity = stock.getOrDefault(id, 0);
        Snack snack = findSnack(id).orElseThrow(() -> new RuntimeException("Snack not found"));
        Map<String, Integer> result = new HashMap<>();
        result.put(snack.getName(), quantity);
        return result;
    }

    public Collection<Snack> getSnackOptions() {
        return snacks.values();
    }

    public Optional<Snack> findSnack(String id) {
        return Optional.ofNullable(snacks.get(id));
    }

    public boolean reserveSnack(String id, String reservationId) {
        if (stock.containsKey(id) && stock.get(id) > 0) {
            stock.put(id, stock.get(id) - 1);
            return true;
        } else {
            throw new RuntimeException("Snack not in stock");
        }
    }

    public void releaseReservation(String id) {
        stock.put(id, stock.getOrDefault(id, 0) + 1);
    }
}
