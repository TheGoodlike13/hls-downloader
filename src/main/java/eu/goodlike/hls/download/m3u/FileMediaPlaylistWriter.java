package eu.goodlike.hls.download.m3u;

import eu.goodlike.io.FileAppender;
import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

import static eu.goodlike.hls.download.m3u.M3U8Defaults.*;

/**
 * Writes media playlist into a file
 */
public final class FileMediaPlaylistWriter implements MediaPlaylistWriter<Path> {

    @Override
    public MediaPlaylistWriter writeStart() throws IOException {
        fileAppender
                .appendLine(M3U8_FILE_START)
                .appendLine("");
        return this;
    }

    @Override
    public MediaPlaylistWriter writeTargetDuration(BigDecimal targetDuration) throws IOException {
        Null.check(targetDuration).as("targetDuration");

        fileAppender
                .append(M3U8_MEDIA_TARGET_DURATION_TAG).appendLine(targetDuration.toString())
                .appendLine("");
        return this;
    }

    @Override
    public MediaPlaylistWriter writeDurationTag(BigDecimal duration) throws IOException {
        Null.check(duration).as("duration");

        fileAppender
                .append(M3U8_MEDIA_PART_DURATION).append(duration.toString()).appendLine(",");
        return this;
    }

    @Override
    public MediaPlaylistWriter writeLocation(HttpUrl url) throws IOException {
        Null.check(url).as("url");

        fileAppender
                .appendLine(url.toString());
        return this;
    }

    @Override
    public Path writeEnd() throws IOException {
        fileAppender
                .appendLine("")
                .appendLine(M3U8_MEDIA_END_OF_FILE)
                .appendLine("");
        return destination;
    }

    @Override
    public void close() throws Exception {
        fileAppender.close();
    }

    // CONSTRUCTORS

    public FileMediaPlaylistWriter(Path destination) throws IOException {
        Null.check(destination).as("destination");

        this.destination = destination;
        this.fileAppender = FileAppender.ofFile(destination)
                .orElseThrow(() -> new IllegalArgumentException("Cannot write to: " + destination));
    }

    // PRIVATE

    private final Path destination;
    private final FileAppender fileAppender;

}
