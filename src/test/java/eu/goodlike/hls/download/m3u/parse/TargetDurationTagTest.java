package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

public class TargetDurationTagTest {

    private static final BigDecimal DURATION = BigDecimal.ONE;

    private final HlsBuilder builder = Mockito.mock(HlsBuilder.class);

    @Test
    public void tagWritesItself() {
        Mockito.when(builder.setTargetDuration(DURATION)).thenReturn(builder);

        new TargetDurationTag(DURATION).extractDataInto(builder);

        Mockito.verify(builder).setTargetDuration(DURATION);
    }

}
