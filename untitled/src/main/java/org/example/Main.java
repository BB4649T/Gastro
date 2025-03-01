package org.example;

import org.example.dao.dishDAO;
import org.example.dao.IngredientsDAO;
import org.example.entity.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            IngredientsDAO ingredientsDAO = new IngredientsDAO();
            dishDAO dishDAO = new dishDAO();

            // Insertion des ingrédients
            Ingredients tomato = new Ingredients(0, "Tomato", LocalDateTime.now(), 4.00, 100, Unite.Gramme);
            ingredientsDAO.insert(tomato);
            System.out.println("Tomato inséré avec l'ID : " + tomato.getId());

            Ingredients chicken = new Ingredients(0, "Chicken", LocalDateTime.now(), 5.00, 50, Unite.Gramme);
            ingredientsDAO.insert(chicken);
            System.out.println("Chicken inséré avec l'ID : " + chicken.getId());

            // Ajout des prix historiques
            ingredientsDAO.addPriceToHistory(tomato.getId(), BigDecimal.valueOf(4.00), LocalDate.now().minusDays(7));
            ingredientsDAO.addPriceToHistory(tomato.getId(), BigDecimal.valueOf(6.00), LocalDate.now());

            ingredientsDAO.addPriceToHistory(chicken.getId(), BigDecimal.valueOf(5.00), LocalDate.now().minusDays(7));
            ingredientsDAO.addPriceToHistory(chicken.getId(), BigDecimal.valueOf(7.00), LocalDate.now());

            // Création d'un plat
            Dish tomatoSoup = new Dish(0, "Tomato Soup", BigDecimal.valueOf(5.99).doubleValue(), new ArrayList<>());
            dishDAO.insert(tomatoSoup);
            System.out.println("Tomato Soup inséré avec l'ID : " + tomatoSoup.getId());

            // Ajout des ingrédients au plat
            dishDAO.addIngredientToDish(tomatoSoup.getId(), tomato.getId());
            dishDAO.addIngredientToDish(tomatoSoup.getId(), chicken.getId());

            // Récupération des détails du plat
            Dish retrievedDish = dishDAO.findById(tomatoSoup.getId());
            System.out.println("Détails du plat : " + retrievedDish);

            // Récupération de tous les ingrédients
            List<Ingredients> allIngredients = ingredientsDAO.findAll();
            System.out.println("Liste des ingrédients :");
            for (Ingredients ingredient : allIngredients) {
                System.out.println(ingredient);
            }

            // Récupération de tous les plats
            List<Dish> allDishes = dishDAO.findAll();
            System.out.println("Liste des plats :");
            for (Dish dish : allDishes) {
                System.out.println(dish);
            }

            // Gestion des stocks
            // Ajout de mouvements de stock pour les ingrédients
            StockMovement tomatoStockEntry = new StockMovement(
                    0, // ID sera généré automatiquement
                    tomato.getId(), // ID de l'ingrédient
                    "ENTRY", // Type de mouvement (entrée)
                    BigDecimal.valueOf(100), // Quantité
                    Unite.Gramme, // Unité
                    LocalDateTime.now() // Date et heure du mouvement
            );
            ingredientsDAO.addStockMovement(tomatoStockEntry);
            System.out.println("Mouvement de stock pour la tomate ajouté.");

            StockMovement chickenStockEntry = new StockMovement(
                    0, // ID sera généré automatiquement
                    chicken.getId(), // ID de l'ingrédient
                    "ENTRY", // Type de mouvement (entrée)
                    BigDecimal.valueOf(50), // Quantité
                    Unite.Gramme, // Unité
                    LocalDateTime.now() // Date et heure du mouvement
            );
            ingredientsDAO.addStockMovement(chickenStockEntry);
            System.out.println("Mouvement de stock pour le poulet ajouté.");

            // Récupération des mouvements de stock pour un ingrédient
            List<StockMovement> tomatoStockMovements = ingredientsDAO.getStockMovements(tomato.getId());
            System.out.println("Mouvements de stock pour la tomate :");
            for (StockMovement movement : tomatoStockMovements) {
                System.out.println(movement);
            }

            // Calcul du stock actuel d'un ingrédient
            BigDecimal tomatoCurrentStock = ingredientsDAO.getCurrentStock(tomato.getId());
            System.out.println("Stock actuel de la tomate : " + tomatoCurrentStock);

            BigDecimal chickenCurrentStock = ingredientsDAO.getCurrentStock(chicken.getId());
            System.out.println("Stock actuel du poulet : " + chickenCurrentStock);

            // Création d'un nouvel enregistrement d'historique de prix
            IngredientPriceHistory priceHistory = new IngredientPriceHistory(
                    0, // ID sera généré automatiquement
                    tomato.getId(), // ID de l'ingrédient
                    BigDecimal.valueOf(5.00), // Prix
                    LocalDate.now() // Date
            );

            // Insertion dans la base de données
            ingredientsDAO.createPriceHistory(priceHistory);
            System.out.println("Nouvel enregistrement d'historique de prix inséré avec l'ID : " + priceHistory.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}