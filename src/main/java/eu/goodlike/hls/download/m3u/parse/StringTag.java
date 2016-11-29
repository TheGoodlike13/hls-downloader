package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import eu.goodlike.neat.Null;

import java.util.Objects;

/**
 * Not a real tag, just plaintext
 */
public final class StringTag implements HlsTag {

    @Override
    public void extractDataInto(HlsBuilder builder) {
        Null.check(builder).as("builder");
        builder.setNextString(filename);
    }

    // CONSTRUCTORS

    public StringTag(String filename) {
        Null.check(filename).as("filename");
        this.filename = filename;
    }

    // PRIVATE

    private final String filename;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringTag)) return false;
        StringTag that = (StringTag) o;
        return Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename);
    }

}
