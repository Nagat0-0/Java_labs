package ua.food_delivery.config;

public final class ConfigKeys {

    private ConfigKeys() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String DATA_PATH_BASE = "data.path.base";

    public static final String DATA_PATH_CUSTOMERS_JSON = "data.path.customers.json";
    public static final String DATA_PATH_CUSTOMERS_YAML = "data.path.customers.yaml";

    public static final String DATA_PATH_RESTAURANTS_JSON = "data.path.restaurants.json";
    public static final String DATA_PATH_RESTAURANTS_YAML = "data.path.restaurants.yaml";

    public static final String DATA_PATH_ORDERS_JSON = "data.path.orders.json";
    public static final String DATA_PATH_ORDERS_YAML = "data.path.orders.yaml";

    public static final String DATA_PATH_MENUITEMS_JSON = "data.path.menuitems.json";
    public static final String DATA_PATH_MENUITEMS_YAML = "data.path.menuitems.yaml";

    public static final String TEST_DATA_COUNT = "test.data.count";
}