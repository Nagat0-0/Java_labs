package ua.food_delivery;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Main {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) {
        System.out.println("=== FOOD DELIVERY API CLIENT DEMO ===\n");
        System.out.println("NOTE: Make sure the server is running via 'gradle appRun' before running this Main class.\n");

        try {
            System.out.println("--- 1. GET /menuitems ---");
            sendRequest(HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/menuitems"))
                    .GET()
                    .build());

            System.out.println("\n--- 2. GET /restaurants?cuisine=ITALIAN ---");
            sendRequest(HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/restaurants?cuisine=ITALIAN"))
                    .GET()
                    .build());

            System.out.println("\n--- 3. POST /customers ---");
            String newCustomerJson = """
                    {
                        "firstName": "Api",
                        "lastName": "Tester",
                        "address": "Web Street 42"
                    }
                    """;
            sendRequest(HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/customers"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(newCustomerJson))
                    .build());

            System.out.println("\n--- 4. GET /customers/Api%20Tester ---");
            sendRequest(HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/customers/Api%20Tester"))
                    .GET()
                    .build());

            System.out.println("\n--- 5. DELETE /customers/Api%20Tester ---");
            sendRequest(HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/customers/Api%20Tester"))
                    .DELETE()
                    .build());

        } catch (Exception e) {
            System.err.println("Error connecting to server. Is it running?");
            e.printStackTrace();
        }
    }

    private static void sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Request: " + request.method() + " " + request.uri());
            System.out.println("Response Status: " + response.statusCode());
            System.out.println("Response Body:");
            System.out.println(response.body());

        } catch (Exception e) {
            System.err.println("Failed to send request: " + e.getMessage());
        }
    }
}