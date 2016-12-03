package eu.goodlike.hls.download.m3u.data.builder;

import eu.goodlike.hls.download.m3u.data.PlaylistData;
import eu.goodlike.libraries.okhttp.HttpUrls;
import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;

/**
 * Base class for {@link HlsBuilder}; only used for playlist data builders which need source location info
 */
public abstract class AbstractHlsBuilder<PlaylistType extends PlaylistData> implements HlsBuilder<PlaylistType> {

    // CONSTRUCTORS

    protected AbstractHlsBuilder(HttpUrl source) {
        Null.check(source).as("source");
        this.source = source;
    }

    // PROTECTED

    protected final HttpUrl source;

    protected final HttpUrl getSourceLocation() {
        return HttpUrls.getLocationOfLastPathPart(source);
    }

    protected final String getSourceFilename() {
        return HttpUrls.getLastPathPart(source);
    }

}
