package eu.goodlike.hls.download.m3u;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import eu.goodlike.hls.download.m3u.data.builder.MediaPlaylistBuilderFactory;
import eu.goodlike.hls.download.m3u.parse.HlsParser;
import eu.goodlike.neat.Null;
import eu.goodlike.str.Str;
import okhttp3.HttpUrl;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Downloads media playlist using ffmpeg
 */
public final class MediaPlaylist implements DownloadableMediaPlaylist {

    @Override
    public CompletableFuture<?> download() {
        return getMediaPlaylistData().handlePlaylistData();
    }

    @Override
    public String toString() {
        return Str.of(getName())
                .andIf(resolution != null, " (", resolution, ")")
                .toString();
    }

    public MediaPlaylistData getMediaPlaylistData() {
        HlsBuilder<MediaPlaylistData> builder = mediaPlaylistBuilderFactory.createMediaPlaylistBuilder(url);
        hlsParser.parse(url).forEach(hlsTag -> hlsTag.extractDataInto(builder));
        return builder.build();
    }

    // CONSTRUCTORS

    @Inject
    public MediaPlaylist(@Assisted("name") @Nullable String name,
                         @Assisted("resolution") @Nullable String resolution,
                         @Assisted HttpUrl url,
                         HlsParser hlsParser,
                         MediaPlaylistBuilderFactory mediaPlaylistBuilderFactory) {
        Null.check(url).as("url");

        this.name = name;
        this.resolution = resolution;
        this.url = url;

        this.hlsParser = hlsParser;
        this.mediaPlaylistBuilderFactory = mediaPlaylistBuilderFactory;
    }

    // PRIVATE

    private final String name;
    private final String resolution;
    private final HttpUrl url;

    private final HlsParser hlsParser;
    private final MediaPlaylistBuilderFactory mediaPlaylistBuilderFactory;

    // OBJECT OVERRIDES

    private String getName() {
        return name == null ? extractName(url) : name;
    }

    private String extractName(HttpUrl url) {
        List<String> pathSegments = url.pathSegments();
        return pathSegments.isEmpty()
                ? url.toString()
                : pathSegments.get(pathSegments.size() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaPlaylist)) return false;
        MediaPlaylist that = (MediaPlaylist) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(resolution, that.resolution) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, resolution, url);
    }

}
