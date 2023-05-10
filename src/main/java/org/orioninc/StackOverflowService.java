package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

public class StackOverflowService {
    public List<Questions> allQuestionsList = new ArrayList<>();
    public List<String> listOfLanguageInRequest = new ArrayList<>();
    private HttpClient client;
    private static String urlWithLanguage(String language) {
        return ConstantValues.API_ENDPOINT_STACKOVERFLOW + "/2.3/questions?pagesize=" + ConstantValues.NUMBER_OF_QUESTIONS_FROM_STACKOVERFLOW + "&order=desc&sort=creation&tagged=" + language + "&site=stackoverflow&filter=!.yIW41g8Y3qudKNa";
    }

    public StackOverflowService(HttpClient client) {
        this.client = client;
    }

    private HttpRequest requestConnection(Languages language) {
        addLanguageInListOfLanguageInRequest(language);
        return HttpRequest.newBuilder()
                .uri(URI.create(urlWithLanguage(language.getLanguageRequest())))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .GET()
                .build();
    }

    private List<String> questionsList(Languages language, StackOverflowItemsArray stackOverflowItemsArray) throws JsonProcessingException {
        List<String> questionsList = new ArrayList<>();
        int questionIndex = 1;
        questionsList.add("-----> " + language.getLanguageName() + " <-----");
        for (StackOverFlowItemsWrapper items : stackOverflowItemsArray.getItems()) {
            questionsList.add(questionIndex + ") " + URLDecoder.decode(items.getTitle(), StandardCharsets.UTF_8));
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
//                            System.out.println("byte array: " + byteArray);
                            return new GZIPInputStream(new ByteArrayInputStream(byteArray.body()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .thenApply(bytes -> {
                        try {
//                            System.out.println("bytes: " + bytes);
                            return new String(bytes.readAllBytes(), StandardCharsets.UTF_8);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .thenApply(string -> {
                        try {
//                            System.out.println("string: " + string);
                            return objectMapper.readValue(string, StackOverflowItemsArray.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .thenApply(stringArray -> {
                        try {
                            return new Questions(questionsList(language, stringArray));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return questions;
    }

    private void addLanguageInListOfLanguageInRequest(Languages language) {
        listOfLanguageInRequest.add(language.getLanguageName());
    }

    public List<String> getListOfLanguageInRequest() {
        return listOfLanguageInRequest;
    }

    public void addQuestionsToFinalList(CompletableFuture<Questions> cf) {
        Questions question;
        try {
            question = cf.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        allQuestionsList.add(question);
    }

    public List<Questions> getAllQuestionsList() {
        return allQuestionsList;
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

    public static String getUrlWithLanguage(String language) {
        return urlWithLanguage(language);
    }


}
