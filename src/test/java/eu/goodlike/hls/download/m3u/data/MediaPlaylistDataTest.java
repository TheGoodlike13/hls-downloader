package eu.goodlike.hls.download.m3u.data;

import com.google.common.collect.ImmutableList;
import eu.goodlike.cmd.ProcessHookAttacher;
import eu.goodlike.hls.download.ffmpeg.FfmpegFormatter;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaPlaylistDataTest {

    private static final String FILENAME = "stream.m3u";
    private static final BigDecimal TARGET_DURATION = BigDecimal.ONE;
    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");
    private static final List<MediaPart> MEDIA_PARTS = ImmutableList.of(new MediaPart(TARGET_DURATION, URL));

    private FfmpegFormatter ffmpegFormatter;
    private ProcessHookAttacher processHookAttacher;
    private MediaPlaylistData mediaPlaylistData;

    @Before
    public void setUp() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ffmpegFormatter = Mockito.mock(FfmpegFormatter.class);
        processHookAttacher = new ProcessHookAttacher(executorService, executorService);
        mediaPlaylistData = new MediaPlaylistData(FILENAME, TARGET_DURATION, MEDIA_PARTS, ffmpegFormatter, processHookAttacher);
    }

    @After
    public void tearDown() throws Exception {
        processHookAttacher.close();

        Path path = Paths.get(FILENAME);
        if (Files.exists(path))
            Files.delete(path);
    }

    @Test
    public void correctFfmpegCallMade() throws InterruptedException {
        Process process = Mockito.mock(Process.class);
        Mockito.when(process.waitFor()).thenReturn(0);

        Mockito.when(ffmpegFormatter.runFfmpeg("stream.mp4", FILENAME))
                .thenReturn(Optional.of(process));

        CompletableFuture<?> doneHandlingMediaPlaylist = mediaPlaylistData.handlePlaylistData();
        doneHandlingMediaPlaylist.join();

        Mockito.verify(ffmpegFormatter).runFfmpeg("stream.mp4", FILENAME);
    }

}
