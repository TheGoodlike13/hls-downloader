package eu.goodlike.hls.download.m3u.data;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.ffmpeg.FfmpegProcessor;
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
import java.util.concurrent.CompletableFuture;

public class MediaPlaylistDataTest {

    private static final String FILENAME = "stream.m3u";
    private static final BigDecimal TARGET_DURATION = BigDecimal.ONE;
    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");
    private static final List<MediaPart> MEDIA_PARTS = ImmutableList.of(new MediaPart(TARGET_DURATION, URL));

    private FfmpegProcessor ffmpegProcessor;
    private MediaPlaylistData mediaPlaylistData;

    @Before
    public void setUp() {
        ffmpegProcessor = Mockito.mock(FfmpegProcessor.class);
        mediaPlaylistData = new MediaPlaylistData(FILENAME, TARGET_DURATION, MEDIA_PARTS, ffmpegProcessor);
    }

    @After
    public void tearDown() throws Exception {
        Path path = Paths.get(FILENAME);
        if (Files.exists(path))
            Files.delete(path);
    }

    @Test
    public void correctFfmpegCallMade() throws InterruptedException {
        Mockito.when(ffmpegProcessor.processFfmpeg(FILENAME, FILENAME))
                .thenReturn(CompletableFuture.completedFuture(null));

        mediaPlaylistData.handlePlaylistData().join();

        Mockito.verify(ffmpegProcessor).processFfmpeg(FILENAME, FILENAME);
    }

}
