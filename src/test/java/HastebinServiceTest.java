import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.orioninc.HastebinService;
import org.orioninc.ServiceResponse;

import java.io.IOException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HastebinServiceTest {
    private static HttpClient mockClient;

    @BeforeClass
    public static void createMockClient() {
        mockClient = mock(HttpClient.class);
    }

    @Test
    public void submitDocumentReturnAnswer() throws IOException, InterruptedException {
        String testQuestion = "This is test question";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn("{ \"key\": \"testvalue\" }");

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        HastebinService hastebinService = new HastebinService(mockClient);
        hastebinService.submitDocument(testQuestion);
        String json = mockResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper
                .readValue(json, ServiceResponse.class)
                .getKey();
        Assert.assertEquals("testvalue", result);
    }

}
