package ua.food_delivery.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.food_delivery.model.Customer;
import ua.food_delivery.repository.CustomerRepository;
import ua.food_delivery.serializer.JsonDataSerializer;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/customers", "/customers/*"})
public class CustomerServlet extends BaseServlet {

    private CustomerRepository repository;
    private JsonDataSerializer<Customer> serializer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = (CustomerRepository) config.getServletContext().getAttribute("customerRepository");
        serializer = new JsonDataSerializer<>();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                String address = req.getParameter("address");
                if (address != null) {
                    sendJson(resp, serializer.listToString(repository.findByAddressContaining(address)));
                } else {
                    sendJson(resp, serializer.listToString(repository.getAll()));
                }
            } else {
                String id = decodePathParam(pathInfo.substring(1));
                Optional<Customer> item = repository.findByIdentity(id);

                if (item.isPresent()) {
                    sendJson(resp, serializer.toString(item.get()));
                } else {
                    sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Customer not found");
                }
            }
        } catch (Exception e) {
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String body = getRequestBody(req);
            Customer newItem = serializer.fromString(body, Customer.class);

            boolean added = repository.add(newItem);
            if (added) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                sendJson(resp, serializer.toString(newItem));
            } else {
                sendError(resp, HttpServletResponse.SC_CONFLICT, "Customer already exists");
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

        String id = decodePathParam(pathInfo.substring(1));
        boolean removed = repository.removeByIdentity(id);

        if (removed) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Customer not found");
        }
    }
}