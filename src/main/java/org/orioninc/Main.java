package org.orioninc;

import java.io.IOException;
import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String allQuestions;
        HttpClient client = HttpClient.newHttpClient();

        StackOverflowService stackOverflowService = new StackOverflowService();
        stackOverflowService.getQuestions(client, Languages.JAVA);
        stackOverflowService.getQuestions(client, Languages.C_SHARP);
        stackOverflowService.getQuestions(client, Languages.C_PLUS_PLUS);
        stackOverflowService.getQuestions(client, Languages.CSS);
        stackOverflowService.getQuestions(client, Languages.HTML);
        stackOverflowService.getQuestions(client, Languages.JAVASCRIPT);
        stackOverflowService.getQuestions(client, Languages.PHP);
        stackOverflowService.getQuestions(client, Languages.PYTHON);
        stackOverflowService.getQuestions(client, Languages.RUBY);
        stackOverflowService.getQuestions(client, Languages.SQL);

        allQuestions = QuestionsOutput.Questions(stackOverflowService.getAllQuestionsList());

        HastebinService hastebinService = new HastebinService();
        hastebinService.submitDocument(allQuestions);

    }
}