package org.orioninc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HastebinService implements QuestionsSubmitter {
    private final HttpClient client;
    private final String token;

    public HastebinService(HttpClient client, String token) {
        this.client = client;
        this.token = token;
    }

    @Override
    public String submitDocument(List<Questions> allQuestionsList) throws IOException, InterruptedException {
        String allQuestionsInString = QuestionsOutput.questions(allQuestionsList);

        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(ConstantValues.HASTEBIN_API_ENDPOINT + ConstantValues.HASTEBIN_POST_ROUTE))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(allQuestionsInString))
                .build();

        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        String HastebinString = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        ServiceResponse serviceResponse = objectMapper.readValue(HastebinString, ServiceResponse.class);
        return buildFinalLink(serviceResponse.key());
    }

    public String buildFinalLink(String key) {
        return ConstantValues.HASTEBIN_DOCUMENT_LINK + key;
    }
}
