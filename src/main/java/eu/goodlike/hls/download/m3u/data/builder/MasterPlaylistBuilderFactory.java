package eu.goodlike.hls.download.m3u.data.builder;

import com.google.inject.assistedinject.AssistedInject;
import eu.goodlike.hls.download.m3u.data.MasterPlaylistData;
import okhttp3.HttpUrl;

/**
 * Factory for {@link MasterPlaylistBuilderFactory}; implementation provided by Guice {@link AssistedInject}
 */
public interface MasterPlaylistBuilderFactory {

    /**
     * @param source url of the source of playlist data
     * @return new {@link MediaPlaylistBuilder}
     * @throws NullPointerException if source is null
     */
    HlsBuilder<MasterPlaylistData> createMasterPlaylistBuilder(HttpUrl source);

}
