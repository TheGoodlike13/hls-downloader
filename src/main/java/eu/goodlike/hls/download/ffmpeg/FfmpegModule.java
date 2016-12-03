package eu.goodlike.hls.download.ffmpeg;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import eu.goodlike.cmd.*;
import eu.goodlike.libraries.slf4j.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class FfmpegModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FfmpegRunner.class)
                .to(CommandLineFfmpegRunner.class)
                .in(Singleton.class);

        bind(FfmpegFormatter.class)
                .to(HlsFfmpegFormatter.class)
                .in(Singleton.class);

        bind(FfmpegProcessor.class)
                .to(Mp4FfmpegProcessor.class)
                .in(Singleton.class);
    }

    @Provides
    @Singleton
    ProcessHookAttacher processHookAttacher() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        return new ProcessHookAttacher(threadPool, threadPool);
    }

    @Provides
    @Singleton
    ProcessRunner getProcessRunner(ProcessHookAttacher processHookAttacher) {
        return new BoundProcessRunner(new LoggingProcessRunner(new CommandLineRunner(), Log.INFO, processHookAttacher));
    }

}
