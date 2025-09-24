package ua.food_delivery.util;

public class MenuItemUtils {
    private MenuItemUtils(){}

    public static boolean isValidName(String name){
        return ValidationHelper.isStringLengthBetween(name, 1, 100);
    }

    public static boolean isValidPrice(double price){
        return ValidationHelper.isNumberBetween(price, 0.01, 1000.0);
    }

    public static boolean isValidCategory(String category){
        return ValidationHelper.isStringLengthBetween(category, 1, 50);
    }
}
