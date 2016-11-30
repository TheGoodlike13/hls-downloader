package eu.goodlike.hls.download;

import ch.qos.logback.classic.Level;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import eu.goodlike.io.PropertiesUtils;

import java.util.Properties;

public final class ConfigurationsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class)
                .annotatedWith(Names.named("app-name"))
                .toInstance("hlsDL");

        bind(String.class)
                .annotatedWith(Names.named("url-key"))
                .toInstance("url");

        bind(HlsDownloaderLauncher.class)
                .in(Singleton.class);
    }

    @Provides
    @Singleton
    @Named("app-log-level")
    Level getAppLogLevel() {
        String isDebugEnabled = properties.getProperty(SHOW_DEBUG);
        return "true".equals(isDebugEnabled) ? Level.DEBUG : Level.INFO;
    }

    // CONSTRUCTORS

    public ConfigurationsModule() {
        this.properties = PropertiesUtils.fileToProperties(PROPERTIES_FILE).orElseGet(Properties::new);
    }

    // PRIVATE

    private final Properties properties;

    private static final String PROPERTIES_FILE = "hls.properties";
    private static final String SHOW_DEBUG = "show-debug";

}
