package ua.food_delivery.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.food_delivery.model.MenuItem;

import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericRepositoryThreadSafetyTest {

    private MenuItemRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MenuItemRepository();
    }

    @Test
    void add_FromMultipleThreads_ShouldBeThreadSafe() throws InterruptedException {
        int threadCount = 10;
        int itemsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int t = 0; t < threadCount; t++) {
            final int threadNum = t;
            executor.submit(() -> {
                try {
                    for (int i = 0; i < itemsPerThread; i++) {
                        String name = "Item-" + threadNum + "-" + i;
                        repository.add(MenuItem.createMenuItem(name, 50.0, "Test"));
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(threadCount * itemsPerThread, repository.size());
    }

    @Test
    void addAll_ShouldAddMultipleItems() {
        List<MenuItem> items = List.of(
                MenuItem.createMenuItem("Pizza", 100.0, "Food"),
                MenuItem.createMenuItem("Cola", 50.0, "Drink"),
                MenuItem.createMenuItem("Soup", 80.0, "Food")
        );

        int added = repository.addAll(items);

        assertEquals(3, added);
        assertEquals(3, repository.size());
    }

    @Test
    void addAll_WithDuplicates_ShouldSkipDuplicates() {
        repository.add(MenuItem.createMenuItem("Pizza", 100.0, "Food"));

        List<MenuItem> items = List.of(
                MenuItem.createMenuItem("Pizza", 100.0, "Food"),
                MenuItem.createMenuItem("Cola", 50.0, "Drink")
        );

        int added = repository.addAll(items);

        assertEquals(1, added);
        assertEquals(2, repository.size());
    }
}