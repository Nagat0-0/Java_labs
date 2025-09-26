package ua.food_delivery.model;

import ua.food_delivery.util.RestaurantUtils;
import java.util.Objects;

public class Restaurant {
    private String name;
    private CuisineType cuisineType; // 🔹 enum замість String
    private String location;

    public Restaurant() {}

    public Restaurant(String name, CuisineType cuisineType, String location) {
        setName(name);
        setCuisineType(cuisineType);
        setLocation(location);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (RestaurantUtils.isValidName(name)) {
            this.name = name.trim();
        }
    }

    public CuisineType getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(CuisineType cuisineType) {
        if (RestaurantUtils.isValidCuisineType(cuisineType)) {
            this.cuisineType = cuisineType;
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (RestaurantUtils.isValidLocation(location)) {
            this.location = location.trim();
        }
    }

    public static Restaurant createRestaurant(String name, CuisineType cuisineType, String location) {
        if (RestaurantUtils.isValidName(name) &&
                RestaurantUtils.isValidCuisineType(cuisineType) &&
                RestaurantUtils.isValidLocation(location)) {
            return new Restaurant(name, cuisineType, location);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", cuisineType=" + cuisineType +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant restaurant)) return false;
        return Objects.equals(name, restaurant.name) &&
                cuisineType == restaurant.cuisineType &&
                Objects.equals(location, restaurant.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cuisineType, location);
    }
}
