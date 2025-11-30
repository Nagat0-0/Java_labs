package ua.food_delivery.repository;

import org.junit.jupiter.api.*;
import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.CuisineType;
import ua.food_delivery.model.Restaurant;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Restaurant Repository Tests")
class RestaurantRepositoryTest {

    private RestaurantRepository repo;
    private Restaurant r1, r2, r3, r4;

    @BeforeAll
    void setUpTestData() {
        r1 = Restaurant.createRestaurant("Sushi Master", CuisineType.JAPANESE, "Kyiv, Center");
        r2 = Restaurant.createRestaurant("Pasta House", CuisineType.ITALIAN, "Lviv, Rynok Sq.");
        r3 = Restaurant.createRestaurant("Burger King", CuisineType.AMERICAN, "Odesa, Port");
        r4 = Restaurant.createRestaurant("Alfredo", CuisineType.ITALIAN, "Kyiv, Left Bank");
    }

    @BeforeEach
    void setUp() {
        repo = new RestaurantRepository();
        repo.add(r1);
        repo.add(r2);
        repo.add(r3);
        repo.add(r4);
    }

    @Test
    @DisplayName("Adding null throws InvalidDataException")
    void testAddNull() {
        assertThatThrownBy(() -> repo.add(null))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    @DisplayName("Internal Sort by Identity (Name)")
    void testInternalSortByIdentity() {
        repo.sortByIdentity("desc");
        List<Restaurant> items = repo.getAll();

        assertAll("Checking sort by Name DESC",
                () -> assertThat(items.get(0).getName()).isEqualTo("Sushi Master"),
                () -> assertThat(items.get(1).getName()).isEqualTo("Pasta House"),
                () -> assertThat(items.get(2).getName()).isEqualTo("Burger King"),
                () -> assertThat(items.get(3).getName()).isEqualTo("Alfredo")
        );
    }

    @Test
    @DisplayName("External Sort by Cuisine and Name")
    void testSortByCuisineAndName() {
        List<Restaurant> sorted = repo.sortByCuisineAndName();

        assertAll("Checking sort by Cuisine + Name",
                () -> assertThat(sorted.get(0).getName()).isEqualTo("Alfredo"),
                () -> assertThat(sorted.get(1).getName()).isEqualTo("Pasta House"),
                () -> assertThat(sorted.get(2).getName()).isEqualTo("Sushi Master"),
                () -> assertThat(sorted.get(3).getName()).isEqualTo("Burger King")
        );
    }

    @Test
    @DisplayName("Stream: Find by Cuisine")
    void testFindByCuisine() {
        List<Restaurant> italians = repo.findByCuisine(CuisineType.ITALIAN);

        assertAll("Checking filter by cuisine",
                () -> assertThat(italians).hasSize(2),
                () -> assertThat(italians).extracting(Restaurant::getName)
                        .containsExactlyInAnyOrder("Pasta House", "Alfredo")
        );
    }

    @Test
    @DisplayName("Stream: Group by Cuisine")
    void testGroupByCuisine() {
        Map<CuisineType, List<Restaurant>> grouped = repo.groupByCuisine();

        assertAll("Checking grouping",
                () -> assertThat(grouped).hasSize(3),
                () -> assertThat(grouped.get(CuisineType.ITALIAN)).hasSize(2),
                () -> assertThat(grouped.get(CuisineType.AMERICAN)).hasSize(1)
        );
    }
}