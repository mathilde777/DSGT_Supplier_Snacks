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
        Snack n1 = new Snack("Ndjb0HZE6s3uxnAryOqA", "Nalu Original",
                "An amazing new taste of Nalu Drinks with less calories and a boost of energy.",
                Brand.AIKI, 250.0, 50, 1.08, 2);

        Snack n2 = new Snack("ep3FAfq9y06EYXx14Mt0", "Nalu Passion",
                "Introducing Nalu Passion, a refreshing, fruity blend of blueberry, pear, blackberry, peach, açai, Goji berries and mangosteen for the most passionate among us.",
                Brand.LAYS, 250.0, 50, 1.41, 1);



        snacks.put(n1.getId(), n1);
        stock.put(n1.getId(), n1.getStock());

        snacks.put(n2.getId(), n2);
        stock.put(n2.getId(), n2.getStock());


    }

    public Map<String, Integer> getStock() {
        return new HashMap<>(stock);
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
