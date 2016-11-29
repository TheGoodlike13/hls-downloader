package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import org.junit.Test;
import org.mockito.Mockito;

public class StringTagTest {

    private static final String FILENAME = "stream.m3u";

    private final HlsBuilder builder = Mockito.mock(HlsBuilder.class);

    @Test
    public void tagWritesItself() {
        Mockito.when(builder.setNextString(FILENAME)).thenReturn(builder);

        new StringTag(FILENAME).extractDataInto(builder);

        Mockito.verify(builder).setNextString(FILENAME);
    }

}
