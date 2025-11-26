package ua.food_delivery.model;

import ua.food_delivery.exception.InvalidDataException;
import ua.food_delivery.util.MenuItemUtils;
import java.util.Comparator;
import java.util.Objects;

public class MenuItem implements Comparable<MenuItem> {
    private String name;
    private double price;
    private String category;

    public static final Comparator<MenuItem> NATURAL_ORDER = Comparator.comparing(MenuItem::getName);

    public MenuItem() {}

    public MenuItem(String name, double price, String category) throws InvalidDataException {
        setName(name);
        setPrice(price);
        setCategory(category);
    }

    public String getName() { return name; }

    public void setName(String name) throws InvalidDataException {
        if (!MenuItemUtils.isValidName(name)) {
            throw new InvalidDataException("Invalid menu item name: " + name);
        }
        this.name = name.trim();
    }

    public double getPrice() { return price; }

    public void setPrice(double price) throws InvalidDataException {
        if (!MenuItemUtils.isValidPrice(price)) {
            throw new InvalidDataException("Invalid price: " + price);
        }
        this.price = price;
    }

    public String getCategory() { return category; }

    public void setCategory(String category) throws InvalidDataException {
        if (!MenuItemUtils.isValidCategory(category)) {
            throw new InvalidDataException("Invalid category: " + category);
        }
        this.category = category.trim();
    }

    public static MenuItem createMenuItem(String name, double price, String category) throws InvalidDataException {
        return new MenuItem(name, price, category);
    }

    @Override
    public int compareTo(MenuItem other) {
        return NATURAL_ORDER.compare(this, other);
    }

    @Override
    public String toString() {
        return "MenuItem{name='" + name + "', price=" + price + ", category='" + category + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem menuItem)) return false;
        return Double.compare(menuItem.price, price) == 0 &&
                Objects.equals(name, menuItem.name) &&
                Objects.equals(category, menuItem.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category);
    }
}