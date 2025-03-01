package org.example.dao;

import org.example.entity.IngredientPriceHistory;
import org.example.entity.Ingredients;
import org.example.entity.StockMovement;
import org.example.entity.Unite;
import org.example.dbConnexion.dbConnexion;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IngredientsDAO {

    public Ingredients findById(int id) throws SQLException {
        String sql = "SELECT * FROM ingredients WHERE id = ?";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Ingredients(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("modification").toLocalDateTime(),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        Unite.valueOf(rs.getString("unite"))
                );
            }
            return null;
        }
    }

    public void insert(Ingredients ingredient) throws SQLException {
        String sql = "INSERT INTO ingredients (name, modification, quantity, unite) VALUES (?, ?, ?, ?::unite)";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ingredient.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(ingredient.getModification()));
            stmt.setInt(3, ingredient.getQuantity());
            stmt.setString(4, ingredient.getUnite().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insertion failed, no rows affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ingredient.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Insertion failed, no ID obtained.");
                }
            }
        }
    }

    // Méthode pour mettre à jour un ingrédient
    public void update(Ingredients ingredient) throws SQLException {
        String sql = "UPDATE ingredients SET name = ?, modification = ?, quantity = ?, unite = ?::unite WHERE id = ?"; // Cast ajouté
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ingredient.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(ingredient.getModification()));
            stmt.setInt(3, ingredient.getQuantity());
            stmt.setString(4, ingredient.getUnite().name());
            stmt.setInt(5, ingredient.getId());
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer un ingrédient
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ingredients WHERE id = ?";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Méthode pour récupérer tous les ingrédients
    public List<Ingredients> findAll() throws SQLException {
        String sql = "SELECT * FROM ingredients";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<Ingredients> ingredients = new ArrayList<>();

            while (rs.next()) {
                ingredients.add(new Ingredients(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("modification").toLocalDateTime(),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        Unite.valueOf(rs.getString("unite"))
                ));
            }
            return ingredients;
        }
    }

    // Méthode pour ajouter un prix à l'historique
    public void addPriceToHistory(int ingredientId, BigDecimal price, LocalDate date) throws SQLException {
        String sql = "INSERT INTO ingredient_price_history (ingredient_id, price, date) VALUES (?, ?, ?)";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ingredientId);
            stmt.setBigDecimal(2, price);
            stmt.setDate(3, Date.valueOf(date));
            stmt.executeUpdate();
        }
    }

    // Méthode pour récupérer le prix à une date donnée
    public BigDecimal getPriceAtDate(int ingredientId, LocalDate date) throws SQLException {
        String sql = "SELECT price FROM ingredient_price_history WHERE ingredient_id = ? AND date <= ? ORDER BY date DESC LIMIT 1";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ingredientId);
            stmt.setDate(2, Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("price");
                }
            }
        }
        return null;
    }

    // Méthode pour filtrer, trier et paginer les ingrédients
    public List<Ingredients> findFilteredAndPaginated(
            String nameFilter,
            Unite uniteFilter,
            Double minPrice,
            Double maxPrice,
            LocalDate startDate,
            LocalDate endDate,
            String sortBy,
            String sortOrder,
            int page,
            int pageSize) throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT * FROM ingredients WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Filtrage par nom
        if (nameFilter != null && !nameFilter.isEmpty()) {
            sql.append(" AND name ILIKE ?");
            params.add("%" + nameFilter + "%");
        }

        // Filtrage par unité
        if (uniteFilter != null) {
            sql.append(" AND unite = ?::unite");
            params.add(uniteFilter.name());
        }

        // Filtrage par intervalle de prix
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }

        // Filtrage par intervalle de date
        if (startDate != null) {
            sql.append(" AND modification >= ?");
            params.add(Timestamp.valueOf(startDate.atStartOfDay()));
        }
        if (endDate != null) {
            sql.append(" AND modification <= ?");
            params.add(Timestamp.valueOf(endDate.atTime(23, 59, 59)));
        }

        // Tri
        if (sortBy != null && !sortBy.isEmpty()) {
            sql.append(" ORDER BY ").append(sortBy);
            if (sortOrder != null && !sortOrder.isEmpty()) {
                sql.append(" ").append(sortOrder);
            }
        }

        // Pagination
        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Ajout des paramètres
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            List<Ingredients> ingredients = new ArrayList<>();

            while (rs.next()) {
                ingredients.add(new Ingredients(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("modification").toLocalDateTime(),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        Unite.valueOf(rs.getString("unite"))
                ));
            }
            return ingredients;
        }
    }

    // Méthode pour récupérer l'historique des prix d'un ingrédient
    public List<IngredientPriceHistory> getPriceHistory(int ingredientId) throws SQLException {
        String sql = "SELECT * FROM ingredient_price_history WHERE ingredient_id = ? ORDER BY date DESC";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ingredientId);
            ResultSet rs = stmt.executeQuery();
            List<IngredientPriceHistory> priceHistory = new ArrayList<>();

            while (rs.next()) {
                priceHistory.add(new IngredientPriceHistory(
                        rs.getInt("id"),
                        rs.getInt("ingredient_id"),
                        rs.getBigDecimal("price"),
                        rs.getDate("date").toLocalDate()
                ));
            }
            return priceHistory;
        }
    }

    // Méthode pour créer un nouvel enregistrement dans l'historique des prix
    public void createPriceHistory(IngredientPriceHistory priceHistory) throws SQLException {
        String sql = "INSERT INTO ingredient_price_history (ingredient_id, price, date) VALUES (?, ?, ?)";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, priceHistory.getIngredientId());
            stmt.setBigDecimal(2, priceHistory.getPrice());
            stmt.setDate(3, Date.valueOf(priceHistory.getDate()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insertion failed, no rows affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    priceHistory.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Insertion failed, no ID obtained.");
                }
            }
        }
    }
    public void addStockMovement(StockMovement movement) throws SQLException {
        String sql = "INSERT INTO stock_movement (ingredient_id, type, quantity, unite, movement_date) VALUES (?, ?, ?, ?::unite, ?)";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movement.getIngredientId());
            stmt.setString(2, movement.getType());
            stmt.setBigDecimal(3, movement.getQuantity());
            stmt.setString(4, movement.getUnite().name());
            stmt.setTimestamp(5, Timestamp.valueOf(movement.getMovementDate()));
            stmt.executeUpdate();
        }
    }

    // Méthode pour récupérer tous les mouvements de stock d'un ingrédient
    public List<StockMovement> getStockMovements(int ingredientId) throws SQLException {
        String sql = "SELECT * FROM stock_movement WHERE ingredient_id = ? ORDER BY movement_date DESC";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ingredientId);
            ResultSet rs = stmt.executeQuery();
            List<StockMovement> movements = new ArrayList<>();

            while (rs.next()) {
                movements.add(new StockMovement(
                        rs.getInt("id"),
                        rs.getInt("ingredient_id"),
                        rs.getString("type"),
                        rs.getBigDecimal("quantity"),
                        Unite.valueOf(rs.getString("unite")),
                        rs.getTimestamp("movement_date").toLocalDateTime()
                ));
            }
            return movements;
        }
    }

    public BigDecimal getCurrentStock(int ingredientId) throws SQLException {
        String sql = "SELECT SUM(CASE WHEN type = 'ENTRY' THEN quantity ELSE -quantity END) AS total " +
                "FROM stock_movement WHERE ingredient_id = ?";
        try (Connection conn = new dbConnexion().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ingredientId);
            ResultSet rs = stmt.executeQuery();

            BigDecimal total = rs.next() ? rs.getBigDecimal("total") : BigDecimal.ZERO;
            System.out.println("[DEBUG] Stock calculé pour ingredient_id=" + ingredientId + " : " + total);
            return total;
        }
    }
}