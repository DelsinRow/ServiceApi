package org.orioninc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class StorageService implements QuestionsSubmitter {
    private final HttpClient client;
    private final StackOverflowService stackOverflowService;
    public static String apiEndpointStorageservice = System.getenv("STORAGESERVICE_API_ENDPOINT");
    public  String storageservice_document_link = System.getenv("STORAGESERVICE_API_ENDPOINT") + "/document/";
    public StorageService(HttpClient client, StackOverflowService stackOverflowService) {
        this.client = client;
        this.stackOverflowService = stackOverflowService;
    }

    String customTitle (List<Questions> allQuestionsList) {
        List<String> listOfLanguage = new ArrayList<>();
        for(Questions questions : allQuestionsList) {
            listOfLanguage.add(questions.tag());
        }
        StringBuilder sb = new StringBuilder();
        for(String languageName : listOfLanguage) {
            sb.append(languageName).append(", ");
        }
        sb.deleteCharAt(sb.length()-2);
        return  ConstantValues.STACKOVERFLOW_NUMBER_OF_QUESTIONS + " questions by next language: " + sb.toString();
    }

    public String submitDocument(List<Questions> allQuestionsList) throws IOException, InterruptedException {
        String allQuestionsInString = QuestionsOutput.questions(allQuestionsList);

        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(apiEndpointStorageservice + ConstantValues.STORAGESERVICE_POST_ROUTE))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .setHeader("title", customTitle(allQuestionsList))
                .setHeader("source", ConstantValues.SERVICE_API_HEADER_SOURCE)
                .POST(HttpRequest.BodyPublishers.ofString(allQuestionsInString))
                .build();

        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        String StorageString = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        ServiceResponse serviceResponse = objectMapper.readValue(StorageString, ServiceResponse.class);
        return serviceResponse.key();
    }

    @Override
    public void printFinalLink(String key){
        System.out.println(storageservice_document_link + key);
    }
}
