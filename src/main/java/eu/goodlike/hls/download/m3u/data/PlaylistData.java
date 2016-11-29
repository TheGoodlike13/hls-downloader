package eu.goodlike.hls.download.m3u.data;

import java.util.concurrent.CompletableFuture;

/**
 * Defines a playlist which can handle its own data
 */
public interface PlaylistData {

    /**
     * <pre>
     * Handles the data of this playlist
     *
     * In case of media playlist, it should use ffmpeg to download it
     *
     * In the case of a master playlist, this method should prompt the user to select a media playlist to download,
     * then handle the media playlist data
     * </pre>
     * @return future which completes when handling is over
     */
    CompletableFuture<?> handlePlaylistData();

}
