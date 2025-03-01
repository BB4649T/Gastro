import org.example.dao.IngredientsDAO;
import org.example.entity.Ingredients;
import org.example.entity.Unite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IngredientsDAOTest {

    private IngredientsDAO ingredientsDAO;

    @BeforeEach
    void setUp() {
        ingredientsDAO = new IngredientsDAO();
    }

    @Test
    void testInsertAndFindById() throws Exception {
        Ingredients ingredient = new Ingredients(0, "Tomato", LocalDateTime.now(), 4.00, 100, Unite.Gramme);
        ingredientsDAO.insert(ingredient);

        Ingredients retrievedIngredient = ingredientsDAO.findById(ingredient.getId());
        assertNotNull(retrievedIngredient);
        assertEquals(ingredient.getName(), retrievedIngredient.getName());
        assertEquals(ingredient.getQuantity(), retrievedIngredient.getQuantity());
        assertEquals(ingredient.getUnite(), retrievedIngredient.getUnite());
    }

    @Test
    void testUpdate() throws Exception {
        Ingredients ingredient = new Ingredients(0, "Tomato", LocalDateTime.now(), 4.00, 100, Unite.Gramme);
        ingredientsDAO.insert(ingredient);

        ingredient.setName("Updated Tomato");
        ingredient.setQuantity(200);
        ingredientsDAO.update(ingredient);

        Ingredients updatedIngredient = ingredientsDAO.findById(ingredient.getId());
        assertEquals("Updated Tomato", updatedIngredient.getName());
        assertEquals(200, updatedIngredient.getQuantity());
    }

    @Test
    void testDelete() throws Exception {
        Ingredients ingredient = new Ingredients(0, "Tomato", LocalDateTime.now(), 4.00, 100, Unite.Gramme);
        ingredientsDAO.insert(ingredient);

        ingredientsDAO.delete(ingredient.getId());

        Ingredients deletedIngredient = ingredientsDAO.findById(ingredient.getId());
        assertNull(deletedIngredient);
    }

    @Test
    void testFindAll() throws Exception {
        Ingredients ingredient1 = new Ingredients(0, "Tomato", LocalDateTime.now(), 4.00, 100, Unite.Gramme);
        Ingredients ingredient2 = new Ingredients(0, "Chicken", LocalDateTime.now(), 5.00, 50, Unite.Gramme);
        ingredientsDAO.insert(ingredient1);
        ingredientsDAO.insert(ingredient2);

        List<Ingredients> ingredients = ingredientsDAO.findAll();
        assertTrue(ingredients.size() >= 2);
    }

    @Test
    void testAddPriceToHistoryAndGetPriceAtDate() throws Exception {
        Ingredients ingredient = new Ingredients(0, "Tomato", LocalDateTime.now(), 4.00, 100, Unite.Gramme);
        ingredientsDAO.insert(ingredient);

        LocalDate date = LocalDate.now();
        ingredientsDAO.addPriceToHistory(ingredient.getId(), new BigDecimal("4.00"), date.minusDays(7)); // Utiliser le constructeur String
        ingredientsDAO.addPriceToHistory(ingredient.getId(), new BigDecimal("6.00"), date);

        BigDecimal price = ingredientsDAO.getPriceAtDate(ingredient.getId(), date.minusDays(5));

        assertEquals(0, new BigDecimal("4.00").compareTo(price));
    }
}