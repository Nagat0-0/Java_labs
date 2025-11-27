package ua.food_delivery.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.util.ValidationUtils;

import java.util.Objects;

public class Restaurant implements Comparable<Restaurant> {
    private static final Logger logger = LoggerFactory.getLogger(Restaurant.class);

    @NotBlank(message = "Restaurant name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 chars")
    private String name;

    @NotNull(message = "Cuisine type is required")
    private CuisineType cuisineType;

    @NotBlank(message = "Location cannot be empty")
    @Size(min = 5, message = "Location too short (min 5)")
    private String location;

    public Restaurant() {}

    public Restaurant(String name, CuisineType cuisineType, String location) {
        this.name = name != null ? name.trim() : null;
        this.cuisineType = cuisineType;
        this.location = location != null ? location.trim() : null;

        logger.info("Created Restaurant: {}", this.name);
    }

    public static Restaurant createRestaurant(String name, CuisineType cuisineType, String location) {
        Restaurant restaurant = new Restaurant(name, cuisineType, location);
        ValidationUtils.validate(restaurant);
        return restaurant;
    }


    public String getName() { return name; }
    public void setName(String name) {
        String oldValue = this.name;
        this.name = name != null ? name.trim() : null;
        validateOrRollback(() -> this.name = oldValue, "name", this.name);
    }

    public CuisineType getCuisineType() { return cuisineType; }
    public void setCuisineType(CuisineType cuisineType) {
        CuisineType oldValue = this.cuisineType;
        this.cuisineType = cuisineType;
        validateOrRollback(() -> this.cuisineType = oldValue, "cuisineType", this.cuisineType);
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        String oldValue = this.location;
        this.location = location != null ? location.trim() : null;
        validateOrRollback(() -> this.location = oldValue, "location", this.location);
    }

    private void validateOrRollback(Runnable rollback, String field, Object value) {
        try {
            ValidationUtils.validate(this);
            logger.debug("Set {}='{}'", field, value);
        } catch (InvalidDataException e) {
            rollback.run();
            throw e;
        }
    }

    @Override public int compareTo(Restaurant o) { return this.name.compareTo(o.name); }
    @Override public String toString() { return "Restaurant{name='" + name + "', cuisine=" + cuisineType + "}"; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant r)) return false;
        return Objects.equals(name, r.name) && cuisineType == r.cuisineType && Objects.equals(location, r.location);
    }
    @Override public int hashCode() { return Objects.hash(name, cuisineType, location); }
}