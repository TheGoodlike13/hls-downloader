package eu.goodlike.hls.download.m3u.data;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Factory for {@link MultiMediaPlaylistData}; implementation provided by Guice {@link AssistedInject}
 */
@SuppressWarnings("BindingAnnotationWithoutInject")
public interface MultiMediaPlaylistDataFactory {

    /**
     * @param videoData playlist data containing video
     * @param audioData playlist data containing audio
     * @return new {@link MultiMediaPlaylistData}
     * @throws NullPointerException if mediaPlaylistDataList is or contains null
     * @throws IllegalArgumentException if mediaPlaylistDataList is empty
     */
    MultiMediaPlaylistData createMultiMediaPlaylistData(@Assisted("video") MediaPlaylistData videoData,
                                                        @Assisted("audio") MediaPlaylistData audioData);

}
