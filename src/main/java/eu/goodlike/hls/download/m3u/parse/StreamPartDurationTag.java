package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import eu.goodlike.neat.Null;
import eu.goodlike.str.Str;

import java.math.BigDecimal;
import java.util.Objects;

import static eu.goodlike.hls.download.m3u.M3U8Defaults.M3U8_MEDIA_PART_DURATION;

/**
 * Hls tag: EXTINF
 */
public final class StreamPartDurationTag implements HlsTag {

    @Override
    public void extractDataInto(HlsBuilder<?> builder) {
        Null.check(builder).as("builder");
        builder.setNextPartDuration(duration);
    }

    // CONSTRUCTORS

    public StreamPartDurationTag(BigDecimal duration) {
        Null.check(duration).as("duration");
        this.duration = duration;
    }

    // PRIVATE

    private final BigDecimal duration;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return Str.format("{}{},",
                M3U8_MEDIA_PART_DURATION,
                duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StreamPartDurationTag)) return false;
        StreamPartDurationTag that = (StreamPartDurationTag) o;
        return duration == null ? that.duration == null : duration.compareTo(that.duration) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration == null ? 0 : duration.doubleValue());
    }

}
