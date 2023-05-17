package org.orioninc;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ServiceManager {
    private static QuestionsSubmitter questionsSubmitter;
    HttpClient client = HttpClient.newHttpClient();
    StackOverflowService stackOverflowService = new StackOverflowService(client);


    public QuestionsSubmitter verifyAndChoiceService() {
        if (System.getenv("SERVICE").equals("storage")) {
            questionsSubmitter = new StorageService(client);
        } else if (System.getenv("SERVICE").equals("hastebin")) {
            if (!System.getenv("TOKEN").isEmpty()) {
                questionsSubmitter = new HastebinService(client);
            }
        } else {
                System.out.println("ERROR. Unknown service: " + System.getenv("SERVICE"));
                System.exit(1);
            }
        return questionsSubmitter;
    }


    List<Questions> allQuestionsList (String[] args) {
            List<Questions> allQuestionsList = new ArrayList<>();

            List<CompletableFuture<Questions>> listOfCompletableFutureOfQuestions = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                for (Languages language : Languages.values()) {
                    if (args[i].equalsIgnoreCase(language.getLanguageName())) {
                        listOfCompletableFutureOfQuestions.add(stackOverflowService.sendRequest(language));
                    }
                }
            }
            CompletableFuture.allOf(listOfCompletableFutureOfQuestions.toArray(CompletableFuture[]::new));

            listOfCompletableFutureOfQuestions.forEach(f -> {
                try {
                    allQuestionsList.add(f.get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });

            return allQuestionsList;
        }
}
