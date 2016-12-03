package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;

/**
 * Defines how a tag is parsed into a {@link HlsBuilder}
 */
public interface HlsTag {

    /**
     * <pre>
     * Give the data of this tag to a {@link HlsBuilder}
     *
     * Some tags might not have any data to give, whereas others might give multiple pieces of data
     * </pre>
     * @param builder builder to given data to
     * @throws NullPointerException if builder is null
     */
    void extractDataInto(HlsBuilder<?> builder);

}
