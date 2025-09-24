package ua.food_delivery.util;

public class CustomerUtils {

    private CustomerUtils() {
    }

    public static boolean isValidName(String name) {
        return ValidationHelper.isStringLengthBetween(name, 1, 50);
    }

    public static boolean isValidAddress(String address) {
        return ValidationHelper.isStringLengthBetween(address, 5, 200);
    }

    public static String capitalizeText(String text) {
        if (text == null || text.trim().isEmpty()) return text;
        String trimmed = text.trim();
        return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }
}
