package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.http.HttpStreamer;
import eu.goodlike.str.Str;
import okhttp3.HttpUrl;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.math.BigDecimal;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class StreamingHlsParserTest {

    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");

    private final HttpStreamer httpStreamer = Mockito.mock(HttpStreamer.class);
    private final HlsParser hlsParser = new StreamingHlsParser(httpStreamer);

    private InputStream streamFromString(String string) {
        return IOUtils.toInputStream(string, UTF_8);
    }

    @Test
    public void parsingOfMasterPlaylist() {
        MediaTag mediaTag = new MediaTag("source");
        StreamInfoTag streamInfoTag = new StreamInfoTag("1920x1080");
        UrlTag urlTag = new UrlTag(URL);

        String masterPlaylist = Str.of()
                .and(mediaTag, System.lineSeparator())
                .and(streamInfoTag, System.lineSeparator())
                .and(urlTag, System.lineSeparator())
                .toString();

        Mockito.when(httpStreamer.getStream(URL)).thenReturn(streamFromString(masterPlaylist));

        assertThat(hlsParser.parse(URL))
                .containsExactly(mediaTag, streamInfoTag, urlTag);
    }

    @Test
    public void parsingOfMediaPlaylist() {
        TargetDurationTag targetDurationTag = new TargetDurationTag(BigDecimal.TEN);
        StreamPartDurationTag streamPartDurationTag = new StreamPartDurationTag(BigDecimal.ONE);
        UrlTag urlTag = new UrlTag(URL);

        String mediaPlaylist = Str.of()
                .and(targetDurationTag, System.lineSeparator())
                .and(streamPartDurationTag, System.lineSeparator())
                .and(urlTag, System.lineSeparator())
                .toString();

        Mockito.when(httpStreamer.getStream(URL)).thenReturn(streamFromString(mediaPlaylist));

        assertThat(hlsParser.parse(URL))
                .containsExactly(targetDurationTag, streamPartDurationTag, urlTag);
    }

}