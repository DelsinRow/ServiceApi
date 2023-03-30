
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
    private static int numberOfQuestion = 10;
    private HttpClient mockClient = mock(HttpClient.class);
    private StackOverflowService stackOverflowService = new StackOverflowService(mockClient);
    private Languages language = Languages.JAVA;

    @Test
    public void correctUrlWithLanguage() {

        String expectedResult = "https://api.stackexchange.com/2.3/questions?pagesize=10&order=desc&sort=creation&tagged=java&site=stackoverflow&filter=!.yIW41g8Y3qudKNa";
        Assert.assertEquals(expectedResult, stackOverflowService.getUrlWithLanguage(language.getLanguageRequest()));
    }

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
        String word = "title\"question_id\"";
        long count = Arrays
                .stream(responseJson.split("\\s+"))
                .filter(word::equals)
                .count() + 1;
        Assert.assertNotNull(questions.get().title());
        Assert.assertEquals(questions.get().title().get(0), ("-----> " + language.getLanguageName() + " <-----"));
        Assert.assertEquals(questions.get().title().get(indexArrayTest).length(), (indexArrayTest + ") Load resources file before all test execution in JUnit").length());
        Assert.assertEquals(questions.get().title().size() - 1, count);

    }
}

