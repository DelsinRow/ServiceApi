package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

public class StackOverflowService {
    private HttpClient client;

    private static String urlWithLanguage(String language) {
        return ConstantValues.STACKOVERFLOW_API_ENDPOINT + "/2.3/questions?pagesize=" + ConstantValues.STACKOVERFLOW_NUMBER_OF_QUESTIONS + "&order=desc&sort=creation&tagged=" + URLEncoder.encode(language, StandardCharsets.UTF_8) + "&site=stackoverflow&filter=!.yIW41g8Y3qudKNa";
    }

    public StackOverflowService(HttpClient client) {
        this.client = client;
    }

    private HttpRequest requestConnection(Languages language) {
        return HttpRequest.newBuilder()
                .uri(URI.create(urlWithLanguage(language.getName())))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .GET()
                .build();
    }

    private List<String> questionsList(Languages language, StackOverflowItemsArray stackOverflowItemsArray) throws JsonProcessingException {
        List<String> questionsList = new ArrayList<>();
        int questionIndex = 1;
        questionsList.add("-----> " + language.getName() + " <-----");
        for (StackOverFlowItemsWrapper items : stackOverflowItemsArray.getItems()) {
            questionsList.add(questionIndex + ") " + StringEscapeUtils.unescapeHtml4(items.getTitle()));
            questionIndex++;
        }
        return questionsList;
    }

    public CompletableFuture<Questions> sendRequest(Languages language) {
        HttpRequest requestGet = requestConnection(language);
        ObjectMapper objectMapper = new ObjectMapper();

        CompletableFuture<Questions> questions;
        try {
            questions = client.sendAsync(requestGet, HttpResponse.BodyHandlers.ofByteArray())
                    .thenApply(byteArray -> {
                        try {
                            return new GZIPInputStream(new ByteArrayInputStream(byteArray.body()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .thenApply(bytes -> {
                        try {
                            return new String(bytes.readAllBytes(), StandardCharsets.UTF_8);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .thenApply(string -> {
                        try {
                            return objectMapper.readValue(string, StackOverflowItemsArray.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .thenApply(stringArray -> {
                        try {
                            return new Questions(questionsList(language, stringArray), language.getName());
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return questions;
    }

    public List<Questions> getQuestions(Set<Languages> languages) {
        List<CompletableFuture<Questions>> listOfCompletableFutureOfQuestions = new ArrayList<>();
        for (Languages language : languages) {
            listOfCompletableFutureOfQuestions.add(sendRequest(language));
        }
        CompletableFuture.allOf(listOfCompletableFutureOfQuestions.toArray(CompletableFuture[]::new));

        List<Questions> responses = new ArrayList<>();
        listOfCompletableFutureOfQuestions.forEach(f -> {
            try {
                responses.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        return responses;
    }


    public static class StackOverflowItemsArray {
        List<StackOverFlowItemsWrapper> items;

        public StackOverflowItemsArray() {
        }

        @JsonProperty("items")
        public List<StackOverFlowItemsWrapper> getItems() {
            return items;
        }

        public void setItems(List<StackOverFlowItemsWrapper> items) {
            this.items = items;
        }

    }

    private static class StackOverFlowItemsWrapper {
        private String creationDate;
        private String questionId;
        private String title;

        public StackOverFlowItemsWrapper() {
        }

        @JsonProperty("creation_date")
        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        @JsonProperty("question_id")
        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        @JsonProperty("title")
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
