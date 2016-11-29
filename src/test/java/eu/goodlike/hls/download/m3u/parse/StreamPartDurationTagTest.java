package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

public class StreamPartDurationTagTest {

    private static final BigDecimal DURATION = BigDecimal.ONE;

    private final HlsBuilder builder = Mockito.mock(HlsBuilder.class);

    @Test
    public void tagWritesItself() {
        Mockito.when(builder.setNextPartDuration(DURATION)).thenReturn(builder);

        new StreamPartDurationTag(DURATION).extractDataInto(builder);

        Mockito.verify(builder).setNextPartDuration(DURATION);
    }

}
