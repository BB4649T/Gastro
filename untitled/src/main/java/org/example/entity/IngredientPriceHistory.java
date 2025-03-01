package org.example.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class IngredientPriceHistory {
    private int id;
    private int ingredientId;
    private BigDecimal price;
    private LocalDate date;

    public IngredientPriceHistory() {
    }

    public IngredientPriceHistory(int id, int ingredientId, BigDecimal price, LocalDate date) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.price = price;
        this.date = date;
    }

    public IngredientPriceHistory(int ingredientId, BigDecimal price, LocalDate date) {
        this.ingredientId = ingredientId;
        this.price = price;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientPriceHistory that = (IngredientPriceHistory) o;
        return id == that.id &&
                ingredientId == that.ingredientId &&
                Objects.equals(price, that.price) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientId, price, date);
    }

    @Override
    public String toString() {
        return "IngredientPriceHistory{" +
                "id=" + id +
                ", ingredientId=" + ingredientId +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}