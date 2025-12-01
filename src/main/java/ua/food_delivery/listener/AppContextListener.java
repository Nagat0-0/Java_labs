package ua.food_delivery.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ua.food_delivery.config.AppConfig;
import ua.food_delivery.model.*;
import ua.food_delivery.persistence.PersistenceManager;
import ua.food_delivery.repository.*;
import ua.food_delivery.service.loading.DataLoader;
import ua.food_delivery.service.loading.ExecutorLoadingStrategy;
import ua.food_delivery.util.LoggerUtil;

import java.util.logging.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger = LoggerUtil.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("=== APPLICATION STARTUP ===");
        ServletContext context = sce.getServletContext();

        try {
            AppConfig config = new AppConfig();
            PersistenceManager persistenceManager = new PersistenceManager(config);

            CustomerRepository customerRepo = new CustomerRepository();
            RestaurantRepository restaurantRepo = new RestaurantRepository();
            MenuItemRepository menuItemRepo = new MenuItemRepository();
            OrderRepository orderRepo = new OrderRepository();

            DataLoader dataLoader = new DataLoader(persistenceManager);
            dataLoader.load(
                    customerRepo, restaurantRepo, menuItemRepo, orderRepo,
                    new ExecutorLoadingStrategy(4)
            );

            context.setAttribute("customerRepository", customerRepo);
            context.setAttribute("restaurantRepository", restaurantRepo);
            context.setAttribute("menuItemRepository", menuItemRepo);
            context.setAttribute("orderRepository", orderRepo);
            context.setAttribute("persistenceManager", persistenceManager);

            logger.info("Repositories initialized and stored in context");

        } catch (Exception e) {
            logger.severe("Startup failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("=== APPLICATION SHUTDOWN ===");
        ServletContext context = sce.getServletContext();
        PersistenceManager pm = (PersistenceManager) context.getAttribute("persistenceManager");

        CustomerRepository cr = (CustomerRepository) context.getAttribute("customerRepository");
        RestaurantRepository rr = (RestaurantRepository) context.getAttribute("restaurantRepository");
        MenuItemRepository mr = (MenuItemRepository) context.getAttribute("menuItemRepository");
        OrderRepository or = (OrderRepository) context.getAttribute("orderRepository");

        if (pm != null) {
            try {
                if (cr != null) pm.save(cr.getAll(), "customers", Customer.class, "JSON");
                if (rr != null) pm.save(rr.getAll(), "restaurants", Restaurant.class, "JSON");
                if (mr != null) pm.save(mr.getAll(), "menuitems", MenuItem.class, "JSON");
                if (or != null) pm.save(or.getAll(), "orders", Order.class, "JSON");
                logger.info("Data saved successfully");
            } catch (Exception e) {
                logger.severe("Failed to save data on shutdown: " + e.getMessage());
            }
        }
    }
}