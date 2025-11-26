package ua.food_delivery.repository;

import ua.food_delivery.model.Customer;
import ua.food_delivery.util.LoggerUtil;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class CustomerRepository extends GenericRepository<Customer> {
    private static final Logger logger = LoggerUtil.getLogger();

    public CustomerRepository() {
        super(c -> c.firstName() + " " + c.lastName(), "Customer");
    }

    public List<Customer> sortByName() {
        List<Customer> all = getAll();
        all.sort(Comparator.comparing(Customer::lastName)
                .thenComparing(Customer::firstName));
        logger.info("Sorted Customer by last name and first name");
        return all;
    }

    public List<Customer> sortByAddressLength() {
        List<Customer> all = getAll();
        all.sort(Comparator.comparingInt((Customer c) -> c.address().length()));
        logger.info("Sorted Customer by address length");
        return all;
    }
}