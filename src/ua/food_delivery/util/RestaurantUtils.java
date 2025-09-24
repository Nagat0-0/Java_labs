package ua.food_delivery.util;

public class RestaurantUtils {
    private RestaurantUtils(){}

    public static boolean isValidName(String name){
        return ValidationHelper.isStringLengthBetween(name, 1, 100);
    }

    public static boolean isValidCuisine(String cuisine){
        return ValidationHelper.isStringLengthBetween(cuisine, 1, 50);
    }

    public static boolean isValidLocation(String location){
        return ValidationHelper.isStringLengthBetween(location, 1, 150);
    }
}
