package ua.food_delivery.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.food_delivery.model.Order;
import ua.food_delivery.model.OrderStatus;
import ua.food_delivery.repository.OrderRepository;
import ua.food_delivery.serializer.JsonDataSerializer;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/orders", "/orders/*"})
public class OrderServlet extends BaseServlet {

    private OrderRepository repository;
    private JsonDataSerializer<Order> serializer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = (OrderRepository) config.getServletContext().getAttribute("orderRepository");
        serializer = new JsonDataSerializer<>();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                String statusParam = req.getParameter("status");
                List<Order> items;

                if (statusParam != null) {
                    OrderStatus status = OrderStatus.valueOf(statusParam.toUpperCase());
                    items = repository.findByStatus(status);
                } else {
                    items = repository.getAll();
                }
                sendJson(resp, serializer.listToString(items));
            } else {
                sendError(resp, HttpServletResponse.SC_NOT_IMPLEMENTED, "Finding order by complex ID via URL is not implemented");
            }
        } catch (IllegalArgumentException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid status");
        } catch (Exception e) {
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String body = getRequestBody(req);
            Order newItem = serializer.fromString(body, Order.class);

            boolean added = repository.add(newItem);
            if (added) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                sendJson(resp, serializer.toString(newItem));
            } else {
                sendError(resp, HttpServletResponse.SC_CONFLICT, "Order already exists");
            }
        } catch (Exception e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid Data: " + e.getMessage());
        }
    }
}