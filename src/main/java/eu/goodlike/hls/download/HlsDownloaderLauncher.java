package eu.goodlike.hls.download;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Main class for HlsDownloader
 */
public final class HlsDownloaderLauncher {

    public static void main(String... args) {
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

    private void run(String... args) {
        Namespace namespace;
        try {
            namespace = argumentParser.parseArgs(args);
        } catch (ArgumentParserException e) {
            argumentParser.handleError(e);
            return;
        }

        List<String> urls = namespace.getList(urlKey);

        List<CompletableFuture<?>> handlers = new ArrayList<>();
        try {
            argResolver.getAllUrls(urls).stream()
                    .map(this::getHandler)
                    .forEach(handlers::add);
        } catch (Throwable t) {
            LOG.info("An error occurred: {}", t.getMessage());
        } finally {
            CompletableFuture<?>[] handlerArray = handlers.toArray(new CompletableFuture<?>[handlers.size()]);
            CompletableFuture.allOf(handlerArray)
                    .whenComplete((any, ex) -> deployTheClosener());
        }
    }

    private CompletableFuture<?> getHandler(HttpUrl url) {
        LOG.info("Downloading from url: {}", url);
        HlsBuilder builder = undefinedPlaylistBuilderFactory.createUndefinedPlaylistBuilder(url);
        hlsParser.parse(url).forEach(hlsTag -> hlsTag.extractDataInto(builder));
        return builder.build().handlePlaylistData();
    }

    private void deployTheClosener() {
        try {
            theClosener.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(HlsDownloaderLauncher.class);

}
