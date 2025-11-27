package ua.food_delivery.model;

import jakarta.validation.constraints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.util.ValidationUtils;
import java.util.Objects;

public class MenuItem implements Comparable<MenuItem> {
    private static final Logger logger = LoggerFactory.getLogger(MenuItem.class);

    @NotBlank(message = "Item name required")
    private String name;

    @Positive(message = "Price must be positive")
    @Max(value = 10000, message = "Price is too high")
    private double price;

    @NotBlank(message = "Category required")
    private String category;

    public MenuItem() {}

    public MenuItem(String name, double price, String category) {
        this.name = name != null ? name.trim() : null;
        this.price = price;
        this.category = category != null ? category.trim() : null;
    }

    public static MenuItem createMenuItem(String name, double price, String category) {
        MenuItem item = new MenuItem(name, price, category);
        ValidationUtils.validate(item);
        return item;
    }

    public String getName() { return name; }
    public void setName(String name) {
        String old = this.name;
        this.name = name;
        check(() -> this.name = old);
    }

    public double getPrice() { return price; }
    public void setPrice(double price) {
        double old = this.price;
        this.price = price;
        check(() -> this.price = old);
    }

    public String getCategory() { return category; }
    public void setCategory(String category) {
        String old = this.category;
        this.category = category;
        check(() -> this.category = old);
    }

    private void check(Runnable rollback) {
        try { ValidationUtils.validate(this); }
        catch (InvalidDataException e) { rollback.run(); throw e; }
    }

    @Override public int compareTo(MenuItem o) { return Double.compare(this.price, o.price); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem m)) return false;
        return Double.compare(m.price, price) == 0 && Objects.equals(name, m.name);
    }
    @Override public int hashCode() { return Objects.hash(name, price); }
    @Override public String toString() { return name + " (" + price + ")"; }
}