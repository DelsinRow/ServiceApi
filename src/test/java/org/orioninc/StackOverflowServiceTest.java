package org.orioninc;

import org.junit.Assert;
import org.junit.Test;
import org.orioninc.Languages;
import org.orioninc.Questions;
import org.orioninc.StackOverflowService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPOutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StackOverflowServiceTest {

    private HttpClient mockClient = mock(HttpClient.class);
    private StackOverflowService stackOverflowService = new StackOverflowService(mockClient);
    private Languages language = Languages.JAVA;
    private String word = "\"title\"";

    @Test
    public void questionsListTest() throws InterruptedException, ExecutionException {
        CompletableFuture<Questions> questions;
        int indexArrayTest = 1;

        HttpResponse<byte[]> mockResponse = mock(HttpResponse.class);
        String responseJson = "{\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"creation_date\": 1680103377,\n" +
                "      \"question_id\": 75879213,\n" +
                "      \"title\": \"Load resources file before all test execution in JUnit\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
//

        byte[] dataToCompress = responseJson.getBytes(StandardCharsets.ISO_8859_1);
        byte[] result = new byte[]{};
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(dataToCompress.length);
             GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(dataToCompress);
            gzipOS.close();
            result = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        when(mockResponse.body()).thenReturn(result);
        when(mockClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(mockResponse));
        questions = stackOverflowService.sendRequest(Languages.JAVA);

        long count = Arrays
                .stream(responseJson.split("\\s+"))
                .filter(word::equals)
                .count() + 1;
        Assert.assertNotNull(questions.get().title());
        Assert.assertEquals(("-----> " + language.getName() + " <-----"), questions.get().title().get(0));
        Assert.assertEquals((indexArrayTest + ") Load resources file before all test execution in JUnit").length(), questions.get().title().get(indexArrayTest).length());
        Assert.assertEquals(count, questions.get().title().size() - 1);

    }
}

