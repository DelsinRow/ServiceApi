package org.orioninc;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(HastebinService.requestPost, HttpResponse.BodyHandlers.ofString());
        String key = HastebinService.getKey(response);
        System.out.println("Body: " + response.body());
        System.out.println("Status: " + response.statusCode());

        System.out.println("Link: " + "https://hastebin.com/share/" + key + ".sql");
    }

}