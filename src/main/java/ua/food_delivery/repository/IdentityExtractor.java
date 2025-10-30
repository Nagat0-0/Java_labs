package ua.food_delivery.repository;

@FunctionalInterface
public interface IdentityExtractor<T> {
    String extractIdentity(T item);
}