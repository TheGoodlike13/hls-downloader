package eu.goodlike.hls.download.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import eu.goodlike.libraries.okhttp.HttpRequestMaker;
import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.io.InputStream;

/**
 * Fetches an {@link InputStream} by making a call to {@link OkHttpClient}
 */
public final class OkHttpStreamer implements HttpStreamer {

    @Override
    public InputStream getStream(HttpUrl url) {
        Null.check(url).as("url");
        return httpRequestMaker.makeRequest(url).join().body().byteStream();
    }

    // CONSTRUCTORS

    @Inject OkHttpStreamer(@Named("default") HttpRequestMaker httpRequestMaker) {
        this.httpRequestMaker = httpRequestMaker;
    }

    // PRIVATE

    private final HttpRequestMaker httpRequestMaker;

}
