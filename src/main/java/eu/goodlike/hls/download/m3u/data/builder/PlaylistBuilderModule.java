package eu.goodlike.hls.download.m3u.data.builder;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public final class PlaylistBuilderModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(HlsBuilder.class, UndefinedPlaylistBuilder.class)
                .build(UndefinedPlaylistBuilderFactory.class));

        install(new FactoryModuleBuilder()
                .implement(HlsBuilder.class, MasterPlaylistBuilder.class)
                .build(MasterPlaylistBuilderFactory.class));

        install(new FactoryModuleBuilder()
                .implement(HlsBuilder.class, MediaPlaylistBuilder.class)
                .build(MediaPlaylistBuilderFactory.class));
    }

}
