package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import eu.goodlike.neat.Null;

import java.math.BigDecimal;
import java.util.Objects;

import static eu.goodlike.hls.download.m3u.M3U8Defaults.M3U8_MEDIA_TARGET_DURATION_TAG;

/**
 * Hls tag: EXT-X-TARGETDURATION
 */
public final class TargetDurationTag implements HlsTag {

    @Override
    public void extractDataInto(HlsBuilder<?> builder) {
        Null.check(builder).as("builder");
        builder.setTargetDuration(targetDuration);
    }

    // CONSTRUCTORS

    public TargetDurationTag(BigDecimal targetDuration) {
        Null.check(targetDuration).as("targetDuration");
        this.targetDuration = targetDuration;
    }

    // PRIVATE

    private final BigDecimal targetDuration;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return M3U8_MEDIA_TARGET_DURATION_TAG + targetDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TargetDurationTag)) return false;
        TargetDurationTag that = (TargetDurationTag) o;
        return targetDuration == null ? that.targetDuration == null : targetDuration.compareTo(that.targetDuration) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetDuration == null ? 0 : targetDuration.doubleValue());
    }

}
