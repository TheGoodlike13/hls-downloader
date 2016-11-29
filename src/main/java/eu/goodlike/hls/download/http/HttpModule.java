package eu.goodlike.hls.download.http;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import eu.goodlike.libraries.okhttp.DefaultHttpRequestCaller;
import eu.goodlike.libraries.okhttp.DefaultHttpRequestMaker;
import eu.goodlike.libraries.okhttp.HttpRequestCaller;
import eu.goodlike.libraries.okhttp.HttpRequestMaker;
import okhttp3.OkHttpClient;

public final class HttpModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HttpStreamer.class)
                .to(OkHttpStreamer.class)
                .in(Singleton.class);
    }

    @Provides
    @Singleton
    OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    @Named("default")
    HttpRequestCaller getDefaultHttpRequestCaller(OkHttpClient okHttpClient) {
        return new DefaultHttpRequestCaller(okHttpClient);
    }

    @Provides
    @Singleton
    @Named("default")
    HttpRequestMaker getDefaultHttpRequestMaker(@Named("default") HttpRequestCaller defaultHttpRequestCaller) {
        return new DefaultHttpRequestMaker(defaultHttpRequestCaller);
    }

}
