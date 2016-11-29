package eu.goodlike.hls.download.m3u;

import com.google.inject.assistedinject.AssistedInject;
import okhttp3.HttpUrl;

/**
 * Factory for {@link MediaPlaylist}; implementation provided by Guice {@link AssistedInject}
 */
public interface MediaPlaylistFactory {

    /**
     * @param name name of this playlist, optional
     * @param resolution resolution of this playlist, optional
     * @param url url of this playlist
     * @return new {@link MediaPlaylist}
     * @throws NullPointerException if url is null
     */
    MediaPlaylist createMediaPlaylist(String name, String resolution, HttpUrl url);

}
