package eu.goodlike.hls.download.m3u.parse;

import okhttp3.HttpUrl;

import java.util.List;

/**
 * Defines how to parse an hls file from an url
 */
public interface HlsParser {

    /**
     * Parses an hls file from given url into tags
     *
     * @param url url of the file
     * @return list of tags
     * @throws NullPointerException if url is null
     */
    List<HlsTag> parse(HttpUrl url);

}
