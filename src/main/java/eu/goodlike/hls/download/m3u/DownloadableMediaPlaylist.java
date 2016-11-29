package eu.goodlike.hls.download.m3u;

import java.util.concurrent.CompletableFuture;

/**
 * Defines how to download a media playlist
 */
public interface DownloadableMediaPlaylist {

    /**
     * Downloads this playlist
     *
     * @return future which completes when download finishes
     */
    CompletableFuture<?> download();

}
