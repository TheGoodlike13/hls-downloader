package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import eu.goodlike.neat.Null;
import eu.goodlike.str.Str;

import java.util.Objects;

import static eu.goodlike.hls.download.m3u.M3U8Defaults.M3U8_MASTER_STREAM_INFO_RESOLUTION_ATTRIBUTE;
import static eu.goodlike.hls.download.m3u.M3U8Defaults.M3U8_MASTER_STREAM_INFO_TAG;

/**
 * Hls tag: EXT-X-STREAM-INF
 */
public final class StreamInfoTag implements HlsTag {

    @Override
    public void extractDataInto(HlsBuilder<?> builder) {
        Null.check(builder).as("builder");
        if (resolution != null)
            builder.setNextPlaylistResolution(resolution);

        if (audioStreamId != null)
            builder.setNextAudioStreamId(audioStreamId);
    }

    // CONSTRUCTORS

    public StreamInfoTag(String resolution, String audioStreamId) {
        this.resolution = resolution;
        this.audioStreamId = audioStreamId;
    }

    // PRIVATE

    private final String resolution;
    private final String audioStreamId;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return Str.format("{}{}\"{}\"",
                M3U8_MASTER_STREAM_INFO_TAG,
                M3U8_MASTER_STREAM_INFO_RESOLUTION_ATTRIBUTE,
                resolution);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StreamInfoTag)) return false;
        StreamInfoTag that = (StreamInfoTag) o;
        return Objects.equals(resolution, that.resolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resolution);
    }

}
