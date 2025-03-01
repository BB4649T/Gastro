import org.example.entity.Dish;
import org.example.entity.Ingredients;
import org.example.entity.Unite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.dao.IngredientsDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DishTest {

    private IngredientsDAO ingredientsDAO;
    private Dish hotDog;

    @BeforeEach
    void setUp() {
        ingredientsDAO = new IngredientsDAO();
        hotDog = new Dish(1, "HotDog", 10.00, new ArrayList<>());
    }

    @Test
    void testGetIngredientsCostAtCurrentDate() throws Exception {
        Ingredients sausage = new Ingredients(0, "Sausage", LocalDateTime.now(), 5.00, 1000, Unite.Gramme);
        Ingredients bread = new Ingredients(0, "Bread", LocalDateTime.now(), 1.00, 500, Unite.Gramme);
        Ingredients mustard = new Ingredients(0, "Mustard", LocalDateTime.now(), 0.50, 100, Unite.Gramme);
        ingredientsDAO.insert(sausage);
        ingredientsDAO.insert(bread);
        ingredientsDAO.insert(mustard);

        LocalDate today = LocalDate.now();
        ingredientsDAO.addPriceToHistory(sausage.getId(), BigDecimal.valueOf(5.00), today);
        ingredientsDAO.addPriceToHistory(bread.getId(), BigDecimal.valueOf(1.00), today);
        ingredientsDAO.addPriceToHistory(mustard.getId(), BigDecimal.valueOf(0.50), today);

        hotDog.getIngredients().add(sausage);
        hotDog.getIngredients().add(bread);
        hotDog.getIngredients().add(mustard);

        BigDecimal costToday = hotDog.getIngredientsCost(today, ingredientsDAO);

        assertEquals(BigDecimal.valueOf(5550.00).setScale(2), costToday.setScale(2));
    }

    @Test
    void testGetIngredientsCostAtPastDate() throws Exception {
        Ingredients sausage = new Ingredients(0, "Sausage", LocalDateTime.now(), 5.00, 1000, Unite.Gramme);
        Ingredients bread = new Ingredients(0, "Bread", LocalDateTime.now(), 1.00, 500, Unite.Gramme);
        Ingredients mustard = new Ingredients(0, "Mustard", LocalDateTime.now(), 0.50, 100, Unite.Gramme);
        ingredientsDAO.insert(sausage);
        ingredientsDAO.insert(bread);
        ingredientsDAO.insert(mustard);

        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusDays(7);
        ingredientsDAO.addPriceToHistory(sausage.getId(), BigDecimal.valueOf(4.00), lastWeek);
        ingredientsDAO.addPriceToHistory(bread.getId(), BigDecimal.valueOf(0.80), lastWeek);
        ingredientsDAO.addPriceToHistory(mustard.getId(), BigDecimal.valueOf(0.30), lastWeek);

        ingredientsDAO.addPriceToHistory(sausage.getId(), BigDecimal.valueOf(5.00), today);
        ingredientsDAO.addPriceToHistory(bread.getId(), BigDecimal.valueOf(1.00), today);
        ingredientsDAO.addPriceToHistory(mustard.getId(), BigDecimal.valueOf(0.50), today);

        hotDog.getIngredients().add(sausage);
        hotDog.getIngredients().add(bread);
        hotDog.getIngredients().add(mustard);

        BigDecimal costLastWeek = hotDog.getIngredientsCost(lastWeek, ingredientsDAO);

        assertEquals(BigDecimal.valueOf(4430.00).setScale(2), costLastWeek.setScale(2));
    }

    @Test
    void testGetGrossMarginAtCurrentDate() throws Exception {
        Ingredients sausage = new Ingredients(0, "Sausage", LocalDateTime.now(), 5.00, 1000, Unite.Gramme);
        Ingredients bread = new Ingredients(0, "Bread", LocalDateTime.now(), 1.00, 500, Unite.Gramme);
        Ingredients mustard = new Ingredients(0, "Mustard", LocalDateTime.now(), 0.50, 100, Unite.Gramme);
        ingredientsDAO.insert(sausage);
        ingredientsDAO.insert(bread);
        ingredientsDAO.insert(mustard);

        LocalDate today = LocalDate.now();
        ingredientsDAO.addPriceToHistory(sausage.getId(), BigDecimal.valueOf(5.00), today);
        ingredientsDAO.addPriceToHistory(bread.getId(), BigDecimal.valueOf(1.00), today);
        ingredientsDAO.addPriceToHistory(mustard.getId(), BigDecimal.valueOf(0.50), today);

        hotDog.getIngredients().add(sausage);
        hotDog.getIngredients().add(bread);
        hotDog.getIngredients().add(mustard);

        Double grossMarginToday = hotDog.getGrossMargin(ingredientsDAO, today);

        assertEquals(-5540.00, grossMarginToday, 0.01);
    }

    @Test
    void testGetGrossMarginAtPastDate() throws Exception {
        Ingredients sausage = new Ingredients(0, "Sausage", LocalDateTime.now(), 5.00, 1000, Unite.Gramme);
        Ingredients bread = new Ingredients(0, "Bread", LocalDateTime.now(), 1.00, 500, Unite.Gramme);
        Ingredients mustard = new Ingredients(0, "Mustard", LocalDateTime.now(), 0.50, 100, Unite.Gramme);
        ingredientsDAO.insert(sausage);
        ingredientsDAO.insert(bread);
        ingredientsDAO.insert(mustard);

        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusDays(7);
        ingredientsDAO.addPriceToHistory(sausage.getId(), BigDecimal.valueOf(4.00), lastWeek);
        ingredientsDAO.addPriceToHistory(bread.getId(), BigDecimal.valueOf(0.80), lastWeek);
        ingredientsDAO.addPriceToHistory(mustard.getId(), BigDecimal.valueOf(0.30), lastWeek);

        ingredientsDAO.addPriceToHistory(sausage.getId(), BigDecimal.valueOf(5.00), today);
        ingredientsDAO.addPriceToHistory(bread.getId(), BigDecimal.valueOf(1.00), today);
        ingredientsDAO.addPriceToHistory(mustard.getId(), BigDecimal.valueOf(0.50), today);
        hotDog.getIngredients().add(sausage);
        hotDog.getIngredients().add(bread);
        hotDog.getIngredients().add(mustard);

        Double grossMarginLastWeek = hotDog.getGrossMargin(ingredientsDAO, lastWeek);

        assertEquals(-4420.00, grossMarginLastWeek, 0.01);
    }
}