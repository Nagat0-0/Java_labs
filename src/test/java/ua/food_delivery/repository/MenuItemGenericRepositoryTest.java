package ua.food_delivery.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.model.MenuItem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("MenuItem Repository Tests")
class MenuItemGenericRepositoryTest {

    private GenericRepository<MenuItem> menuItemRepository;
    private MenuItem item1, item2, item3;

    @BeforeAll
    void setUpTestData() {
        item1 = MenuItem.createMenuItem("Margherita", 150.0, "Pizza");
        item2 = MenuItem.createMenuItem("Cola", 30.0, "Drinks");
        item3 = MenuItem.createMenuItem("Cheesecake", 90.0, "Dessert");
    }

    @BeforeEach
    void setUp() {
        menuItemRepository = new MenuItemRepository();
        menuItemRepository.add(item1);
    }

    static Stream<Arguments> menuItemIdentityProvider() {
        return Stream.of(
                Arguments.of("Margherita", true, "Existing item"),
                Arguments.of("Cola", true, "Another existing item"),
                Arguments.of("Burger", false, "Non-existent item"),
                Arguments.of("", false, "Empty string"),
                Arguments.of(null, false, "Null identity")
        );
    }

    @DisplayName("Test adding valid menu item")
    @Test
    void testAddValidItem() {
        SoftAssertions softly = new SoftAssertions();
        int initialSize = menuItemRepository.size();

        boolean added = menuItemRepository.add(item2);

        softly.assertThat(added).as("Should successfully add item").isTrue();
        softly.assertThat(menuItemRepository.size()).as("Size should increase").isEqualTo(initialSize + 1);
        softly.assertAll();
    }

    @DisplayName("Test finding existing item")
    @Test
    void testFoundItem() {
        String expectedName = "Margherita";
        Optional<MenuItem> found = menuItemRepository.findByIdentity(expectedName);

        assertThat(found).isPresent();
        MenuItem foundItem = found.get();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(foundItem.getName()).isEqualTo(item1.getName());
        softly.assertThat(foundItem.getPrice()).isEqualTo(item1.getPrice());
        softly.assertThat(foundItem.getCategory()).isEqualTo(item1.getCategory());
        softly.assertAll();
    }

    @Test
    @DisplayName("Test duplicate prevention")
    void testDuplicatePrevention() {
        boolean added = menuItemRepository.add(item1);

        assertThat(added).as("Should return false when adding duplicate").isFalse();
        assertThat(menuItemRepository.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test null adding prevention")
    void testNullPrevention() {
        assertThatThrownBy(() -> menuItemRepository.add(null))
                .isInstanceOf(InvalidDataException.class)
                .hasMessageContaining("null");
    }

    @ParameterizedTest(name = "Find by name: {0} -> {1}")
    @MethodSource("menuItemIdentityProvider")
    @DisplayName("Test finding items by identity")
    void testFindByIdentity(String name, boolean shouldFind, String description) {
        if (shouldFind && "Cola".equals(name)) {
            menuItemRepository.add(item2);
        }

        Optional<MenuItem> result = menuItemRepository.findByIdentity(name);
        assertThat(result.isPresent()).isEqualTo(shouldFind);
    }

    @Test
    @DisplayName("Test getAll operation")
    void testGetAllItems() {
        menuItemRepository.add(item2);
        menuItemRepository.add(item3);

        List<MenuItem> allItems = menuItemRepository.getAll();

        assertThat(allItems)
                .hasSize(3)
                .contains(item1, item2, item3);
    }

    @Test
    @DisplayName("Test remove by identity")
    void testRemoveByIdentity() {
        boolean removed = menuItemRepository.removeByIdentity("Margherita");
        assertThat(removed).isTrue();
        assertThat(menuItemRepository.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test clear operation")
    void testClearRepository() {
        menuItemRepository.add(item2);
        menuItemRepository.clear();
        assertThat(menuItemRepository.isEmpty()).isTrue();
    }
}