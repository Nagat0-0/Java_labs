package ua.food_delivery.model;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.util.RestaurantUtils;
import java.util.Comparator;
import java.util.Objects;

public class Restaurant implements Comparable<Restaurant> {
    private String name;
    private CuisineType cuisineType;
    private String location;

    public static final Comparator<Restaurant> NATURAL_ORDER = Comparator.comparing(Restaurant::getName);

    public Restaurant() {}

    public Restaurant(String name, CuisineType cuisineType, String location) throws InvalidDataException {
        setName(name);
        setCuisineType(cuisineType);
        setLocation(location);
    }

    public String getName() { return name; }

    public void setName(String name) throws InvalidDataException {
        if (!RestaurantUtils.isValidName(name)) {
            throw new InvalidDataException("Invalid restaurant name: " + name);
        }
        this.name = name.trim();
    }

    public CuisineType getCuisineType() { return cuisineType; }

    public void setCuisineType(CuisineType cuisineType) throws InvalidDataException {
        if (!RestaurantUtils.isValidCuisineType(cuisineType)) {
            throw new InvalidDataException("Cuisine type cannot be null");
        }
        this.cuisineType = cuisineType;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) throws InvalidDataException {
        if (!RestaurantUtils.isValidLocation(location)) {
            throw new InvalidDataException("Invalid location: " + location);
        }
        this.location = location.trim();
    }

    public static Restaurant createRestaurant(String name, CuisineType cuisineType, String location)
            throws InvalidDataException {
        return new Restaurant(name, cuisineType, location);
    }

    @Override
    public int compareTo(Restaurant other) {
        return NATURAL_ORDER.compare(this, other);
    }

    @Override
    public String toString() {
        return "Restaurant{name='" + name + "', cuisineType=" + cuisineType + ", location='" + location + "'}";
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