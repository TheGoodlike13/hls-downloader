package eu.goodlike.hls.download.m3u.parse;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public final class HlsParsingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HlsParser.class)
                .to(StreamingHlsParser.class)
                .in(Singleton.class);
    }

}
