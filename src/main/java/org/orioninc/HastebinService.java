package org.orioninc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HastebinService {
    private final static String BEARER_TOKEN_HASTEBIN = System.getenv("TOKEN_HASTEBIN");
    private final static String API_ENDPOINTS_HASTEBIN = "https://hastebin.com";
    private final static String POST_ROUTE_HASTEBIN = "/documents";

    static HttpRequest requestPost = HttpRequest.newBuilder()
            .uri(URI.create(API_ENDPOINTS_HASTEBIN + POST_ROUTE_HASTEBIN))
            .header("Content-Type", "text/plain; charset=UTF-8")
            .header("Authorization", BEARER_TOKEN_HASTEBIN)
            .POST(HttpRequest.BodyPublishers.ofString("Hello world"))
            .build();

    public static String getKey(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = response.body();

        Map<String, String> map = objectMapper.readValue(requestBody, new TypeReference<Map<String, String>>() {
        });
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals("key")) {
                return entry.getValue();
            }
        }
        return null;
    }


}
