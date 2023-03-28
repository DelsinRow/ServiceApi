package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HastebinService {
    private final static String BEARER_TOKEN_HASTEBIN = "Bearer " + System.getenv("TOKEN_HASTEBIN");
    private final static String API_ENDPOINTS_HASTEBIN = "https://hastebin.com";
    private final static String POST_ROUTE_HASTEBIN = "/documents";
    private HttpClient client = HttpClient.newHttpClient();

    public HastebinService(HttpClient client) {
        this.client = client;
    }

    public String submitDocument(String allStringQuestions) throws IOException, InterruptedException {

        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINTS_HASTEBIN + POST_ROUTE_HASTEBIN))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .header("Authorization", BEARER_TOKEN_HASTEBIN)
                .POST(HttpRequest.BodyPublishers.ofString(allStringQuestions))
                .build();

        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        String HastebinString = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        HastebinResponse hastebinResponse = objectMapper.readValue(HastebinString, HastebinResponse.class);
        return hastebinResponse.getKey();
    }

    public static class HastebinResponse {
        private String key;

        public HastebinResponse() {
        }

        @JsonProperty("key")
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }
}
