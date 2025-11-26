package ua.food_delivery.repository;

import org.junit.jupiter.api.*;
import ua.food_delivery.model.CuisineType;
import ua.food_delivery.model.Restaurant;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Restaurant Repository Tests")
class RestaurantRepositoryTest {

    private RestaurantRepository restaurantRepository;
    private Restaurant r1_SushiKyiv;
    private Restaurant r2_PastaLviv;
    private Restaurant r3_BurgerOdesa;
    private Restaurant r4_PastaKyiv;

    @BeforeAll
    void setUpTestData() {
        r1_SushiKyiv = Restaurant.createRestaurant("Sushi Master", CuisineType.JAPANESE, "Kyiv, Center");
        r2_PastaLviv = Restaurant.createRestaurant("Pasta House", CuisineType.ITALIAN, "Lviv, Rynok Sq.");
        r3_BurgerOdesa = Restaurant.createRestaurant("Burger King", CuisineType.AMERICAN, "Odesa, Port");
        r4_PastaKyiv = Restaurant.createRestaurant("Alfredo", CuisineType.ITALIAN, "Kyiv, Left Bank");
    }

    @BeforeEach
    void setUp() {
        restaurantRepository = new RestaurantRepository();
        restaurantRepository.add(r1_SushiKyiv);
        restaurantRepository.add(r2_PastaLviv);
        restaurantRepository.add(r3_BurgerOdesa);
        restaurantRepository.add(r4_PastaKyiv);
    }

    @Test
    @DisplayName("Test Internal Sort by Identity (Name)")
    void testInternalSortByIdentity() {
        restaurantRepository.sortByIdentity("desc");
        List<Restaurant> items = restaurantRepository.getAll();

        assertAll("Checking sort by Name DESC",
                () -> assertEquals("Sushi Master", items.get(0).getName()),
                () -> assertEquals("Pasta House", items.get(1).getName()),
                () -> assertEquals("Burger King", items.get(2).getName()),
                () -> assertEquals("Alfredo", items.get(3).getName())
        );
    }

    @Test
    @DisplayName("Test External Sort by Cuisine and Name")
    void testSortByCuisineAndName() {

        List<Restaurant> sorted = restaurantRepository.sortByCuisineAndName();

        assertAll("Checking complex sort: Cuisine (Enum Order) + Name ASC",
                () -> assertEquals(CuisineType.ITALIAN, sorted.get(0).getCuisineType()),
                () -> assertEquals("Alfredo", sorted.get(0).getName()),

                () -> assertEquals(CuisineType.ITALIAN, sorted.get(1).getCuisineType()),
                () -> assertEquals("Pasta House", sorted.get(1).getName()),

                () -> assertEquals(CuisineType.JAPANESE, sorted.get(2).getCuisineType()),
                () -> assertEquals("Sushi Master", sorted.get(2).getName()),

                () -> assertEquals(CuisineType.AMERICAN, sorted.get(3).getCuisineType()),
                () -> assertEquals("Burger King", sorted.get(3).getName())
        );
    }

    @Test
    @DisplayName("Test External Sort by Location DESC")
    void testSortByLocationDesc() {

        List<Restaurant> sorted = restaurantRepository.sortByLocationDesc();

        assertAll("Checking sort by Location DESC",
                () -> assertTrue(sorted.get(0).getLocation().startsWith("Odesa"), "First should be Odesa"),
                () -> assertTrue(sorted.get(1).getLocation().startsWith("Lviv"), "Second should be Lviv"),
                () -> assertEquals("Kyiv, Left Bank", sorted.get(2).getLocation()),
                () -> assertEquals("Kyiv, Center", sorted.get(3).getLocation())
        );
    }


    @Test
    @DisplayName("Stream: Find by Cuisine")
    void testFindByCuisine() {
        List<Restaurant> italians = restaurantRepository.findByCuisine(CuisineType.ITALIAN);

        assertAll("Checking filter by cuisine",
                () -> assertEquals(2, italians.size()),
                () -> assertTrue(italians.stream().allMatch(r -> r.getCuisineType() == CuisineType.ITALIAN))
        );
    }

    @Test
    @DisplayName("Stream: Group by Cuisine")
    void testGroupByCuisine() {
        Map<CuisineType, List<Restaurant>> grouped = restaurantRepository.groupByCuisine();

        assertAll("Checking grouping",
                () -> assertEquals(3, grouped.size()),
                () -> assertEquals(2, grouped.get(CuisineType.ITALIAN).size()),
                () -> assertEquals(1, grouped.get(CuisineType.AMERICAN).size())
        );
    }
}