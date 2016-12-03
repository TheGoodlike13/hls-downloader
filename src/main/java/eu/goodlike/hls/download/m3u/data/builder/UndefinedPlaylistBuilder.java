package eu.goodlike.hls.download.m3u.data.builder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.goodlike.hls.download.m3u.data.MasterPlaylistData;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import eu.goodlike.hls.download.m3u.data.PlaylistData;
import okhttp3.HttpUrl;

import java.math.BigDecimal;

/**
 * Implementation of {@link HlsBuilder} for building data of yet unknown type
 */
public final class UndefinedPlaylistBuilder extends AbstractHlsBuilder<PlaylistData> {

    @Override
    public HlsBuilder<MasterPlaylistData> setNextPlaylistName(String nextPlaylistName) {
        return getActualBuilderInitializeMasterIfNeeded().setNextPlaylistName(nextPlaylistName);
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextPlaylistResolution(String nextPlaylistResolution) {
        return getActualBuilderInitializeMasterIfNeeded().setNextPlaylistResolution(nextPlaylistResolution);
    }

    @Override
    public HlsBuilder<PlaylistData> setNextUrl(HttpUrl url) {
        return getActualBuilderInitializeMasterIfNeeded().setNextUrl(url);
    }

    @Override
    public HlsBuilder<PlaylistData> setNextString(String string) {
        return getActualBuilderInitializeMasterIfNeeded().setNextString(string);
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextGroupId(String groupId) {
        return getActualBuilderInitializeMasterIfNeeded().setNextGroupId(groupId);
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextAudioStreamId(String groupId) {
        return getActualBuilderInitializeMasterIfNeeded().setNextAudioStreamId(groupId);
    }

    @Override
    public HlsBuilder<MediaPlaylistData> setTargetDuration(BigDecimal targetDuration) {
        return getActualBuilderInitializeMediaIfNeeded().setTargetDuration(targetDuration);
    }

    @Override
    public HlsBuilder<MediaPlaylistData> setNextPartDuration(BigDecimal nextPartDuration) {
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

    private HlsBuilder<? extends PlaylistData> actualBuilder;

    @SuppressWarnings("unchecked")
    private HlsBuilder<PlaylistData> getActualBuilder() {
        if (actualBuilder == null)
            throw new IllegalStateException("Invalid playlist: no tags found");

        return (HlsBuilder<PlaylistData>) actualBuilder;
    }

    @SuppressWarnings("unchecked")
    private HlsBuilder<PlaylistData> getActualBuilderInitializeMasterIfNeeded() {
        if (actualBuilder == null)
            actualBuilder = masterPlaylistBuilderFactory.createMasterPlaylistBuilder(source);

        return (HlsBuilder<PlaylistData>) actualBuilder;
    }

    @SuppressWarnings("unchecked")
    private HlsBuilder<PlaylistData> getActualBuilderInitializeMediaIfNeeded() {
        if (actualBuilder == null)
            actualBuilder = mediaPlaylistBuilderFactory.createMediaPlaylistBuilder(source);

        return (HlsBuilder<PlaylistData>) actualBuilder;
    }

}
