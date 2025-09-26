import ua.food_delivery.model.*;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Restaurant r = Restaurant.createRestaurant("Pizza House", CuisineType.ITALIAN, "Kobylyanska Street 5");
        System.out.println(r);

        MenuItem m1 = MenuItem.createMenuItem("Margarita", 233.5, "Pizza");
        MenuItem m2 = new MenuItem("Coca-Cola", 60, "Drink");

        Customer newCustomer = new Customer("ivan", "petrenko", "University Street 2");
        System.out.println(newCustomer);

        Order o = new Order(newCustomer, Arrays.asList(m1, m2), LocalDateTime.now(), OrderStatus.PREPARING);
        System.out.println(o);
        System.out.println(o.getStatusDescription());

        Delivery d = new Delivery(o, "Courier Oleh Ivanenko", LocalDateTime.now().plusMinutes(30));
        System.out.println(d);
    }
}
