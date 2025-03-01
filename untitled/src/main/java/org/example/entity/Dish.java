package org.example.entity;

import org.example.dao.IngredientsDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dish {
    private int id;
    private String name;
    private Double price;
    private List<Ingredients> ingredients;

    public Dish(int id, String name, Double price, List<Ingredients> ingredients) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Dish name cannot be null.");
        this.price = Objects.requireNonNull(price, "Price cannot be null.");
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String namea) {
        this.name = Objects.requireNonNull(name, "Dish name cannot be null.");
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = Objects.requireNonNull(price, "Price cannot be null.");
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException("A dish must contain at least one ingredient.");
        }
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", ingredients=" + ingredients +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return id == dish.id &&
                Objects.equals(name, dish.name) &&
                Objects.equals(price, dish.price) &&
                Objects.equals(ingredients, dish.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, ingredients);
    }

    public BigDecimal getIngredientsCost(LocalDate date, IngredientsDAO ingredientsDAO) throws SQLException {
        BigDecimal totalCost = BigDecimal.ZERO;

        for (Ingredients ingredient : ingredients) {
            BigDecimal ingredientPrice = ingredientsDAO.getPriceAtDate(ingredient.getId(), date);
            if (ingredientPrice != null) {
                totalCost = totalCost.add(ingredientPrice.multiply(BigDecimal.valueOf(ingredient.getQuantity())));
            }
        }

        return totalCost;
    }

    public Double getGrossMargin(IngredientsDAO ingredientsDAO) throws SQLException {
        return getGrossMargin(ingredientsDAO, LocalDate.now());
    }

    public Double getGrossMargin(IngredientsDAO ingredientsDAO, LocalDate date) throws SQLException {
        BigDecimal ingredientsCost = getIngredientsCost(date, ingredientsDAO);
        BigDecimal sellingPrice = BigDecimal.valueOf(this.price);


        return sellingPrice.subtract(ingredientsCost).doubleValue();
    }
}