import org.example.dao.IngredientsDAO;
import org.example.entity.StockMovement;
import org.example.entity.Unite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IngredientsDAOTest2 {

    private IngredientsDAO ingredientsDAO;

    @BeforeEach
    void setUp() {
        ingredientsDAO = new IngredientsDAO();
    }

    @Test
    void testAddStockMovement() throws Exception {
        StockMovement movement = new StockMovement(
                0, 1, "ENTRY", BigDecimal.valueOf(100), Unite.Unit, LocalDateTime.now()
        );
        ingredientsDAO.addStockMovement(movement);

        BigDecimal currentStock = ingredientsDAO.getCurrentStock(1);
        assertEquals(BigDecimal.valueOf(100), currentStock);
    }
}
