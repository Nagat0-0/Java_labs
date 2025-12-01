package ua.food_delivery.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.food_delivery.model.CuisineType;
import ua.food_delivery.model.Restaurant;
import ua.food_delivery.repository.RestaurantRepository;
import ua.food_delivery.serializer.JsonDataSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/restaurants", "/restaurants/*"})
public class RestaurantServlet extends BaseServlet {

    private RestaurantRepository repository;
    private JsonDataSerializer<Restaurant> serializer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = (RestaurantRepository) config.getServletContext().getAttribute("restaurantRepository");
        serializer = new JsonDataSerializer<>();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                String cuisineParam = req.getParameter("cuisine");
                List<Restaurant> items;

                if (cuisineParam != null) {
                    CuisineType type = CuisineType.valueOf(cuisineParam.toUpperCase());
                    items = repository.findByCuisine(type);
                } else {
                    items = repository.getAll();
                }
                sendJson(resp, serializer.listToString(items));
            } else {
                String name = decodePathParam(pathInfo.substring(1));
                Optional<Restaurant> item = repository.findByIdentity(name);

                if (item.isPresent()) {
                    sendJson(resp, serializer.toString(item.get()));
                } else {
                    sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Restaurant not found");
                }
            }
        } catch (IllegalArgumentException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid cuisine type");
        } catch (Exception e) {
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String body = getRequestBody(req);
            Restaurant newItem = serializer.fromString(body, Restaurant.class);

            boolean added = repository.add(newItem);
            if (added) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                sendJson(resp, serializer.toString(newItem));
            } else {
                sendError(resp, HttpServletResponse.SC_CONFLICT, "Restaurant already exists");
            }
        } catch (Exception e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid Data: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "ID required");
            return;
        }

        String name = decodePathParam(pathInfo.substring(1));
        boolean removed = repository.removeByIdentity(name);

        if (removed) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Restaurant not found");
        }
    }
}