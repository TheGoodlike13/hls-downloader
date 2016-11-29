package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import okhttp3.HttpUrl;
import org.junit.Test;
import org.mockito.Mockito;

public class UrlTagTest {

    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");

    private final HlsBuilder builder = Mockito.mock(HlsBuilder.class);

    @Test
    public void tagWritesItself() {
        Mockito.when(builder.setNextUrl(URL)).thenReturn(builder);

        new UrlTag(URL).extractDataInto(builder);

        Mockito.verify(builder).setNextUrl(URL);
    }

}
