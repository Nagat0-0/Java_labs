package ua.food_delivery.model;

import ua.food_delivery.util.RestaurantUtils;
import java.util.Objects;

public class Restaurant {
    private String name;
    private String cuisine;
    private String location;

    public Restaurant() {}

    public Restaurant(String name, String cuisine, String location) {
        setName(name);
        setCuisine(cuisine);
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

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        if (RestaurantUtils.isValidCuisine(cuisine)) {
            this.cuisine = cuisine.trim();
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

    public static Restaurant createRestaurant(String name, String cuisine, String location) {
        if (RestaurantUtils.isValidName(name) &&
                RestaurantUtils.isValidCuisine(cuisine) &&
                RestaurantUtils.isValidLocation(location)) {
            return new Restaurant(name, cuisine, location);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant restaurant)) return false;
        return Objects.equals(name, restaurant.name) &&
                Objects.equals(cuisine, restaurant.cuisine) &&
                Objects.equals(location, restaurant.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cuisine, location);
    }
}
