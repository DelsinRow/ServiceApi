package org.orioninc;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String key;
        String allQuestions;
        HttpClient client = HttpClient.newHttpClient();
        final long start = System.nanoTime();

        StackOverflowService stackOverflowService = new StackOverflowService(client);

        List<CompletableFuture<Questions>> listOfCompletableFutureOfQuestions = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            for (Languages language : Languages.values()) {
                if (args[i].equalsIgnoreCase(language.getLanguageName())) {
                    listOfCompletableFutureOfQuestions.add(stackOverflowService.sendRequest(language));
                }
            }
        }
        for (CompletableFuture<Questions> completableFutureOfQuestions : listOfCompletableFutureOfQuestions) {
            stackOverflowService.addQuestionsToFinalList(completableFutureOfQuestions);
        }

        allQuestions = QuestionsOutput.questions(stackOverflowService.getAllQuestionsList());

        if (System.getenv("SERVICE").equals("storage")) {
            StorageService storageService = new StorageService(client, stackOverflowService);
            key = storageService.submitDocument(allQuestions);
            System.out.println("Link: " + System.getenv("FQDN") + "/document/" + key);
        } else if (System.getenv("SERVICE").equals("hastebin")) {
            HastebinService hastebinService = new HastebinService(client);
            key = hastebinService.submitDocument(allQuestions);
            System.out.println("Link: " + "https://hastebin.com/share/" + key);
        }


        System.out.printf("Done in %dms\n", Duration.ofNanos(System.nanoTime() - start).toMillis());
    }
}