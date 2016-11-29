package eu.goodlike.hls.download.m3u.data;

import com.google.common.base.MoreObjects;
import eu.goodlike.hls.download.m3u.MediaPlaylistWriter;
import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a single media stream part
 */
public final class MediaPart {

    /**
     * Writes this {@link MediaPart} into a {@link MediaPlaylistWriter}
     * @param mediaPlaylistWriter writer to write this part into
     * @throws IOException if writing fails
     * @throws NullPointerException if mediaPlaylistWriter is null
     */
    public void writeInto(MediaPlaylistWriter<?> mediaPlaylistWriter) throws IOException {
        Null.check(mediaPlaylistWriter).as("mediaPlaylistWriter");

        mediaPlaylistWriter.writeDurationTag(duration).writeLocation(url);
    }

    // CONSTRUCTORS

    public MediaPart(BigDecimal duration, HttpUrl url) {
        Null.check(duration, url).as("duration, url");

        this.duration = duration;
        this.url = url;
    }

    // PRIVATE

    private final BigDecimal duration;
    private final HttpUrl url;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("MediaPart")
                .add("duration", duration)
                .add("url", url)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaPart)) return false;
        MediaPart mediaPart = (MediaPart) o;
        return duration == null ? mediaPart.duration == null : duration.compareTo(mediaPart.duration) == 0 &&
                Objects.equals(url, mediaPart.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration == null ? 0 : duration.doubleValue(), url);
    }

}
