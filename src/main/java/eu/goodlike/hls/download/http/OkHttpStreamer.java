package eu.goodlike.hls.download.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import eu.goodlike.libraries.okhttp.HttpRequestMaker;
import okhttp3.HttpUrl;

import java.io.InputStream;

public final class OkHttpStreamer implements HttpStreamer {

    @Override
    public InputStream getStream(HttpUrl url) {
        return httpRequestMaker.makeRequest(url).join().body().byteStream();
    }

    // CONSTRUCTORS

    @Inject OkHttpStreamer(@Named("default") HttpRequestMaker httpRequestMaker) {
        this.httpRequestMaker = httpRequestMaker;
    }

    // PRIVATE

    private final HttpRequestMaker httpRequestMaker;

}
