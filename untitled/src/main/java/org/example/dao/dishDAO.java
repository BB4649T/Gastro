package org.example.dao;

import org.example.entity.Dish;
import org.example.entity.Unite;
import org.example.entity.Ingredients;
import org.example.dbConnexion.dbConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dishDAO {
    public void addIngredientToDish(int dishId, int ingredientId) throws SQLException {
        String sql = "INSERT INTO dish_ingredients (dish_id, ingredient_id) VALUES (?, ?)";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dishId);
            stmt.setInt(2, ingredientId);
            stmt.executeUpdate();
        }
    }
    public Dish findById(int id) throws SQLException {
        String sql = "SELECT d.id AS dish_id, d.name AS dish_name, d.price AS dish_price, " +
                "i.id AS ingredient_id, i.name AS ingredient_name, i.modification, i.price AS ingredient_price, " +
                "i.quantity, i.unite " +
                "FROM dish d " +
                "JOIN dish_ingredients di ON d.id = di.dish_id " +
                "JOIN ingredients i ON di.ingredient_id = i.id " +
                "WHERE d.id = ?";

        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            Dish dish = null;
            List<Ingredients> ingredients = new ArrayList<>();

            while (rs.next()) {
                if (dish == null) {
                    dish = new Dish(
                            rs.getInt("dish_id"),
                            rs.getString("dish_name"),
                            rs.getDouble("dish_price"),
                            ingredients
                    );
                }

                Ingredients ingredient = new Ingredients(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getTimestamp("modification").toLocalDateTime().toLocalDate(),
                        rs.getDouble("ingredient_price"),
                        rs.getInt("quantity"),
                        Unite.valueOf(rs.getString("unite"))
                );
                ingredients.add(ingredient);
            }

            return dish;
        }
    }

    public void insert(Dish dish) throws SQLException {
        String sql = "INSERT INTO dish (name, price) VALUES (?, ?)";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dish.getName());
            stmt.setDouble(2, dish.getPrice());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                dish.setId(rs.getInt(1));
            }
            insertDishIngredients(dish);
        }
    }

    private void insertDishIngredients(Dish dish) throws SQLException {
        String sql = "INSERT INTO dish_ingredients (dish_id, ingredient_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Ingredients ingredient : dish.getIngredients()) {
                stmt.setInt(1, dish.getId());
                stmt.setInt(2, ingredient.getId());
                stmt.setInt(3, ingredient.getQuantity());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public void update(Dish dish) throws SQLException {
        String sql = "UPDATE dish SET name = ?, price = ? WHERE id = ?";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dish.getName());
            stmt.setDouble(2, dish.getPrice());
            stmt.setInt(3, dish.getId());
            stmt.executeUpdate();

            deleteDishIngredients(dish.getId());
            insertDishIngredients(dish);
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM dish WHERE id = ?";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private void deleteDishIngredients(int dishId) throws SQLException {
        String sql = "DELETE FROM dish_ingredients WHERE dish_id = ?";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dishId);
            stmt.executeUpdate();
        }
    }
    public List<Dish> findAll() throws SQLException {
        String sql = "SELECT d.id AS dish_id, d.name AS dish_name, d.price AS dish_price, " +
                "i.id AS ingredient_id, i.name AS ingredient_name, i.modification, i.price AS ingredient_price, " +
                "i.quantity, i.unite " +
                "FROM dish d " +
                "LEFT JOIN dish_ingredients di ON d.id = di.dish_id " +
                "LEFT JOIN ingredients i ON di.ingredient_id = i.id";

        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            Map<Integer, Dish> dishMap = new HashMap<>();
            while (rs.next()) {
                int dishId = rs.getInt("dish_id");

                Dish dish = dishMap.get(dishId);
                if (dish == null) {
                    dish = new Dish(
                            dishId,
                            rs.getString("dish_name"),
                            rs.getDouble("dish_price"),
                            new ArrayList<>()
                    );
                    dishMap.put(dishId, dish);
                }

                int ingredientId = rs.getInt("ingredient_id");
                if (!rs.wasNull()) {
                    Ingredients ingredient = new Ingredients(
                            ingredientId,
                            rs.getString("ingredient_name"),
                            rs.getTimestamp("modification").toLocalDateTime().toLocalDate(),
                            rs.getDouble("ingredient_price"),
                            rs.getInt("quantity"),
                            Unite.valueOf(rs.getString("unite"))
                    );
                    dish.getIngredients().add(ingredient);
                }
            }
            return new ArrayList<>(dishMap.values());
        }
    }

}