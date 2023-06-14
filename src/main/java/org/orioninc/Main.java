package org.orioninc;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private final static long start = System.nanoTime();

    public static void main(String[] args) throws IOException, InterruptedException {
        String service = System.getenv("SERVICE");
        String token = System.getenv("TOKEN");
        String storageserviceLink = System.getenv("STORAGESERVICE_API_ENDPOINT");

        if (!service.equalsIgnoreCase("storage") && !service.equalsIgnoreCase("hastebin")) {
            fatal("ERROR. Unknown service: " + service);
        }
        if (service.equalsIgnoreCase("storage") && storageserviceLink.isEmpty()) {
            fatal("Storage Service link is required for Storage Service");
        }
        if (service.equalsIgnoreCase("hastebin") && token.isEmpty()) {
            fatal("Token is required for Hastebin service");
        }

        Set<Languages> languages = new HashSet<>();
        for (String arg : args) {
            Languages lang = Languages.findByLanguage(arg);
            if (lang != null) {
                languages.add(lang);
            } else {
                System.out.println(arg + " will be ignored");
            }
        }

        System.out.println("Query languages: " + languages);

        HttpClient client = HttpClient.newHttpClient();
        StackOverflowService stackOverflowService = new StackOverflowService(client);
        QuestionsSubmitter questionsSubmitter = null;

        if (service.equalsIgnoreCase("storage")) {
            questionsSubmitter = new StorageService(client, storageserviceLink);
        } else if (service.equalsIgnoreCase("hastebin")) {
            questionsSubmitter = new HastebinService(client, token);
        }

        List<Questions> allQuestionList = stackOverflowService.getQuestions(languages);
        String link = questionsSubmitter.submitDocument(allQuestionList);
        System.out.println(link);

        System.out.printf("Done in %dms\n", Duration.ofNanos(System.nanoTime() - start).toMillis());
    }

    private static void fatal(String message) {
        System.err.println(message);
        System.exit(1);
    }

}