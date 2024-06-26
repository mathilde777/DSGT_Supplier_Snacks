package be.kuleuven.caffeinerestservice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;


public class Snack {

    private String id;
    private String name;
    private String description;
    private Brand brand;
    private double weight;
    private int kcal;
    private double price;
    private int stock; // Add stock field

    public Snack() {}

    public Snack(String id, String name, String description, Brand brand, double weight, int kcal, double price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.weight = weight;
        this.kcal = kcal;
        this.price = price;
        this.stock = stock;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public int getKcal() { return kcal; }
    public void setKcal(int kcal) { this.kcal = kcal; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
