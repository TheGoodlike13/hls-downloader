package eu.goodlike.hls.download.m3u.data.builder;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.goodlike.hls.download.m3u.DownloadableMediaPlaylist;
import eu.goodlike.hls.download.m3u.MediaPlaylistFactory;
import eu.goodlike.hls.download.m3u.data.MasterPlaylistData;
import eu.goodlike.hls.download.m3u.data.MasterPlaylistDataFactory;
import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static eu.goodlike.validate.CommonValidators.NOT_BLANK;

/**
 * Implementation of {@link HlsBuilder} for building {@link MasterPlaylistData}
 */
public final class MasterPlaylistBuilder extends AbstractHlsBuilder {

    @Override
    public HlsBuilder setNextPlaylistName(String nextPlaylistName) {
        trySetPlaylistName(nextPlaylistName);
        return this;
    }

    @Override
    public HlsBuilder setNextPlaylistResolution(String nextPlaylistResolution) {
        trySetPlaylistResolution(nextPlaylistResolution);
        return this;
    }

    @Override
    public HlsBuilder setNextUrl(HttpUrl url) {
        trySetPlaylistUrl(url);
        return this;
    }

    @Override
    public HlsBuilder setNextString(String string) {
        trySetPlaylistFilename(string);
        return this;
    }

    @Override
    public MasterPlaylistData build() {
        return tryBuild();
    }

    @Override
    public HlsBuilder setTargetDuration(BigDecimal targetDuration) {
        throw new IllegalStateException("Invalid master playlist: media playlist tag found");
    }

    @Override
    public HlsBuilder setNextPartDuration(BigDecimal nextPartDuration) {
        throw new IllegalStateException("Invalid master playlist: media playlist tag found");
    }

    // CONSTRUCTORS

    @Inject MasterPlaylistBuilder(@Assisted HttpUrl source,
                                  MasterPlaylistDataFactory masterPlaylistDataFactory,
                                  MediaPlaylistFactory mediaPlaylistFactory) {
        super(source);
        this.masterPlaylistDataFactory = masterPlaylistDataFactory;
        this.mediaPlaylistFactory = mediaPlaylistFactory;

        this.playlists = new ArrayList<>();
    }

    // PRIVATE

    private final MasterPlaylistDataFactory masterPlaylistDataFactory;
    private final MediaPlaylistFactory mediaPlaylistFactory;

    private final List<DownloadableMediaPlaylist> playlists;

    private String nextPlaylistName;
    private String nextPlaylistResolution;

    private void trySetPlaylistName(String playlistName) {
        Null.check(playlistName).as("playlistName");
        NOT_BLANK.ifInvalid(playlistName)
                .thenThrow(() -> new IllegalStateException("Invalid master playlist: blank playlist name"));

        if (nextPlaylistName != null)
            throw new IllegalStateException("Invalid master playlist: multiple name tags before playlist url");

        nextPlaylistName = playlistName;
    }

    private void trySetPlaylistResolution(String playlistResolution) {
        Null.check(playlistResolution).as("playlistResolution");
        NOT_BLANK.ifInvalid(playlistResolution)
                .thenThrow(() -> new IllegalStateException("Invalid master playlist: blank playlist resolution"));

        if (nextPlaylistResolution != null)
            throw new IllegalStateException("Invalid master playlist: multiple resolution tags before playlist url");

        nextPlaylistResolution = playlistResolution;
    }

    private void trySetPlaylistFilename(String filename) {
        Null.check(filename).as("filename");
        NOT_BLANK.ifInvalid(filename)
                .thenThrow(() -> new AssertionError("Programming error: attempted to set blank part name"));

        HttpUrl resolvedUrl = getSourceLocation().resolve(filename);
        if (resolvedUrl == null)
            throw new IllegalStateException("Invalid master playlist: filename cannot be resolved to an url: " + filename);

        trySetPlaylistUrl(resolvedUrl);
    }

    private void trySetPlaylistUrl(HttpUrl playlistUrl) {
        DownloadableMediaPlaylist mediaPlaylist = mediaPlaylistFactory.createMediaPlaylist(nextPlaylistName, nextPlaylistResolution, playlistUrl);
        playlists.add(mediaPlaylist);

        nextPlaylistName = null;
        nextPlaylistResolution = null;
    }

    private MasterPlaylistData tryBuild() {
        if (nextPlaylistName != null)
            throw new IllegalStateException("Invalid master playlist: dangling playlist name");

        if (nextPlaylistResolution != null)
            throw new IllegalStateException("Invalid master playlist: dangling playlist resolution");

        if (playlists.isEmpty())
            throw new IllegalStateException("Invalid master playlist: no media playlists found");

        LOG.info("Building master playlist...");
        return masterPlaylistDataFactory.createMasterPlaylistData(ImmutableList.copyOf(playlists));
    }

    private static final Logger LOG = LoggerFactory.getLogger(MasterPlaylistBuilder.class);

}
