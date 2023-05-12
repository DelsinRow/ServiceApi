package org.orioninc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HastebinService implements QuestionsSubmitter {
    public static String hastebinToken = "Bearer " + System.getenv("TOKEN");
    private final HttpClient client;

    public HastebinService(HttpClient client) {
        this.client = client;
    }

    @Override
    public String submitDocument(String allStringQuestions) throws IOException, InterruptedException {

        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(ConstantValues.HASTEBIN_API_ENDPOINT + ConstantValues.HASTEBIN_POST_ROUTE))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .header("Authorization", hastebinToken)
                .POST(HttpRequest.BodyPublishers.ofString(allStringQuestions))
                .build();

        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        String HastebinString = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        ServiceResponse serviceResponse = objectMapper.readValue(HastebinString, ServiceResponse.class);
        return serviceResponse.key();
    }

    @Override
    public void printFinalLink(String key){
        System.out.println(ConstantValues.HASTEBIN_DOCUMENT_LINK + key);
    }
}
