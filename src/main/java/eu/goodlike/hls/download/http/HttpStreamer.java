package eu.goodlike.hls.download.http;

import okhttp3.HttpUrl;

import java.io.InputStream;

public interface HttpStreamer {

    InputStream getStream(HttpUrl url);

}
