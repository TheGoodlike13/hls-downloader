package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;

import java.util.Objects;

/**
 * Not a real tag, just a plaintext url
 */
public final class UrlTag implements HlsTag {

    @Override
    public void extractDataInto(HlsBuilder builder) {
        Null.check(builder).as("builder");
        builder.setNextUrl(url);
    }

    // CONSTRUCTORS

    public UrlTag(HttpUrl url) {
        Null.check(url).as("url");
        this.url = url;
    }

    // PRIVATE

    private final HttpUrl url;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return url.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlTag)) return false;
        UrlTag that = (UrlTag) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

}
