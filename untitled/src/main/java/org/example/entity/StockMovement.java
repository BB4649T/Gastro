package org.example.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class StockMovement {
    private int id;
    private int ingredientId;
    private String type; // "ENTRY" ou "EXIT"
    private BigDecimal quantity;
    private Unite unite;
    private LocalDateTime movementDate;

    public StockMovement(int id, int ingredientId, String type, BigDecimal quantity, Unite unite, LocalDateTime movementDate) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.type = Objects.requireNonNull(type, "Type cannot be null.");
        this.quantity = Objects.requireNonNull(quantity, "Quantity cannot be null.");
        this.unite = Objects.requireNonNull(unite, "Unite cannot be null.");
        this.movementDate = Objects.requireNonNull(movementDate, "Movement date cannot be null.");
    }

    // Getters et setters
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", ingredientId=" + ingredientId +
                ", type='" + type + '\'' +
                ", quantity=" + quantity +
                ", unite=" + unite +
                ", movementDate=" + movementDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockMovement that = (StockMovement) o;
        return id == that.id &&
                ingredientId == that.ingredientId &&
                Objects.equals(type, that.type) &&
                Objects.equals(quantity, that.quantity) &&
                unite == that.unite &&
                Objects.equals(movementDate, that.movementDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientId, type, quantity, unite, movementDate);
    }
}
