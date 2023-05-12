package org.orioninc;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String key;
        String allQuestions;
        HttpClient client = HttpClient.newHttpClient();
        final long start = System.nanoTime();
        List<Questions> allQuestionsList = new ArrayList<>();

        StackOverflowService stackOverflowService = new StackOverflowService(client);

        List<CompletableFuture<Questions>> listOfCompletableFutureOfQuestions = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            for (Languages language : Languages.values()) {
                if (args[i].equalsIgnoreCase(language.getLanguageName())) {
                    listOfCompletableFutureOfQuestions.add(stackOverflowService.sendRequest(language));
//                    stackOverflowService.addLanguageInListOfLanguageInRequest(language);
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

        allQuestions = QuestionsOutput.questions(allQuestionsList);

        QuestionsSubmitter questionsSubmitter = null;           //remove null
        if (System.getenv("SERVICE").equals("storage")) {
            questionsSubmitter = new StorageService(client, stackOverflowService);
        } else if (System.getenv("SERVICE").equals("hastebin")) {
            questionsSubmitter = new HastebinService(client);
        }
        key = questionsSubmitter.submitDocument(allQuestions);
        questionsSubmitter.printFinalLink(key);

        System.out.printf("Done in %dms\n", Duration.ofNanos(System.nanoTime() - start).toMillis());
    }

}