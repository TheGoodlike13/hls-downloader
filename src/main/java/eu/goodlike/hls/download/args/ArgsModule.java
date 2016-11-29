package eu.goodlike.hls.download.args;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;

public final class ArgsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ArgResolver.class)
                .to(FileAwareArgResolver.class)
                .in(Singleton.class);
    }

    @Provides
    @Singleton
    ArgumentParser getArgumentParser(@Named("app-name") String appName,
                                     @Named("url-key") String urlKey) {
        ArgumentParser argumentParser = ArgumentParsers.newArgumentParser(appName)
                .defaultHelp(true)
                .description("Downloads HLS/M3U/M3U8 streams and combines them using ffmpeg.")
                .epilog("For additional questions and help, refer to https://github.com/TheGoodlike13/hls-downloader");

        argumentParser.addArgument("url").nargs("+")
                .dest(urlKey)
                .help(URL_EXPLANATION);

        return argumentParser;
    }

    private static final String URL_EXPLANATION =
            "playlist url/local file" + System.lineSeparator() +
                    "The url can refer to one of the following:" + System.lineSeparator() +
                    "1) http link to a master playlist; you will be given a choice of media playlists" + System.lineSeparator() +
                    "2) http link to a media playlist; it will be downloaded" + System.lineSeparator() +
                    "3) file containing lines of urls/other files" + System.lineSeparator() +
                    "All url/files are processed linearly and all duplicates are automatically ignored, " +
                    "regardless of how they are specified.";

}
