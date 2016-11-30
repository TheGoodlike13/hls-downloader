package eu.goodlike.hls.download;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import eu.goodlike.hls.download.args.ArgResolver;
import eu.goodlike.hls.download.args.ArgsModule;
import eu.goodlike.hls.download.ffmpeg.FfmpegModule;
import eu.goodlike.hls.download.http.HttpModule;
import eu.goodlike.hls.download.m3u.HlsModule;
import eu.goodlike.hls.download.m3u.data.PlaylistDataModule;
import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import eu.goodlike.hls.download.m3u.data.builder.PlaylistBuilderModule;
import eu.goodlike.hls.download.m3u.data.builder.UndefinedPlaylistBuilderFactory;
import eu.goodlike.hls.download.m3u.parse.HlsParser;
import eu.goodlike.hls.download.m3u.parse.HlsParsingModule;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import okhttp3.HttpUrl;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Main class for HlsDownloader
 */
public final class HlsDownloaderLauncher {

    public static void main(String... args) throws Exception {
        Injector injector = Guice.createInjector(
                new ArgsModule(),
                new ConfigurationsModule(),
                new PlaylistBuilderModule(),
                new PlaylistDataModule(),
                new HlsModule(),
                new HlsParsingModule(),
                new HttpModule(),
                new FfmpegModule()
        );

        Level logLevel = injector.getInstance(Key.get(Level.class, Names.named("app-log-level")));
        Logger logger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(logLevel);

        HlsDownloaderLauncher launcher = injector.getInstance(HlsDownloaderLauncher.class);
        launcher.run(args);
    }

    // CONSTRUCTORS

    @Inject HlsDownloaderLauncher(ArgumentParser argumentParser,
                                  @Named("url-key") String urlKey,
                                  ArgResolver argResolver,
                                  UndefinedPlaylistBuilderFactory undefinedPlaylistBuilderFactory,
                                  HlsParser hlsParser,
                                  TheClosener theClosener) {
        this.argumentParser = argumentParser;
        this.urlKey = urlKey;
        this.argResolver = argResolver;
        this.undefinedPlaylistBuilderFactory = undefinedPlaylistBuilderFactory;
        this.hlsParser = hlsParser;
        this.theClosener = theClosener;
    }

    // PRIVATE

    private final ArgumentParser argumentParser;
    private final String urlKey;
    private final ArgResolver argResolver;
    private final UndefinedPlaylistBuilderFactory undefinedPlaylistBuilderFactory;
    private final HlsParser hlsParser;
    private final TheClosener theClosener;

    private void run(String... args) throws Exception {
        Namespace namespace;
        try {
            namespace = argumentParser.parseArgs(args);
        } catch (ArgumentParserException e) {
            argumentParser.handleError(e);
            return;
        }

        List<String> urls = namespace.getList(urlKey);

        try {
            Set<HttpUrl> allUrls = argResolver.getAllUrls(urls);
            if (allUrls.isEmpty())
                LOG.info("No playlists found! Check your args: " + Arrays.toString(args));

            for (HttpUrl url : allUrls)
                getHandler(url).join();
        } catch (Throwable t) {
            LOG.info("Error {}: {}", t.getClass().getSimpleName(), t.getMessage());
            LOG.debug("Detailed error output", t);
        } finally {
            theClosener.close();
        }
    }

    private CompletableFuture<?> getHandler(HttpUrl url) {
        LOG.info("Downloading from url: {}", url);
        HlsBuilder builder = undefinedPlaylistBuilderFactory.createUndefinedPlaylistBuilder(url);
        hlsParser.parse(url).forEach(hlsTag -> hlsTag.extractDataInto(builder));
        return builder.build().handlePlaylistData();
    }

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(HlsDownloaderLauncher.class);

}
