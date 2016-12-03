package eu.goodlike.hls.download.m3u.data.builder;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.goodlike.hls.download.m3u.data.MasterPlaylistData;
import eu.goodlike.hls.download.m3u.data.MediaPart;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistDataFactory;
import eu.goodlike.neat.Null;
import eu.goodlike.validate.impl.BigDecimalValidator;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static eu.goodlike.validate.CommonValidators.NOT_BLANK;
import static eu.goodlike.validate.Validate.bigDecimal;

/**
 * Implementation of {@link HlsBuilder} for building {@link MediaPlaylistData}
 */
public final class MediaPlaylistBuilder extends AbstractHlsBuilder<MediaPlaylistData> {

    @Override
    public HlsBuilder<MediaPlaylistData> setTargetDuration(BigDecimal targetDuration) {
        trySetTargetDuration(targetDuration);
        return this;
    }

    @Override
    public HlsBuilder<MediaPlaylistData> setNextPartDuration(BigDecimal nextPartDuration) {
        trySetNextPartDuration(nextPartDuration);
        return this;
    }

    @Override
    public HlsBuilder<MediaPlaylistData> setNextUrl(HttpUrl url) {
        trySetNextPartUrl(url);
        return this;
    }

    @Override
    public HlsBuilder<MediaPlaylistData> setNextString(String string) {
        trySetNextPartFilename(string);
        return this;
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextGroupId(String groupId) {
        throw new IllegalStateException("Invalid media playlist: master playlist tag found");
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextAudioStreamId(String groupId) {
        throw new IllegalStateException("Invalid media playlist: master playlist tag found");
    }

    @Override
    public MediaPlaylistData build() {
        return tryBuild();
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextPlaylistName(String nextPlaylistName) {
        throw new IllegalStateException("Invalid media playlist: master playlist tag found");
    }

    @Override
    public HlsBuilder<MasterPlaylistData> setNextPlaylistResolution(String nextPlaylistResolution) {
        throw new IllegalStateException("Invalid media playlist: master playlist tag found");
    }

    // CONSTRUCTORS

    @Inject
    public MediaPlaylistBuilder(@Assisted HttpUrl source,
                                MediaPlaylistDataFactory mediaPlaylistDataFactory) {
        super(source);
        this.mediaPlaylistDataFactory = mediaPlaylistDataFactory;

        this.mediaParts = new ArrayList<>();
    }

    // PRIVATE

    private final MediaPlaylistDataFactory mediaPlaylistDataFactory;

    private final List<MediaPart> mediaParts;

    private BigDecimal playlistTargetDuration;
    private BigDecimal nextPartDuration;

    private void trySetTargetDuration(BigDecimal targetDuration) {
        Null.check(targetDuration).as("targetDuration");
        IS_POSITIVE.ifInvalid(targetDuration)
                .thenThrow(() -> new IllegalStateException("Invalid media playlist: negative target duration: " + targetDuration));

        if (playlistTargetDuration != null)
            throw new IllegalStateException("Invalid media playlist: duplicate target duration tag");

        playlistTargetDuration = targetDuration;
    }

    private void trySetNextPartDuration(BigDecimal partDuration) {
        Null.check(partDuration).as("partDuration");
        IS_POSITIVE.ifInvalid(partDuration)
                .thenThrow(() -> new IllegalStateException("Invalid media playlist: negative part duration: " + partDuration));

        if (nextPartDuration != null)
            throw new IllegalStateException("Invalid media playlist: multiple duration tags before part url");

        nextPartDuration = partDuration;
    }

    private void trySetNextPartFilename(String filename) {
        Null.check(filename).as("filename");
        NOT_BLANK.ifInvalid(filename)
                .thenThrow(() -> new IllegalStateException("Invalid media playlist: filename cannot be blank"));

        HttpUrl resolvedUrl = getSourceLocation().resolve(filename);
        if (resolvedUrl == null)
            throw new IllegalStateException("Invalid media playlist: filename cannot be resolved to an url: " + filename);

        trySetNextPartUrl(resolvedUrl);
    }

    private void trySetNextPartUrl(HttpUrl url) {
        if (nextPartDuration == null)
            throw new IllegalStateException("Invalid media playlist: no duration specified for a part");

        MediaPart mediaPart = new MediaPart(nextPartDuration, url);
        mediaParts.add(mediaPart);

        nextPartDuration = null;
    }

    private MediaPlaylistData tryBuild() {
        if (playlistTargetDuration == null)
            throw new IllegalStateException("Invalid media playlist: no target duration tag");

        if (nextPartDuration != null)
            throw new IllegalStateException("Invalid media playlist: dangling part duration tag");

        if (mediaParts.isEmpty())
            throw new IllegalStateException("Invalid media playlist: no playlist parts");

        LOG.info("Building media playlist...");
        return mediaPlaylistDataFactory.createMediaPlaylistData(
                getSourceFilename(), playlistTargetDuration, ImmutableList.copyOf(mediaParts));
    }

    private static final BigDecimalValidator IS_POSITIVE = bigDecimal().isPositive();

    private static final Logger LOG = LoggerFactory.getLogger(MediaPlaylistBuilder.class);

}
