package ua.food_delivery.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.CuisineType;
import ua.food_delivery.model.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Restaurant Repository Tests")
class RestaurantGenericRepositoryTest {

    private GenericRepository<Restaurant> restaurantRepository;
    private Restaurant r1, r2, r3;

    @BeforeAll
    void setUpTestData() {
        r1 = Restaurant.createRestaurant("Sushi Master", CuisineType.JAPANESE, "Kyiv Center");
        r2 = Restaurant.createRestaurant("Pasta House", CuisineType.ITALIAN, "Lviv Rynok");
        r3 = Restaurant.createRestaurant("Burger King", CuisineType.AMERICAN, "Odesa Port");
    }

    @BeforeEach
    void setUp() {
        restaurantRepository = new RestaurantRepository();
        restaurantRepository.add(r1);
    }

    static Stream<Arguments> restaurantIdentityProvider() {
        return Stream.of(
                Arguments.of("Sushi Master", true, "Existing restaurant"),
                Arguments.of("Pasta House", true, "Another existing restaurant"),
                Arguments.of("Unknown Place", false, "Non-existent restaurant"),
                Arguments.of("", false, "Empty string"),
                Arguments.of(null, false, "Null identity")
        );
    }

    @DisplayName("Test adding valid restaurant")
    @Test
    void testAddValidRestaurant() {
        SoftAssertions softly = new SoftAssertions();
        int initialSize = restaurantRepository.size();

        boolean added = restaurantRepository.add(r2);

        softly.assertThat(added).isTrue();
        softly.assertThat(restaurantRepository.size()).isEqualTo(initialSize + 1);
        softly.assertAll();
    }

    @DisplayName("Test finding existing restaurant")
    @Test
    void testFoundRestaurant() {
        Optional<Restaurant> found = restaurantRepository.findByIdentity("Sushi Master");

        assertThat(found).isPresent();
        Restaurant foundRestaurant = found.get();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(foundRestaurant.getName()).isEqualTo(r1.getName());
        softly.assertThat(foundRestaurant.getCuisineType()).isEqualTo(r1.getCuisineType());
        softly.assertThat(foundRestaurant.getLocation()).isEqualTo(r1.getLocation());
        softly.assertAll();
    }

    @Test
    @DisplayName("Test duplicate prevention")
    void testDuplicatePrevention() {
        boolean added = restaurantRepository.add(r1);
        assertThat(added).isFalse();
        assertThat(restaurantRepository.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test null adding prevention")
    void testNullPrevention() {
        assertThatThrownBy(() -> restaurantRepository.add(null))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("null");
    }

    @ParameterizedTest(name = "Find by name: {0} -> {1}")
    @MethodSource("restaurantIdentityProvider")
    @DisplayName("Test finding restaurants by identity")
    void testFindByIdentity(String name, boolean shouldFind, String description) {
        if (shouldFind && "Pasta House".equals(name)) {
            restaurantRepository.add(r2);
        }

        Optional<Restaurant> result = restaurantRepository.findByIdentity(name);
        assertThat(result.isPresent()).isEqualTo(shouldFind);
    }

    @Test
    @DisplayName("Test getAll operation")
    void testGetAllRestaurants() {
        restaurantRepository.add(r2);
        restaurantRepository.add(r3);

        List<Restaurant> all = restaurantRepository.getAll();

        assertThat(all).hasSize(3).contains(r1, r2, r3);
    }

    @Test
    @DisplayName("Test remove by identity")
    void testRemoveByIdentity() {
        boolean removed = restaurantRepository.removeByIdentity("Sushi Master");
        assertThat(removed).isTrue();
        assertThat(restaurantRepository.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test clear operation")
    void testClearRepository() {
        restaurantRepository.add(r2);
        restaurantRepository.clear();
        assertThat(restaurantRepository.isEmpty()).isTrue();
    }
}