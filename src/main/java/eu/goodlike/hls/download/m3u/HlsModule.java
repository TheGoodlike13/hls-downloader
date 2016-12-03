package eu.goodlike.hls.download.m3u;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public final class HlsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(MediaPlaylistFactory.class));

        install(new FactoryModuleBuilder().build(MultiMediaPlaylistFactory.class));
    }

}
