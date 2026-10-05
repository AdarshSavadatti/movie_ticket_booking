package com.adarsh.moviebooking;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConcurrencyTestSimple {


    private static final int TOTAL_REQUESTS = 20;


    private static final String URL = "http://localhost:8080/api/seats/hold";
    private static final String BODY = "{\"seatId\":5}";

    public static void main(String[] args) throws InterruptedException {
        int[] statusCodes = new int[TOTAL_REQUESTS];
        Thread[] threads = new Thread[TOTAL_REQUESTS];

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            final int myIndex = i;
            threads[i] = new Thread(() -> {
                int result = sendLockRequest();
                statusCodes[myIndex] = result;
            });
        }

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            threads[i].start();
        }

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            threads[i].join();
        }

        int successCount = 0;
        int rejectedCount = 0;

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            int code = statusCodes[i];
            System.out.println("Request " + i + " -> status code: " + code);

            if (code == 200 || code == 201) {
                successCount = successCount + 1;
            } else {
                rejectedCount = rejectedCount + 1;
            }
        }

        // Step 7: print the final proof.
        System.out.println("----------------------------------------");
        System.out.println("Total requests sent: " + TOTAL_REQUESTS);
        System.out.println("Successful locks:    " + successCount);
        System.out.println("Rejected attempts:   " + rejectedCount);

        if (successCount == 1) {
            System.out.println("PASS: exactly 1 request won the seat. No double-booking.");
        } else {
            System.out.println("CHECK: expected exactly 1 success, got " + successCount);
        }
    }

    private static int sendLockRequest() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(BODY))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            System.out.println("  -> body: " + response.body());

            return response.statusCode();

        } catch (Exception e) {
            System.out.println("Request failed: " + e.getMessage());
            return -1;
        }
    }
}
