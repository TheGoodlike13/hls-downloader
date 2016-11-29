package eu.goodlike.hls.download;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import eu.goodlike.libraries.slf4j.Log;

public final class ConfigurationsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class)
                .annotatedWith(Names.named("app-name"))
                .toInstance("hlsDL");

        bind(String.class)
                .annotatedWith(Names.named("url-key"))
                .toInstance("url");

        bind(Log.class)
                .annotatedWith(Names.named("ffmpeg-log-level"))
                .toInstance(Log.INFO);

        bind(HlsDownloaderLauncher.class)
                .in(Singleton.class);
    }

}
