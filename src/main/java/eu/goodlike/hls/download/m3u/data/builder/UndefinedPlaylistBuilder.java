package eu.goodlike.hls.download.m3u.data.builder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.goodlike.hls.download.m3u.data.PlaylistData;
import okhttp3.HttpUrl;

import java.math.BigDecimal;

/**
 * Implementation of {@link HlsBuilder} for building data of yet unknown type
 */
public final class UndefinedPlaylistBuilder extends AbstractHlsBuilder {

    @Override
    public HlsBuilder setNextPlaylistName(String nextPlaylistName) {
        return masterPlaylistBuilderFactory.createMasterPlaylistBuilder(source).setNextPlaylistName(nextPlaylistName);
    }

    @Override
    public HlsBuilder setNextPlaylistResolution(String nextPlaylistResolution) {
        return masterPlaylistBuilderFactory.createMasterPlaylistBuilder(source).setNextPlaylistResolution(nextPlaylistResolution);
    }

    @Override
    public HlsBuilder setNextUrl(HttpUrl url) {
        return masterPlaylistBuilderFactory.createMasterPlaylistBuilder(source).setNextUrl(url);
    }

    @Override
    public HlsBuilder setTargetDuration(BigDecimal targetDuration) {
        return mediaPlaylistBuilderFactory.createMediaPlaylistBuilder(source).setTargetDuration(targetDuration);
    }

    @Override
    public HlsBuilder setNextPartDuration(BigDecimal nextPartDuration) {
        throw new IllegalStateException("Invalid media playlist: no target duration found");
    }

    @Override
    public HlsBuilder setNextString(String string) {
        throw new IllegalStateException("Invalid media playlist: no target duration found, filename before duration");
    }

    @Override
    public PlaylistData build() {
        throw new IllegalStateException("Cannot build playlist without any tag information");
    }

    @Inject UndefinedPlaylistBuilder(@Assisted HttpUrl source,
                                     MasterPlaylistBuilderFactory masterPlaylistBuilderFactory,
                                     MediaPlaylistBuilderFactory mediaPlaylistBuilderFactory) {
        super(source);
        this.masterPlaylistBuilderFactory = masterPlaylistBuilderFactory;
        this.mediaPlaylistBuilderFactory = mediaPlaylistBuilderFactory;
    }

    // PRIVATE

    private final MasterPlaylistBuilderFactory masterPlaylistBuilderFactory;
    private final MediaPlaylistBuilderFactory mediaPlaylistBuilderFactory;

}
