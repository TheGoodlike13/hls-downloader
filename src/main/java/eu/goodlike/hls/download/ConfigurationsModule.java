package eu.goodlike.hls.download;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import eu.goodlike.libraries.slf4j.Log;

public final class ConfigurationsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Log.class)
                .annotatedWith(Names.named("ffmpeg-log-level"))
                .toInstance(Log.INFO);
    }

}
