package eu.goodlike.hls.download.m3u.data;

import com.google.inject.assistedinject.AssistedInject;
import eu.goodlike.hls.download.m3u.DownloadableMediaPlaylist;

import java.util.List;

/**
 * Factory for {@link MasterPlaylistData}; implementation provided by Guice {@link AssistedInject}
 */
public interface MasterPlaylistDataFactory {

    /**
     * @param playlists media playlists inside this master playlist
     * @return new {@link MasterPlaylistData}
     * @throws NullPointerException if playlists is null
     * @throws IllegalArgumentException if mediaParts is empty
     */
    MasterPlaylistData createMasterPlaylistData(List<DownloadableMediaPlaylist> playlists);

}
