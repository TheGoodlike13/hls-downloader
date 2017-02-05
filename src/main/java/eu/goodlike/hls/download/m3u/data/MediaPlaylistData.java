package eu.goodlike.hls.download.m3u.data;

import com.google.common.base.MoreObjects;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.goodlike.functional.Futures;
import eu.goodlike.hls.download.ffmpeg.FfmpegProcessor;
import eu.goodlike.hls.download.m3u.FileMediaPlaylistWriter;
import eu.goodlike.hls.download.m3u.MediaPlaylistWriter;
import eu.goodlike.io.FileUtils;
import eu.goodlike.neat.Null;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Defines how media playlist should handle its own data
 */
public final class MediaPlaylistData implements PlaylistData {

    @Override
    public CompletableFuture<?> handlePlaylistData() {
        return writePlaylistToFile()
                .thenCompose(actualPlaylistName -> ffmpegProcessor.processFfmpeg(actualPlaylistName, actualPlaylistName));
    }

    public CompletableFuture<String> writePlaylistToFile() {
        String actualPlaylistName = FileUtils.findAvailableName(filename);
        Path path = Paths.get(actualPlaylistName);
        try (MediaPlaylistWriter<Path> mediaPlaylistWriter = new FileMediaPlaylistWriter(path)) {
            mediaPlaylistWriter
                    .writeStart()
                    .writeTargetDuration(targetDuration);

            for (MediaPart mediaPart : mediaParts)
                mediaPart.writeInto(mediaPlaylistWriter);

            mediaPlaylistWriter.writeEnd();
        } catch (Exception e) {
            return Futures.failedFuture(e);
        }

        return CompletableFuture.completedFuture(actualPlaylistName);
    }

    // CONSTRUCTORS

    @Inject
    public MediaPlaylistData(@Assisted String filename, @Assisted BigDecimal targetDuration, @Assisted List<MediaPart> mediaParts,
                             FfmpegProcessor ffmpegProcessor) {
        Null.check(filename, targetDuration).as("filename, targetDuration");
        Null.checkList(mediaParts).as("mediaParts");
        if (mediaParts.isEmpty())
            throw new IllegalArgumentException("Playlist must have at least one media part");

        this.filename = filename;
        this.targetDuration = targetDuration;
        this.mediaParts = mediaParts;

        this.ffmpegProcessor = ffmpegProcessor;
    }

    // PRIVATE

    private final String filename;
    private final BigDecimal targetDuration;
    private final List<MediaPart> mediaParts;

    private final FfmpegProcessor ffmpegProcessor;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("MediaPlaylistData")
                .add("targetDuration", targetDuration)
                .add("mediaParts", mediaParts)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaPlaylistData)) return false;
        MediaPlaylistData that = (MediaPlaylistData) o;
        return Objects.equals(targetDuration, that.targetDuration) &&
                Objects.equals(mediaParts, that.mediaParts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetDuration, mediaParts);
    }

}
