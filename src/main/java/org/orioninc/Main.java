package org.orioninc;

import java.io.IOException;
import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        StackOverflowService stackOverflowService = new StackOverflowService();
        stackOverflowService.submitDocument(client, "java");

        HastebinService hastebinService = new HastebinService();
        System.out.println(hastebinService.submitDocument(client));
    }
}