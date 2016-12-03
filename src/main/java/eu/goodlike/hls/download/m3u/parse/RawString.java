package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;

import java.util.Objects;

/**
 * Not a real tag, just plaintext
 */
public final class RawString implements HlsTag {

    @Override
    public void extractDataInto(HlsBuilder<?> builder) {
        Null.check(builder).as("builder");
        HttpUrl url = HttpUrl.parse(raw);
        if (url == null)
            builder.setNextString(raw);
        else
            builder.setNextUrl(url);
    }

    // CONSTRUCTORS

    public RawString(String raw) {
        Null.check(raw).as("raw");
        this.raw = raw;
    }

    // PRIVATE

    private final String raw;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RawString)) return false;
        RawString that = (RawString) o;
        return Objects.equals(raw, that.raw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raw);
    }

}
