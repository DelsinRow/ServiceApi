package org.orioninc;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String token = System.getenv("TOKEN");
        String key;
        String allQuestions;
        HttpClient client = HttpClient.newHttpClient();
        final long start = System.nanoTime();

        StackOverflowService stackOverflowService = new StackOverflowService(client);
        HastebinService hastebinService = new HastebinService(client);
        StorageService storageService = new StorageService(client);

        CompletableFuture<Questions> python = stackOverflowService.sendRequest(Languages.PYTHON);
        CompletableFuture<Questions> java = stackOverflowService.sendRequest(Languages.JAVA);
        CompletableFuture<Questions> sql = stackOverflowService.sendRequest(Languages.SQL);
        CompletableFuture<Questions> css = stackOverflowService.sendRequest(Languages.CSS);
        CompletableFuture<Questions> cSharp = stackOverflowService.sendRequest(Languages.C_SHARP);
        CompletableFuture<Questions> cPlus = stackOverflowService.sendRequest(Languages.C_PLUS_PLUS);
        CompletableFuture<Questions> html = stackOverflowService.sendRequest(Languages.HTML);
        CompletableFuture<Questions> ruby = stackOverflowService.sendRequest(Languages.RUBY);
        CompletableFuture<Questions> js = stackOverflowService.sendRequest(Languages.JAVASCRIPT);
        CompletableFuture<Questions> php = stackOverflowService.sendRequest(Languages.PHP);

        CompletableFuture.allOf(
                python, java, sql, css, cSharp, cPlus, html, ruby, js, php
        ).join();

        stackOverflowService.addQuestionsToFinalList(python);
        stackOverflowService.addQuestionsToFinalList(java);
        stackOverflowService.addQuestionsToFinalList(sql);
        stackOverflowService.addQuestionsToFinalList(css);
        stackOverflowService.addQuestionsToFinalList(cSharp);
        stackOverflowService.addQuestionsToFinalList(cPlus);
        stackOverflowService.addQuestionsToFinalList(html);
        stackOverflowService.addQuestionsToFinalList(ruby);
        stackOverflowService.addQuestionsToFinalList(js);
        stackOverflowService.addQuestionsToFinalList(php);

        allQuestions = QuestionsOutput.questions(stackOverflowService.getAllQuestionsList());

        if (token == null) {
            key = storageService.submitDocument(allQuestions);
            System.out.println("Link: " + "http://localhost:8080/document/get/" + key);
        } else {
            key = hastebinService.submitDocument(allQuestions);
            System.out.println("Link: " + "https://hastebin.com/share/" + key);
        }

        System.out.printf("Done in %dms\n", Duration.ofNanos(System.nanoTime() - start).toMillis());
    }
}