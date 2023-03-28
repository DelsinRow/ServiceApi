import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.orioninc.HastebinService;

import java.io.IOException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * POST bodypublisher not null
 * запрос не уходит на сервер, если не указан Токен
 * Возвращаемое боди не пустое
 * ответ содержит "https://hastebin.com/share/"
 * response.body() not NULL
 */

public class HastebinServiceTest {
    String testQuestion = "This is test question";

    @Test
    public void submitDocumentReturnAnswer() throws IOException, InterruptedException {
        HttpClient mockClient = mock(HttpClient.class);

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn("{ \"key\": \"testvalue\" }");

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        HastebinService hastebinService = new HastebinService(mockClient);
        hastebinService.submitDocument(testQuestion);
        String json = mockResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper
                .readValue(json, HastebinService.HastebinResponse.class)
                .getKey();
        Assert.assertEquals(result, "testvalue");
    }

}
