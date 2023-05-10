package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class StorageService implements ServicesInterface{
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
                .uri(URI.create(ConstantValues.API_ENDPOINT_STORAGESERVICE + ConstantValues.POST_ROUTE_STORAGESERVICE))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .setHeader("title", customTitle(stackOverflowService.getListOfLanguageInRequest()))
                .setHeader("source", ConstantValues.HEADER_SOURCE)
                .POST(HttpRequest.BodyPublishers.ofString(allStringQuestions))
                .build();

        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        String StorageString = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        ServiceResponse serviceResponse = objectMapper.readValue(StorageString, ServiceResponse.class);
        return serviceResponse.getKey();
    }
}
