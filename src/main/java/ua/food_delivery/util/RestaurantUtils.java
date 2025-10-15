package ua.food_delivery.util;

import ua.food_delivery.model.CuisineType;

public class RestaurantUtils {
    private RestaurantUtils(){}

    public static boolean isValidName(String name){
        return ValidationHelper.isStringLengthBetween(name, 1, 100);
    }

    public static boolean isValidCuisineType(CuisineType cuisineType){
        return cuisineType != null;
    }

    public static boolean isValidLocation(String location){
        return ValidationHelper.isStringLengthBetween(location, 1, 150);
    }
}
