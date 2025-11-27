package ua.food_delivery.repository;

import org.junit.jupiter.api.*;
import ua.food_delivery.model.MenuItem;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("MenuItem Repository Tests")
class MenuItemRepositoryTest {

    private MenuItemRepository repo;
    private MenuItem m1, m2, m3, m4;

    @BeforeAll
    void setUpTestData() {
        m1 = MenuItem.createMenuItem("Water", 20.0, "Drinks");
        m2 = MenuItem.createMenuItem("Steak", 500.0, "Main");
        m3 = MenuItem.createMenuItem("Pasta", 200.0, "Main");
        m4 = MenuItem.createMenuItem("Soup", 50.0, "Starter");
    }

    @BeforeEach
    void setUp() {
        repo = new MenuItemRepository();
        repo.add(m1);
        repo.add(m2);
        repo.add(m3);
        repo.add(m4);
    }

    @Test
    @DisplayName("Adding null item returns false")
    void testAddNull() {
        boolean result = repo.add(null);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Sort by Price ASC")
    void testSortByPriceAsc() {
        List<MenuItem> sorted = repo.sortByPriceAsc();

        assertAll(
                () -> assertThat(sorted.get(0).getPrice()).isEqualTo(20.0),
                () -> assertThat(sorted.get(3).getPrice()).isEqualTo(500.0)
        );
    }

    @Test
    @DisplayName("Stream: Find in price range")
    void testFindByPriceRange() {
        List<MenuItem> result = repo.findByPriceRange(40, 250);

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result).extracting(MenuItem::getName)
                        .containsExactlyInAnyOrder("Soup", "Pasta")
        );
    }

    @Test
    @DisplayName("Stream: Most expensive item")
    void testGetMostExpensiveItem() {
        Optional<MenuItem> max = repo.getMostExpensiveItem();

        assertThat(max).isPresent();
        assertThat(max.get().getName()).isEqualTo("Steak");
        assertThat(max.get().getPrice()).isEqualTo(500.0);
    }
}