package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import eu.goodlike.neat.Null;
import eu.goodlike.str.Str;

import java.util.Objects;

import static eu.goodlike.hls.download.m3u.M3U8Defaults.M3U8_MASTER_MEDIA_NAME_ATTRIBUTE;
import static eu.goodlike.hls.download.m3u.M3U8Defaults.M3U8_MASTER_MEDIA_TAG;

/**
 * Hls tag: EXT-X-MEDIA
 */
public final class MediaTag implements HlsTag {

    @Override
    public void extractDataInto(HlsBuilder<?> builder) {
        Null.check(builder).as("builder");
        if (groupId != null)
            builder.setNextGroupId(groupId);

        if (name != null)
            builder.setNextPlaylistName(name);

        if (rawString != null)
            rawString.extractDataInto(builder);
    }

    // CONSTRUCTORS

    public MediaTag(String groupId, String name, String uri) {
        this.groupId = groupId;
        this.name = name;
        this.rawString = uri == null ? null : new RawString(uri);
    }

    // PRIVATE

    private final String groupId;
    private final String name;
    private final RawString rawString;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return Str.format("{}{}\"{}\"",
                M3U8_MASTER_MEDIA_TAG,
                M3U8_MASTER_MEDIA_NAME_ATTRIBUTE,
                name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaTag)) return false;
        MediaTag mediaTag = (MediaTag) o;
        return Objects.equals(name, mediaTag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
