package org.orioninc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

public class StorageService {
    private final static String API_ENDPOINTS_STORAGESERVICE = System.getenv("API_ENDPOINT_STORAGESERVICE");
    private final static String POST_ROUTE_STORAGESERVICE = "/api/document";
    private final HttpClient client;
    private final StackOverflowService stackOverflowService;

    public StorageService(HttpClient client, StackOverflowService stackOverflowService) {
        this.client = client;
        this.stackOverflowService = stackOverflowService;
    }

    String customTitle (List<String> ListOfLanguage) {
        StringBuilder sb = new StringBuilder();
        for(String language : ListOfLanguage) {
            sb.append(language).append(", ");
        }
        sb.deleteCharAt(sb.length()-2);
        return  "10 questions by next language: " + sb.toString();
    }

    public String submitDocument(String allStringQuestions) throws IOException, InterruptedException {

        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINTS_STORAGESERVICE + POST_ROUTE_STORAGESERVICE))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .setHeader("title", customTitle(stackOverflowService.getListOfLanguageInRequest()))
                .POST(HttpRequest.BodyPublishers.ofString(allStringQuestions))
                .build();

        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        String StorageString = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        StorageServiceResponse storageServiceResponse = objectMapper.readValue(StorageString, StorageServiceResponse.class);
        return storageServiceResponse.getKey();
    }

    public static class StorageServiceResponse {
        private String key;
        private String title;
        private String date;

        @JsonProperty("key")
        public String getKey() {
            return key;
        }

        @JsonProperty("title")
        public String getTitle() {
            return title;
        }

        @JsonProperty("date")
        public String getDate() {
            return date;
        }
    }
}
