package ua.food_delivery.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.food_delivery.util.LoggerUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class BaseServlet extends HttpServlet {
    protected final Logger logger = LoggerUtil.getLogger();
    protected static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    protected void sendJson(HttpServletResponse resp, String json) throws IOException {
        resp.setContentType(CONTENT_TYPE_JSON);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(json);
    }

    protected void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType(CONTENT_TYPE_JSON);
        String json = String.format("{\"error\": \"%s\", \"status\": %d}", message, status);
        resp.getWriter().write(json);
    }

    protected String getRequestBody(HttpServletRequest req) throws IOException {
        try (BufferedReader reader = req.getReader()) {
            return reader.lines().collect(Collectors.joining());
        }
    }

    protected String decodePathParam(String param) {
        try {
            return java.net.URLDecoder.decode(param, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return param;
        }
    }
}