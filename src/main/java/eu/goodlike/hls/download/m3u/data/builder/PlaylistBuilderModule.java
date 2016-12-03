package eu.goodlike.hls.download.m3u.data.builder;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import eu.goodlike.hls.download.m3u.data.MasterPlaylistData;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import eu.goodlike.hls.download.m3u.data.PlaylistData;

public final class PlaylistBuilderModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(new TypeLiteral<HlsBuilder<PlaylistData>>(){}, UndefinedPlaylistBuilder.class)
                .build(UndefinedPlaylistBuilderFactory.class));

        install(new FactoryModuleBuilder()
                .implement(new TypeLiteral<HlsBuilder<MasterPlaylistData>>() {}, MasterPlaylistBuilder.class)
                .build(MasterPlaylistBuilderFactory.class));

        install(new FactoryModuleBuilder()
                .implement(new TypeLiteral<HlsBuilder<MediaPlaylistData>>(){}, MediaPlaylistBuilder.class)
                .build(MediaPlaylistBuilderFactory.class));
    }

}
