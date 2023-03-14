package org.orioninc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    private final static String BEARER_TOKEN_HASTEBIN = "Bearer 088f9f6c0d3647bd0fc66bdecfae4f3e889c67d8f8231180c209d23549d91a1df2f2dcf780b815f3aa03a186334a959c9ef0ba6c65d8c28a53e325969f59840e";
    private final static String API_ENDPOINTS_HASTEBIN = "https://hastebin.com";
    private final static String POST_ROUTE_HASTEBIN = "/documents";

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINTS_HASTEBIN + POST_ROUTE_HASTEBIN))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .header("Authorization", BEARER_TOKEN_HASTEBIN)
                .POST(HttpRequest.BodyPublishers.ofString("Hello world"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                .thenApply(HttpResponse::body)
//                .thenAccept((System.out::println))
//                .join();

        System.out.println("Body: " + response.body());
        System.out.println("Status: " + response.statusCode());

    }

}