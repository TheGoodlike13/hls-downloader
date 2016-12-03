package eu.goodlike.hls.download.m3u.data;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.ffmpeg.FfmpegProcessor;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MultiMediaPlaylistDataTest {

    private static final String VIDEO_FILENAME = "video.m3u";
    private static final BigDecimal VIDEO_TARGET_DURATION = BigDecimal.ONE;
    private static final HttpUrl VIDEO_URL = HttpUrl.parse("https://localhost:8080/");
    private static final List<MediaPart> VIDEO_MEDIA_PARTS = ImmutableList.of(new MediaPart(VIDEO_TARGET_DURATION, VIDEO_URL));

    private static final String AUDIO_FILENAME = "audio.m3u";
    private static final BigDecimal AUDIO_TARGET_DURATION = BigDecimal.ONE;
    private static final HttpUrl AUDIO_URL = HttpUrl.parse("https://localhost:8080/");
    private static final List<MediaPart> AUDIO_MEDIA_PARTS = ImmutableList.of(new MediaPart(AUDIO_TARGET_DURATION, AUDIO_URL));

    private final FfmpegProcessor ffmpegProcessor = Mockito.mock(FfmpegProcessor.class);
    private final MediaPlaylistData video =
            new MediaPlaylistData(VIDEO_FILENAME, VIDEO_TARGET_DURATION, VIDEO_MEDIA_PARTS, ffmpegProcessor);

    private final MediaPlaylistData audio =
            new MediaPlaylistData(AUDIO_FILENAME, AUDIO_TARGET_DURATION, AUDIO_MEDIA_PARTS, ffmpegProcessor);

    private final MultiMediaPlaylistData multiMediaPlaylistData =
            new MultiMediaPlaylistData(video, audio, ffmpegProcessor);

    @After
    public void tearDown() throws Exception {
        Path videoPath = Paths.get(VIDEO_FILENAME);
        if (Files.exists(videoPath))
            Files.delete(videoPath);

        Path audioPath = Paths.get(AUDIO_FILENAME);
        if (Files.exists(audioPath))
            Files.delete(audioPath);
    }

    @Test
    public void correctFfmpegCallMade() throws InterruptedException {
        Mockito.when(ffmpegProcessor.processFfmpeg(VIDEO_FILENAME, VIDEO_FILENAME, AUDIO_FILENAME))
                .thenReturn(CompletableFuture.completedFuture(null));

        multiMediaPlaylistData.handlePlaylistData().join();

        Mockito.verify(ffmpegProcessor).processFfmpeg(VIDEO_FILENAME, VIDEO_FILENAME, AUDIO_FILENAME);
    }

}
