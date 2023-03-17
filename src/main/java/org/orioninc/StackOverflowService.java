package org.orioninc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;


public class StackOverflowService {
    private final static String API_ENDPOINTS_STACKOVERFLOW = "https://api.stackexchange.com";
    private int numberOfQuestion = 10;

    private String urlWithLanguage(String language) {
        return API_ENDPOINTS_STACKOVERFLOW + "/2.3/questions?pagesize=" + numberOfQuestion + "&order=desc&sort=creation&tagged=" + language + "&site=stackoverflow&filter=!.yIW41g8Y3qudKNa";
    }

//    private String urlWithLanguage(String language, int numberOfQuestions) {
//        return API_ENDPOINTS_STACKOVERFLOW + "/2.3/questions?pagesize=" + numberOfQuestions + "&order=desc&sort=creation&tagged=" + language + "&site=stackoverflow&filter=!.yIW41g8Y3qudKNa";
//    }

    public void submitDocument(HttpClient client, String language) throws IOException, InterruptedException {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(urlWithLanguage(language)))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .GET()
                .build();

        HttpResponse<byte[]> responseStackOverFlow = client.send(requestGet, HttpResponse.BodyHandlers.ofByteArray());
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(responseStackOverFlow.body()));
        String stackoverFlowJsonString = new String(gis.readAllBytes(), StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();

        StackOverflowItemsArray stackOverflowItemsArray = objectMapper.readValue(stackoverFlowJsonString, StackOverflowItemsArray.class);

        for (StackOverFlowItemsWrapper items : stackOverflowItemsArray.getItems()) {
            System.out.println(items.getTitle());
        }
    }
}

