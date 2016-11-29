package eu.goodlike.hls.download.m3u.data;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public final class PlaylistDataModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(MasterPlaylistDataFactory.class));

        install(new FactoryModuleBuilder().build(MediaPlaylistDataFactory.class));
    }

}
