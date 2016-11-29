package eu.goodlike.hls.download.args;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public final class ArgsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ArgResolver.class)
                .to(FileAwareArgResolver.class)
                .in(Singleton.class);
    }

}
