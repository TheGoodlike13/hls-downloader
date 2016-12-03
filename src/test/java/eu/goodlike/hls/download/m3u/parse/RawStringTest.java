package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import okhttp3.HttpUrl;
import org.junit.Test;
import org.mockito.Mockito;

public class RawStringTest {

    private static final String FILENAME = "stream.m3u";
    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");

    private final HlsBuilder builder = Mockito.mock(HlsBuilder.class);

    @Test
    public void tagWritesItselfAsString() {
        Mockito.when(builder.setNextString(FILENAME)).thenReturn(builder);

        new RawString(FILENAME).extractDataInto(builder);

        Mockito.verify(builder).setNextString(FILENAME);
    }

    @Test
    public void tagWritesItselfAsUrl() {
        Mockito.when(builder.setNextUrl(URL)).thenReturn(builder);

        new RawString(URL.toString()).extractDataInto(builder);

        Mockito.verify(builder).setNextUrl(URL);
    }

}
