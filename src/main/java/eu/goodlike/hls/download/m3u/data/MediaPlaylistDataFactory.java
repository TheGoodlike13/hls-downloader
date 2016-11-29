package eu.goodlike.hls.download.m3u.data;

import com.google.inject.assistedinject.AssistedInject;

import java.math.BigDecimal;
import java.util.List;

/**
 * Factory for {@link MediaPlaylistData}; implementation provided by Guice {@link AssistedInject}
 */
public interface MediaPlaylistDataFactory {

    /**
     * @param filename filename this playlist should attempt to store itself as
     * @param targetDuration target duration of this playlist
     * @param mediaParts parts of this playlist
     * @return new {@link MediaPlaylistData}
     * @throws NullPointerException if filename, targetDuration or mediaParts is or contains null
     * @throws IllegalArgumentException if mediaParts is empty
     */
    MediaPlaylistData createMediaPlaylistData(String filename, BigDecimal targetDuration, List<MediaPart> mediaParts);

}
