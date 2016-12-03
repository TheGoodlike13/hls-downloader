package eu.goodlike.hls.download.m3u.data;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import eu.goodlike.io.InputReader;
import eu.goodlike.io.UserInputReader;

public final class PlaylistDataModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(MasterPlaylistDataFactory.class));

        install(new FactoryModuleBuilder().build(MediaPlaylistDataFactory.class));

        install(new FactoryModuleBuilder().build(MultiMediaPlaylistDataFactory.class));
    }

    @Provides
    @Singleton
    InputReader getInputReader() {
        return new UserInputReader();
    }

}
