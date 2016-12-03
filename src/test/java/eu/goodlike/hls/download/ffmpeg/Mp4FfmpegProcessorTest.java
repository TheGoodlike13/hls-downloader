package eu.goodlike.hls.download.ffmpeg;

import eu.goodlike.cmd.ProcessHookAttacher;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mp4FfmpegProcessorTest {

    private final FfmpegFormatter ffmpegFormatter = Mockito.mock(FfmpegFormatter.class);
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ProcessHookAttacher processHookAttacher = new ProcessHookAttacher(executorService, executorService);
    private final FfmpegProcessor ffmpegProcessor = new Mp4FfmpegProcessor(ffmpegFormatter, processHookAttacher);

    @After
    public void tearDown() throws Exception {
        processHookAttacher.close();
    }

    @Test
    public void ffmpegProcessConfiguredAndHooked() {
        Process process = Mockito.mock(Process.class);
        Mockito.when(ffmpegFormatter.runFfmpeg("playlist.mp4", "playlist.m3u"))
                .thenReturn(Optional.of(process));

        ffmpegProcessor.processFfmpeg("playlist.m3u", "playlist.m3u");

        Mockito.verify(ffmpegFormatter).runFfmpeg("playlist.mp4", "playlist.m3u");
    }

}
