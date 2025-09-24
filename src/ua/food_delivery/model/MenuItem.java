package ua.food_delivery.model;

import ua.food_delivery.util.MenuItemUtils;
import  java.util.Objects;


public class MenuItem {
    private String name;
    private double price;
    private String category;

    public MenuItem(){}

    public MenuItem(String name, double price, String category) {
        setName(name);
        setPrice(price);
        setCategory(category);
    }

    public String getName() { return name; }

    public void setName(String name) {
        if (MenuItemUtils.isValidName(name)) {
            this.name = name.trim();
        }
    }

    public double getPrice() { return price; }

    public void setPrice(double price) {
        if (MenuItemUtils.isValidPrice(price)) {
            this.price = price;
        }
    }

    public String getCategory() { return category; }

    public void setCategory(String category) {
        if (MenuItemUtils.isValidCategory(category)) {
            this.category = category.trim();
        }
    }

    public static MenuItem createMenuItem(String name, double price, String category) {
        if (MenuItemUtils.isValidName(name) &&
                MenuItemUtils.isValidPrice(price) &&
                MenuItemUtils.isValidCategory(category)) {
            return new MenuItem(name, price, category);
        }
        return null;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
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
