import ua.food_delivery.model.*;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Restaurant r = Restaurant.createRestaurant("Pizza House", "Italian", "Kobylyanska Street 5");
        System.out.println(r);

        MenuItem m1 = MenuItem.createMenuItem("Margarita", 233.5, "Pizza");
        MenuItem m2 = new MenuItem("Coca-Cola", 60, "Drink");

        MenuItem m3 = new MenuItem("Carbonara", -3, "Pasta");
        System.out.println(m3.getPrice());

        Customer badCustomer = new Customer("", "Petrenko", "University Street 2");
        System.out.println(badCustomer);

        Customer c = Customer.createCustomer("Ivan", "Shevchenko", "Chernivtsi, Golovna 10A");

        Order o = Order.createOrder(c, Arrays.asList(m1, m2), LocalDateTime.now());
        System.out.println(o);

        Delivery d = Delivery.createDelivery(o, "Courier Oleh Ivanenko", LocalDateTime.now().plusMinutes(30));
        System.out.println(d);
    }
}
