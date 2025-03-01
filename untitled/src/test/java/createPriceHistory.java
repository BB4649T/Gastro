import org.example.entity.IngredientPriceHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IngredientsDAO {

    private org.example.dao.IngredientsDAO ingredientsDAO;

    @BeforeEach
    void setUp() {
        ingredientsDAO = new org.example.dao.IngredientsDAO();
    }

    @Test
    void testCreatePriceHistory() throws Exception {
        IngredientPriceHistory priceHistory = new IngredientPriceHistory(
                0,
                1,
                BigDecimal.valueOf(5.00),
                LocalDate.now()
        );

        ingredientsDAO.createPriceHistory(priceHistory);

        assertTrue(priceHistory.getId() > 0);

        BigDecimal retrievedPrice = ingredientsDAO.getPriceAtDate(priceHistory.getIngredientId(), priceHistory.getDate());
        assertEquals(0, BigDecimal.valueOf(5.00).compareTo(retrievedPrice));
    }
}