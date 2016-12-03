package eu.goodlike.hls.download.m3u;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import eu.goodlike.hls.download.m3u.data.MultiMediaPlaylistDataFactory;
import eu.goodlike.neat.Null;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Media playlist which has separate video and audio streams; these streams are combined when downloading
 */
public final class MultiMediaPlaylist implements DownloadableMediaPlaylist {

    @Override
    public CompletableFuture<?> download() {
        MediaPlaylistData videoData = videoStream.getMediaPlaylistData();
        MediaPlaylistData audioData = audioStream.getMediaPlaylistData();
        return multiMediaPlaylistDataFactory.createMultiMediaPlaylistData(videoData, audioData).handlePlaylistData();
    }

    @Override
    public String toString() {
        return videoStream.toString();
    }

    // CONSTRUCTORS

    @Inject
    public MultiMediaPlaylist(@Assisted("video") MediaPlaylist videoStream,
                              @Assisted("audio") MediaPlaylist audioStream,
                              MultiMediaPlaylistDataFactory multiMediaPlaylistDataFactory) {
        Null.check(videoStream, audioStream).as("videoStream, audioStream");

        this.videoStream = videoStream;
        this.audioStream = audioStream;

        this.multiMediaPlaylistDataFactory = multiMediaPlaylistDataFactory;
    }

    // PRIVATE

    private final MediaPlaylist videoStream;
    private final MediaPlaylist audioStream;

    private final MultiMediaPlaylistDataFactory multiMediaPlaylistDataFactory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiMediaPlaylist)) return false;
        MultiMediaPlaylist that = (MultiMediaPlaylist) o;
        return Objects.equals(videoStream, that.videoStream) &&
                Objects.equals(audioStream, that.audioStream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoStream, audioStream);
    }

}
