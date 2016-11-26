package eu.goodlike.hls.download.http;

import okhttp3.HttpUrl;

import java.io.InputStream;

/**
 * Defines how to get an {@link InputStream} containing the contents of any given {@link HttpUrl}
 */
public interface HttpStreamer {

    /**
     * Gets an input stream from any given {@link HttpUrl}
     *
     * @param url url to get the contents of
     * @return {@link InputStream} containing contents of given url
     * @throws NullPointerException if url is null
     */
    InputStream getStream(HttpUrl url);

}
