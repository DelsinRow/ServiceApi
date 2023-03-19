package org.orioninc;

import java.io.IOException;
import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        StackOverflowService stackOverflowService = new StackOverflowService();
        stackOverflowService.submitDocument(client, Languages.JAVA);
        stackOverflowService.submitDocument(client, Languages.C_SHARP);
        stackOverflowService.submitDocument(client, Languages.C_PLUS_PLUS);
        stackOverflowService.submitDocument(client, Languages.CSS);
        stackOverflowService.submitDocument(client, Languages.HTML);
        stackOverflowService.submitDocument(client, Languages.JAVASCRIPT);
        stackOverflowService.submitDocument(client, Languages.PHP);
        stackOverflowService.submitDocument(client, Languages.PYTHON);
        stackOverflowService.submitDocument(client, Languages.RUBY);
        stackOverflowService.submitDocument(client, Languages.SQL);

        HastebinService hastebinService = new HastebinService();
        hastebinService.submitDocument(client);

    }
}