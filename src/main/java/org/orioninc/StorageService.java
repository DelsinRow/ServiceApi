package org.orioninc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class StorageService implements QuestionsSubmitter {
    private final HttpClient client;
    private final StackOverflowService stackOverflowService;
    public static String apiEndpointStorageservice = System.getenv("STORAGESERVICE_API_ENDPOINT");
    public  String storageservice_document_link = System.getenv("STORAGESERVICE_API_ENDPOINT") + "/document/";    public StorageService(HttpClient client, StackOverflowService stackOverflowService) {
        this.client = client;
        this.stackOverflowService = stackOverflowService;
    }

    String customTitle (List<String> ListOfLanguage) {
        StringBuilder sb = new StringBuilder();
        for(String language : ListOfLanguage) {
            sb.append(language).append(", ");
        }
        sb.deleteCharAt(sb.length()-2);
        return  ConstantValues.STACKOVERFLOW_NUMBER_OF_QUESTIONS + " questions by next language: " + sb.toString();
    }

    public String submitDocument(String allStringQuestions) throws IOException, InterruptedException {

        HttpRequest requestPost = HttpRequest.newBuilder()
                .uri(URI.create(apiEndpointStorageservice + ConstantValues.STORAGESERVICE_POST_ROUTE))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .setHeader("title", customTitle(stackOverflowService.getListOfLanguageInRequest()))
                .setHeader("source", ConstantValues.SERVICE_API_HEADER_SOURCE)
                .POST(HttpRequest.BodyPublishers.ofString(allStringQuestions))
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
