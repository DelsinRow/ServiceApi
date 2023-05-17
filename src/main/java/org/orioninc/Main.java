package org.orioninc;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final long start = System.nanoTime();
        String key;
        List<Questions> allQuestionList;
        QuestionsSubmitter questionsSubmitter;

        ServiceManager serviceManager = new ServiceManager();

        questionsSubmitter = serviceManager.verifyAndChoiceService();
        allQuestionList = serviceManager.allQuestionsList(args);
        key = questionsSubmitter.submitDocument(allQuestionList);
        questionsSubmitter.printFinalLink(key);

        System.out.printf("Done in %dms\n", Duration.ofNanos(System.nanoTime() - start).toMillis());
    }

}