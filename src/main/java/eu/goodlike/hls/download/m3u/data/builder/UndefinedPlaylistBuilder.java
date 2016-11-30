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
        return getActualBuilderInitializeMasterIfNeeded().setNextPlaylistName(nextPlaylistName);
    }

    @Override
    public HlsBuilder setNextPlaylistResolution(String nextPlaylistResolution) {
        return getActualBuilderInitializeMasterIfNeeded().setNextPlaylistResolution(nextPlaylistResolution);
    }

    @Override
    public HlsBuilder setNextUrl(HttpUrl url) {
        return getActualBuilderInitializeMasterIfNeeded().setNextUrl(url);
    }

    @Override
    public HlsBuilder setNextString(String string) {
        return getActualBuilderInitializeMasterIfNeeded().setNextString(string);
    }

    @Override
    public HlsBuilder setTargetDuration(BigDecimal targetDuration) {
        return getActualBuilderInitializeMediaIfNeeded().setTargetDuration(targetDuration);
    }

    @Override
    public HlsBuilder setNextPartDuration(BigDecimal nextPartDuration) {
        return getActualBuilderInitializeMediaIfNeeded().setNextPartDuration(nextPartDuration);
    }

    @Override
    public PlaylistData build() {
        return getActualBuilder().build();
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

    private HlsBuilder actualBuilder;

    private HlsBuilder getActualBuilder() {
        if (actualBuilder == null)
            throw new IllegalStateException("Invalid playlist: no tags found");

        return actualBuilder;
    }

    private HlsBuilder getActualBuilderInitializeMasterIfNeeded() {
        if (actualBuilder == null)
            actualBuilder = masterPlaylistBuilderFactory.createMasterPlaylistBuilder(source);

        return actualBuilder;
    }

    private HlsBuilder getActualBuilderInitializeMediaIfNeeded() {
        if (actualBuilder == null)
            actualBuilder = mediaPlaylistBuilderFactory.createMediaPlaylistBuilder(source);

        return actualBuilder;
    }

}
