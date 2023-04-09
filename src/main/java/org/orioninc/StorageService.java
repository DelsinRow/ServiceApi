package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StorageService {
    private final static String API_ENDPOINTS_STORAGESERVICE = "http://localhost:8080";
    private final static String POST_ROUTE_STORAGESERVICE = "/document/post";
    private final HttpClient client;

    public StorageService(HttpClient client) {
        this.client = client;
    }

    public String submitDocument(String allStringQuestions) throws IOException, InterruptedException {

        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINTS_STORAGESERVICE + POST_ROUTE_STORAGESERVICE))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(allStringQuestions))
                .build();

        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        String HastebinString = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        StorageServiceResponse storageServiceResponse = objectMapper.readValue(HastebinString, StorageServiceResponse.class);
        return storageServiceResponse.getKey();
    }

    public static class StorageServiceResponse {
        private String key;

        @JsonProperty("key")
        public String getKey() {
            return key;
        }
    }
}
