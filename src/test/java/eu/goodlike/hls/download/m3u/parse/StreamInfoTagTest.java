package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import org.junit.Test;
import org.mockito.Mockito;

public class StreamInfoTagTest {

    private static final String RESOLUTION = "1920x1080";

    private final HlsBuilder builder = Mockito.mock(HlsBuilder.class);

    @Test
    public void tagWritesItself() {
        Mockito.when(builder.setNextPlaylistResolution(RESOLUTION)).thenReturn(builder);

        new StreamInfoTag(RESOLUTION).extractDataInto(builder);

        Mockito.verify(builder).setNextPlaylistResolution(RESOLUTION);
    }

}
