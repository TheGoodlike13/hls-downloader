package eu.goodlike.hls.download.ffmpeg;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import eu.goodlike.cmd.*;
import eu.goodlike.libraries.slf4j.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class FfmpegModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    ProcessHookAttacher processHookAttacher() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        return new ProcessHookAttacher(threadPool, threadPool);
    }

    @Provides
    @Singleton
    ProcessRunner getProcessRunner(@Named("ffmpeg-log-level") Log log, ProcessHookAttacher processHookAttacher) {
        return new BoundProcessRunner(new LoggingProcessRunner(new CommandLineRunner(), log, processHookAttacher));
    }

}
