package be.kuleuven.caffeinerestservice.domain;

public enum Brand {

    AIKI("aiki"),
    LAYS("lays");

    private final String value;

    Brand(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Brand fromValue(String v) {
        for (Brand c: Brand.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}