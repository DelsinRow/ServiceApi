package org.orioninc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;


public class StackOverflowService {
    private final static String API_ENDPOINTS_STACKOVERFLOW = "https://api.stackexchange.com";
    private static int numberOfQuestion = 10;
    public List<Questions> allQuestionsList = new ArrayList<>();
    private static String urlWithLanguage(String language) {
        return API_ENDPOINTS_STACKOVERFLOW + "/2.3/questions?pagesize=" + numberOfQuestion + "&order=desc&sort=creation&tagged=" + language + "&site=stackoverflow&filter=!.yIW41g8Y3qudKNa";
    }

    public void getQuestions(HttpClient client, Languages language) throws IOException, InterruptedException {
        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(URI.create(urlWithLanguage(language.getLanguageRequest())))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .GET()
                .build();

        HttpResponse<byte[]> responseStackOverFlow = client.send(requestGet, HttpResponse.BodyHandlers.ofByteArray());
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(responseStackOverFlow.body()));
        String stackoverFlowJsonString = new String(gis.readAllBytes(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        StackOverflowItemsArray stackOverflowItemsArray = objectMapper.readValue(stackoverFlowJsonString, StackOverflowItemsArray.class);
        allQuestionsList.add(new Questions(questionsList(language, stackOverflowItemsArray)));
        }

    private List<String> questionsList(Languages language, StackOverflowItemsArray stackOverflowItemsArray) throws UnsupportedEncodingException {
        List<String> questionsList = new ArrayList<>();
        int questionIndex = 1;

        questionsList.add("-----> " + language.getLanguageName() + " <-----");
        for (StackOverFlowItemsWrapper items : stackOverflowItemsArray.getItems()) {
            questionsList.add(questionIndex + ") " + items.getTitle().);                    //дописать декодер
            questionIndex++;
        }
        return questionsList;
    }

    public List<Questions> getAllQuestionsList() {
        return allQuestionsList;
    }

    static class StackOverflowItemsArray {
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

    static class StackOverFlowItemsWrapper {
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



