package eu.goodlike.hls.download.m3u.data.builder;

import com.google.inject.assistedinject.AssistedInject;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import okhttp3.HttpUrl;

/**
 * Factory for {@link MediaPlaylistBuilder}; implementation provided by Guice {@link AssistedInject}
 */
public interface MediaPlaylistBuilderFactory {

    /**
     * @param source url of the source of playlist data
     * @return new {@link MediaPlaylistBuilder}
     * @throws NullPointerException if source is null
     */
    HlsBuilder<MediaPlaylistData> createMediaPlaylistBuilder(HttpUrl source);

}
