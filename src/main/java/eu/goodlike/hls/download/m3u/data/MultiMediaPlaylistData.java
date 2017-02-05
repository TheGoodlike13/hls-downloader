package eu.goodlike.hls.download.m3u.data;

import com.google.common.base.MoreObjects;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.goodlike.functional.Futures;
import eu.goodlike.hls.download.ffmpeg.FfmpegProcessor;
import eu.goodlike.neat.Null;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Media playlist data which has separate video and audio streams; these streams are combined when processing
 */
public final class MultiMediaPlaylistData implements PlaylistData {

    @Override
    public CompletableFuture<?> handlePlaylistData() {
        return Futures.allOf(videoData.writePlaylistToFile(), audioData.writePlaylistToFile())
                .thenCompose(this::processPlaylist);
    }

    // CONSTRUCTORS

    @Inject
    public MultiMediaPlaylistData(@Assisted("video") MediaPlaylistData videoData,
                                  @Assisted("audio") MediaPlaylistData audioData,
                                  FfmpegProcessor ffmpegProcessor) {
        Null.check(videoData, audioData).as("videoData, audioData");

        this.videoData = videoData;
        this.audioData = audioData;

        this.ffmpegProcessor = ffmpegProcessor;
    }

    // PRIVATE

    private final MediaPlaylistData videoData;
    private final MediaPlaylistData audioData;

    private final FfmpegProcessor ffmpegProcessor;

    private CompletableFuture<?> processPlaylist(List<String> filenames) {
        String videoFilename = filenames.get(0);
        String audioFilename = filenames.get(1);
        return ffmpegProcessor.processFfmpeg(videoFilename, videoFilename, audioFilename);
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("MultiMediaPlaylistData")
                .add("videoData", videoData)
                .add("audioData", audioData)
                .toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiMediaPlaylistData)) return false;
        MultiMediaPlaylistData that = (MultiMediaPlaylistData) o;
        return Objects.equals(videoData, that.videoData) &&
                Objects.equals(audioData, that.audioData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoData, audioData);
    }

}
