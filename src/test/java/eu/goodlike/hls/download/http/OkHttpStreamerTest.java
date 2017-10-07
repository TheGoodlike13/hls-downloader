package eu.goodlike.hls.download.http;

import eu.goodlike.libraries.jackson.Json;
import eu.goodlike.libraries.okhttp.HttpRequestMaker;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class OkHttpStreamerTest {

    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");
    private static final String BODY_STRING = "body_string";

    private HttpRequestMaker httpRequestMaker;
    private HttpStreamer httpStreamer;

    @Before
    public void setUp() {
        httpRequestMaker = Mockito.mock(HttpRequestMaker.class);
        httpStreamer = new OkHttpStreamer(httpRequestMaker);
    }

    private Request newRequest(HttpUrl url) {
        return new Request.Builder().url(url).build();
    }

    private ResponseBody newResponseBody(Object responseBody) throws IOException {
        return ResponseBody.create(MediaType.parse("application/json"), Json.stringFrom(responseBody));
    }

    private Response newResponse(Request request, ResponseBody responseBody) {
        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(responseBody)
                .build();
    }

    @Test
    public void streamContainsContents() throws IOException {
        Request request = newRequest(URL);
        ResponseBody responseBody = newResponseBody(BODY_STRING);
        Response response = newResponse(request, responseBody);

        Mockito.when(httpRequestMaker.makeRequest(URL))
                .thenReturn(CompletableFuture.completedFuture(response));

        InputStream stream = httpStreamer.getStream(URL);
        InputStream expected = IOUtils.toInputStream(Json.stringFrom(BODY_STRING), UTF_8);
        assertThat(stream)
                .hasSameContentAs(expected);
    }

}
