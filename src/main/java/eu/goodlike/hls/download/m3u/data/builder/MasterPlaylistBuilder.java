package eu.goodlike.hls.download.m3u.data.builder;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.goodlike.hls.download.m3u.*;
import eu.goodlike.hls.download.m3u.data.MasterPlaylistData;
import eu.goodlike.hls.download.m3u.data.MasterPlaylistDataFactory;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.goodlike.validate.CommonValidators.NOT_BLANK;

/**
 * Implementation of {@link HlsBuilder} for building {@link MasterPlaylistData}
 */
public final class MasterPlaylistBuilder extends AbstractHlsBuilder<MasterPlaylistData> {

    @Override
    public HlsBuilder<MasterPlaylistData> setNextPlaylistName(String nextPlaylistName) {
        trySetPlaylistName(nextPlaylistName);
        return this;
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextPlaylistResolution(String nextPlaylistResolution) {
        trySetPlaylistResolution(nextPlaylistResolution);
        return this;
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextUrl(HttpUrl url) {
        trySetPlaylistUrl(url);
        return this;
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextString(String string) {
        trySetPlaylistFilename(string);
        return this;
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextGroupId(String groupId) {
        trySetNextGroupId(groupId);
        return this;
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextAudioStreamId(String groupId) {
        trySetNextAudioStreamId(groupId);
        return this;
    }

    @Override
    public MasterPlaylistData build() {
        return tryBuild();
    }

    @Override
    public HlsBuilder<MediaPlaylistData> setTargetDuration(BigDecimal targetDuration) {
        throw new IllegalStateException("Invalid master playlist: media playlist tag found");
    }

    @Override
    public HlsBuilder<MediaPlaylistData> setNextPartDuration(BigDecimal nextPartDuration) {
        throw new IllegalStateException("Invalid master playlist: media playlist tag found");
    }

    // CONSTRUCTORS

    @Inject MasterPlaylistBuilder(@Assisted HttpUrl source,
                                  MasterPlaylistDataFactory masterPlaylistDataFactory,
                                  MediaPlaylistFactory mediaPlaylistFactory,
                                  MultiMediaPlaylistFactory multiMediaPlaylistFactory) {
        super(source);
        this.masterPlaylistDataFactory = masterPlaylistDataFactory;
        this.mediaPlaylistFactory = mediaPlaylistFactory;
        this.multiMediaPlaylistFactory = multiMediaPlaylistFactory;

        this.playlists = new ArrayList<>();

        this.groupIdToStream = new HashMap<>();
        this.streamToAudioStreamId = new HashMap<>();
    }

    // PRIVATE

    private final MasterPlaylistDataFactory masterPlaylistDataFactory;
    private final MediaPlaylistFactory mediaPlaylistFactory;
    private final MultiMediaPlaylistFactory multiMediaPlaylistFactory;

    private final List<MediaPlaylist> playlists;

    private String nextPlaylistName;
    private String nextPlaylistResolution;

    private String groupId;
    private final Map<String, MediaPlaylist> groupIdToStream;

    private String audioStreamId;
    private final Map<MediaPlaylist, String> streamToAudioStreamId;

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
                .thenThrow(() -> new IllegalStateException("Invalid master playlist: filename cannot be blank"));

        HttpUrl resolvedUrl = getSourceLocation().resolve(filename);
        if (resolvedUrl == null)
            throw new IllegalStateException("Invalid master playlist: filename cannot be resolved to an url: " + filename);

        trySetPlaylistUrl(resolvedUrl);
    }

    private void trySetPlaylistUrl(HttpUrl playlistUrl) {
        MediaPlaylist mediaPlaylist = mediaPlaylistFactory.createMediaPlaylist(nextPlaylistName, nextPlaylistResolution, playlistUrl);
        playlists.add(mediaPlaylist);

        if (groupId != null)
            groupIdToStream.put(groupId, mediaPlaylist);

        if (audioStreamId != null)
            streamToAudioStreamId.put(mediaPlaylist, audioStreamId);

        nextPlaylistName = null;
        nextPlaylistResolution = null;
        groupId = null;
        audioStreamId = null;
    }

    private void trySetNextGroupId(String playlistGroupId) {
        Null.check(playlistGroupId).as("groupId");
        NOT_BLANK.ifInvalid(playlistGroupId)
                .thenThrow(() -> new IllegalStateException("Invalid master playlist: groupId cannot be blank"));

        if (groupId != null)
            throw new IllegalStateException("Invalid master playlist: multiple groupId tags before url");

        if (groupIdToStream.containsKey(playlistGroupId))
            throw new IllegalStateException("Invalid master playlist: groupId already in use: " + groupId);

        this.groupId = playlistGroupId;
    }

    private void trySetNextAudioStreamId(String groupId) {
        Null.check(groupId).as("groupId");
        NOT_BLANK.ifInvalid(groupId)
                .thenThrow(() -> new IllegalStateException("Invalid master playlist: audio stream id cannot be blank"));

        if (audioStreamId != null)
            throw new IllegalStateException("Invalid master playlist: multiple audio stream ids before url");

        this.audioStreamId = groupId;
    }

    private MasterPlaylistData tryBuild() {
        if (nextPlaylistName != null)
            throw new IllegalStateException("Invalid master playlist: dangling playlist name");

        if (nextPlaylistResolution != null)
            throw new IllegalStateException("Invalid master playlist: dangling playlist resolution");

        if (groupId != null)
            throw new IllegalStateException("Invalid master playlist: dangling groupId");

        if (audioStreamId != null)
            throw new IllegalStateException("Invalid master playlist: dangling audio stream id");

        if (playlists.isEmpty())
            throw new IllegalStateException("Invalid master playlist: no media playlists found");

        LOG.info("Building master playlist...");
        ImmutableList.Builder<DownloadableMediaPlaylist> actualPlaylists = ImmutableList.builder();
        for (MediaPlaylist playlist : playlists) {
            String audioStreamId = streamToAudioStreamId.get(playlist);
            if (audioStreamId == null)
                actualPlaylists.add(playlist);
            else {
                MediaPlaylist audioStream = groupIdToStream.get(audioStreamId);
                if (audioStream == null)
                    throw new IllegalStateException("Invalid master playlist: no audio stream with id found: " + audioStreamId);

                MultiMediaPlaylist combinedStream = multiMediaPlaylistFactory.createMultiMediaPlaylist(playlist, audioStream);
                actualPlaylists.add(combinedStream);
            }
        }

        return masterPlaylistDataFactory.createMasterPlaylistData(actualPlaylists.build());
    }

    private static final Logger LOG = LoggerFactory.getLogger(MasterPlaylistBuilder.class);

}
