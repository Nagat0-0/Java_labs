package ua.food_delivery.serializer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ua.food_delivery.exception.DataSerializationException;
import ua.food_delivery.model.Customer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Data Serializer Tests (Lab 7)")
class DataSerializerTest {

    @Test
    @DisplayName("Test JSON: Serialize and Deserialize correctly")
    void testJsonSerialization(@TempDir Path tempDir) throws DataSerializationException {
        File file = tempDir.resolve("test_customers.json").toFile();
        DataSerializer<Customer> serializer = new JsonDataSerializer<>();

        Customer c1 = Customer.createCustomer("Ivan", "Test", "Kyiv St 1");
        Customer c2 = Customer.createCustomer("Anna", "Test", "Lviv St 2");
        List<Customer> originalList = List.of(c1, c2);

        serializer.serialize(originalList, file.getAbsolutePath());

        assertTrue(file.exists(), "JSON file should be created");
        assertTrue(file.length() > 0, "JSON file should not be empty");

        List<Customer> loadedList = serializer.deserialize(file.getAbsolutePath(), Customer.class);

        assertNotNull(loadedList);
        assertEquals(originalList.size(), loadedList.size(), "List size should match");

        assertEquals(c1.firstName(), loadedList.get(0).firstName());
        assertEquals(c1.lastName(), loadedList.get(0).lastName());
        assertEquals(c1.address(), loadedList.get(0).address());
    }

    @Test
    @DisplayName("Test YAML: Serialize and Deserialize correctly")
    void testYamlSerialization(@TempDir Path tempDir) throws DataSerializationException {
        File file = tempDir.resolve("test_customers.yaml").toFile();
        DataSerializer<Customer> serializer = new YamlDataSerializer<>();
        List<Customer> originalList = List.of(
                Customer.createCustomer("Yaml", "User", "Test Path 55")
        );

        serializer.serialize(originalList, file.getAbsolutePath());

        assertTrue(file.exists());

        List<Customer> loadedList = serializer.deserialize(file.getAbsolutePath(), Customer.class);

        assertEquals(1, loadedList.size());
        assertEquals("Yaml", loadedList.get(0).firstName());
    }

    @Test
    @DisplayName("Exception Handling: Serialize null list")
    void testSerializeNullThrowsException() {
        DataSerializer<Customer> serializer = new JsonDataSerializer<>();

        DataSerializationException exception = assertThrows(DataSerializationException.class, () -> {
            serializer.serialize(null, "dummy.json");
        });

        assertEquals("Cannot serialize null list", exception.getMessage());
    }

    @Test
    @DisplayName("Exception Handling: Deserialize corrupted file")
    void testDeserializeCorruptedFile(@TempDir Path tempDir) throws IOException {
        File corruptedFile = tempDir.resolve("corrupted.json").toFile();
        Files.writeString(corruptedFile.toPath(), "{ invalid json content here... ");

        DataSerializer<Customer> serializer = new JsonDataSerializer<>();

        assertThrows(DataSerializationException.class, () -> {
            serializer.deserialize(corruptedFile.getAbsolutePath(), Customer.class);
        });
    }

    @Test
    @DisplayName("Edge Case: Deserialize non-existent file returns empty list")
    void testDeserializeNonExistentFile(@TempDir Path tempDir) throws DataSerializationException {
        File missingFile = tempDir.resolve("ghost.json").toFile();
        DataSerializer<Customer> serializer = new JsonDataSerializer<>();

        List<Customer> result = serializer.deserialize(missingFile.getAbsolutePath(), Customer.class);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list for missing file");
    }
}