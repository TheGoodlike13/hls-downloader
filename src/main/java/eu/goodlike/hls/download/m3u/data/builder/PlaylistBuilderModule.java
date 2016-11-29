package eu.goodlike.hls.download.m3u.data.builder;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public final class PlaylistBuilderModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(UndefinedPlaylistBuilderFactory.class));

        install(new FactoryModuleBuilder().build(MasterPlaylistBuilderFactory.class));

        install(new FactoryModuleBuilder().build(MediaPlaylistBuilderFactory.class));
    }

}
