package ua.food_delivery.repository;

import ua.food_delivery.model.Customer;
import ua.food_delivery.util.LoggerUtil;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CustomerRepository extends GenericRepository<Customer> {
    private static final Logger logger = LoggerUtil.getLogger();

    public CustomerRepository() {
        super(c -> c.firstName() + " " + c.lastName(), "Customer");
    }

    public List<Customer> sortByName() {
        List<Customer> all = getAll();
        all.sort(Comparator.comparing(Customer::lastName).thenComparing(Customer::firstName));
        logger.info("Sorted Customer by last name and first name");
        return all;
    }

    public List<Customer> sortByAddressLength() {
        List<Customer> all = getAll();
        all.sort(Comparator.comparingInt((Customer c) -> c.address().length()));
        logger.info("Sorted Customer by address length");
        return all;
    }

    public List<Customer> findByAddressContaining(String partialAddress) {
        if (partialAddress == null || partialAddress.trim().isEmpty()) {
            return List.of();
        }
        String searchTerm = partialAddress.trim().toLowerCase();

        List<Customer> results = items.stream()
                .filter(c -> c.address().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        logger.info("Found " + results.size() + " customers with address containing '" + partialAddress + "'");
        return results;
    }

    public List<String> getAllFullNamesUpperCase() {
        List<String> names = items.stream()
                .map(c -> (c.firstName() + " " + c.lastName()).toUpperCase())
                .collect(Collectors.toList());

        logger.info("Retrieved " + names.size() + " customer names in UPPERCASE");
        return names;
    }
}