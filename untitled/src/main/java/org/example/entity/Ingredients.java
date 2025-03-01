package org.example.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Ingredients {
    private int id;
    private String name;
    private LocalDateTime modification;
    private Double price;
    private int quantity;
    private Unite unite;

    public Ingredients(int id, String name, LocalDateTime modification, Double price, int quantity, Unite unite) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Ingredient name cannot be null.");
        this.modification = Objects.requireNonNull(modification, "Modification date cannot be null.");
        this.price = price;
        this.quantity = quantity;
        this.unite = Objects.requireNonNull(unite, "Unit cannot be null.");
    }

    public Ingredients(int id, String name, LocalDate modification, double price, int quantity, Unite unite) {
        this(id, name, modification.atStartOfDay(), Double.valueOf(price), quantity, unite);
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Ingredient name cannot be null.");
    }

    public LocalDateTime getModification() {
        return modification;
    }

    public void setModification(LocalDateTime modification) {
        this.modification = Objects.requireNonNull(modification, "Modification date cannot be null.");
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = Objects.requireNonNull(unite, "Unit cannot be null.");
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", modification=" + modification +
                ", price=" + price +
                ", quantity=" + quantity +
                ", unite=" + unite +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredients that = (Ingredients) o;
        return id == that.id &&
                quantity == that.quantity &&
                Objects.equals(name, that.name) &&
                Objects.equals(modification, that.modification) &&
                Objects.equals(price, that.price) &&
                unite == that.unite;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modification, price, quantity, unite);
    }
}