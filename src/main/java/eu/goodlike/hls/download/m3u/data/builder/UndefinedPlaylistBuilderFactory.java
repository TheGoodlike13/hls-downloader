package eu.goodlike.hls.download.m3u.data.builder;

import com.google.inject.assistedinject.AssistedInject;
import eu.goodlike.hls.download.m3u.data.PlaylistData;
import okhttp3.HttpUrl;

/**
 * Factory for {@link UndefinedPlaylistBuilder}; implementation provided by Guice {@link AssistedInject}
 */
public interface UndefinedPlaylistBuilderFactory {

    /**
     * @param source url of the source of playlist data
     * @return new {@link UndefinedPlaylistBuilder}
     * @throws NullPointerException if source is null
     */
    HlsBuilder<PlaylistData> createUndefinedPlaylistBuilder(HttpUrl source);

}
