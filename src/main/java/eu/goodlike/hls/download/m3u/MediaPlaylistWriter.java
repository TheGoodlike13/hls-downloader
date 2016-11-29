package eu.goodlike.hls.download.m3u;

import okhttp3.HttpUrl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

/**
 * <pre>
 * Defines how to write various parts of a hls media playlist into some kind of a container
 *
 * Implementations of this writer do not check whether the order of writing or such is correct, they merely dump the
 * data
 *
 * Do not forget to call {@link AutoCloseable#close()} when done writing!
 * </pre>
 * @param <WritingContainer> type of container written into (i.e. {@link Path} for files
 */
public interface MediaPlaylistWriter<WritingContainer> extends AutoCloseable {

    /**
     * Writes the tag which should be present at the start of a media playlist
     *
     * @return this writer
     * @throws IOException if writing fails
     */
    MediaPlaylistWriter writeStart() throws IOException;

    /**
     * Writes target duration tag; there should be one of these tags per playlist, just after the start tag
     *
     * @param targetDuration target duration of this playlist
     * @return this writer
     * @throws IOException if writing fails
     * @throws NullPointerException if targetDuration is null
     */
    MediaPlaylistWriter writeTargetDuration(BigDecimal targetDuration) throws IOException;

    /**
     * Writes a duration tag; there should be one of these tags before every part of the stream
     *
     * @param duration duration of the next part
     * @return this writer
     * @throws IOException if writing fails
     * @throws NullPointerException if duration is null
     */
    MediaPlaylistWriter writeDurationTag(BigDecimal duration) throws IOException;

    /**
     * Writes an url tag; there should be one of these tags for every part
     *
     * @param url url of a part
     * @return this writer
     * @throws IOException if writing fails
     * @throws NullPointerException if url is null
     */
    MediaPlaylistWriter writeLocation(HttpUrl url) throws IOException;

    /**
     * Writes the tag which signifies the end of media playlist
     *
     * @return reference to the container
     * @throws IOException if writing fails
     */
    WritingContainer writeEnd() throws IOException;

}
