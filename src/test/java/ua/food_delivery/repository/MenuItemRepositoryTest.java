package ua.food_delivery.repository;

import org.junit.jupiter.api.*;
import ua.food_delivery.model.MenuItem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("MenuItem Repository Tests")
class MenuItemRepositoryTest {

    private MenuItemRepository menuItemRepository;
    private MenuItem m1_CheapDrink;
    private MenuItem m2_ExpensiveMain;
    private MenuItem m3_MediumMain;
    private MenuItem m4_CheapStarter;

    @BeforeAll
    void setUpTestData() {
        m1_CheapDrink = MenuItem.createMenuItem("Water", 20.0, "Drinks");
        m2_ExpensiveMain = MenuItem.createMenuItem("Steak", 500.0, "Main");
        m3_MediumMain = MenuItem.createMenuItem("Pasta", 200.0, "Main");
        m4_CheapStarter = MenuItem.createMenuItem("Soup", 50.0, "Starter");
    }

    @BeforeEach
    void setUp() {
        menuItemRepository = new MenuItemRepository();
        menuItemRepository.add(m1_CheapDrink);
        menuItemRepository.add(m2_ExpensiveMain);
        menuItemRepository.add(m3_MediumMain);
        menuItemRepository.add(m4_CheapStarter);
    }

    @Test
    @DisplayName("Test Sort by Price ASC")
    void testSortByPriceAsc() {
        List<MenuItem> sorted = menuItemRepository.sortByPriceAsc();

        assertAll("Checking price sorting ASC",
                () -> assertEquals(20.0, sorted.get(0).getPrice()),
                () -> assertEquals(50.0, sorted.get(1).getPrice()),
                () -> assertEquals(200.0, sorted.get(2).getPrice()),
                () -> assertEquals(500.0, sorted.get(3).getPrice())
        );
    }

    @Test
    @DisplayName("Test Sort by Category and Price DESC")
    void testSortByCategoryAndPriceDesc() {

        List<MenuItem> sorted = menuItemRepository.sortByCategoryAndPriceDesc();

        assertAll("Checking complex sort: Category ASC + Price DESC",

                () -> assertEquals("Drinks", sorted.get(0).getCategory()),

                () -> assertEquals("Main", sorted.get(1).getCategory()),
                () -> assertEquals(500.0, sorted.get(1).getPrice()),

                () -> assertEquals("Main", sorted.get(2).getCategory()),
                () -> assertEquals(200.0, sorted.get(2).getPrice()),

                () -> assertEquals("Starter", sorted.get(3).getCategory())
        );
    }
}