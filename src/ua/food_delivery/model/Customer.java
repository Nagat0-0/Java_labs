package ua.food_delivery.model;

import ua.food_delivery.util.CustomerUtils;

import java.util.Objects;

public class Customer {
    private String firstName;
    private String lastName;
    private String address;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String address) {
        setFirstName(firstName);
        setLastName(lastName);
        setAddress(address);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (CustomerUtils.isValidName(firstName)) {
            this.firstName = CustomerUtils.capitalizeText(firstName);
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (CustomerUtils.isValidName(lastName)) {
            this.lastName = CustomerUtils.capitalizeText(lastName);
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (CustomerUtils.isValidAddress(address)) {
            this.address = address.trim();
        }
    }

    public static Customer createCustomer(String firstName, String lastName, String address) {
        if (CustomerUtils.isValidName(firstName) &&
                CustomerUtils.isValidName(lastName) &&
                CustomerUtils.isValidAddress(address)) {
            return new Customer(firstName, lastName, address);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(address, customer.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, address);
    }
}
