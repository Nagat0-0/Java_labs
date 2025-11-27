package ua.food_delivery.exception;

public class DataSerializationException extends Exception {
    public DataSerializationException() {
        super();
    }

    public DataSerializationException(String message) {
        super(message);
    }

    public DataSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSerializationException(Throwable cause) {
        super(cause);
    }
}