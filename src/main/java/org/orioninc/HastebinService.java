package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HastebinService implements ServicesInterface{
    private final HttpClient client;

    public HastebinService(HttpClient client) {
        this.client = client;
    }

    public String submitDocument(String allStringQuestions) throws IOException, InterruptedException {

        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(ConstantValues.API_ENDPOINT_HASTEBIN + ConstantValues.POST_ROUTE_HASTEBIN))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .header("Authorization", ConstantValues.BEARER_TOKEN_HASTEBIN)
                .POST(HttpRequest.BodyPublishers.ofString(allStringQuestions))
                .build();

        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        String HastebinString = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        ServiceResponse serviceResponse = objectMapper.readValue(HastebinString, ServiceResponse.class);
        return serviceResponse.getKey();
    }
}
