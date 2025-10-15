package ua.food_delivery.util;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger("FoodDeliveryLogger");

    static {
        try {
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);

            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);

            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);
        } catch (IOException e) {
            System.err.println("Could not initialize log file: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
