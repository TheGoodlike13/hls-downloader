package eu.goodlike.hls.download.m3u;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Factory for {@link MultiMediaPlaylist}; implementation provided by Guice {@link AssistedInject}
 */
@SuppressWarnings("BindingAnnotationWithoutInject")
public interface MultiMediaPlaylistFactory {

    /**
     * @param videoStream playlist containing video
     * @param audioStream playlist containing audio
     * @return new {@link MultiMediaPlaylist}
     * @throws NullPointerException if mediaPlaylistDataList is or contains null
     * @throws IllegalArgumentException if mediaPlaylistDataList is empty
     */
    MultiMediaPlaylist createMultiMediaPlaylist(@Assisted("video") MediaPlaylist videoStream,
                                                @Assisted("audio") MediaPlaylist audioStream);

}
