package eu.goodlike.hls.download.m3u;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.ffmpeg.FfmpegProcessor;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistDataFactory;
import eu.goodlike.hls.download.m3u.data.MultiMediaPlaylistData;
import eu.goodlike.hls.download.m3u.data.MultiMediaPlaylistDataFactory;
import eu.goodlike.hls.download.m3u.data.builder.MediaPlaylistBuilder;
import eu.goodlike.hls.download.m3u.data.builder.MediaPlaylistBuilderFactory;
import eu.goodlike.hls.download.m3u.parse.HlsParser;
import eu.goodlike.hls.download.m3u.parse.RawString;
import eu.goodlike.hls.download.m3u.parse.StreamPartDurationTag;
import eu.goodlike.hls.download.m3u.parse.TargetDurationTag;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class MultiMediaPlaylistTest {

    private static final String PLAYLIST_NAME = "source";
    private static final String RESOLUTION = "1920x1080";
    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/source.m3u");

    private static final String AUDIO_PLAYLIST_NAME = "audio";
    private static final HttpUrl AUDIO_URL = HttpUrl.parse("https://localhost:8080/audio.m3u");

    private final HlsParser hlsParser = Mockito.mock(HlsParser.class);

    private final FfmpegProcessor ffmpegProcessor = Mockito.mock(FfmpegProcessor.class);
    private final MediaPlaylistDataFactory mediaPlaylistDataFactory =
            (filename, targetDuration, mediaParts) -> new MediaPlaylistData(filename, targetDuration, mediaParts, ffmpegProcessor);
    private final MediaPlaylistBuilderFactory mediaPlaylistBuilderFactory =
            source -> new MediaPlaylistBuilder(source, mediaPlaylistDataFactory);

    private final MediaPlaylist video = new MediaPlaylist(PLAYLIST_NAME, RESOLUTION, URL,
            hlsParser, mediaPlaylistBuilderFactory);

    private final MediaPlaylist audio = new MediaPlaylist(AUDIO_PLAYLIST_NAME, null, AUDIO_URL,
            hlsParser, mediaPlaylistBuilderFactory);

    private final MultiMediaPlaylistDataFactory multiMediaPlaylistDataFactory =
            (videoData, audioData) -> new MultiMediaPlaylistData(videoData, audioData, ffmpegProcessor);

    private final MultiMediaPlaylist multiMediaPlaylist = new MultiMediaPlaylist(video, audio, multiMediaPlaylistDataFactory);

    @After
    public void tearDown() throws IOException {
        Path videoPath = Paths.get("source.m3u");
        if (Files.exists(videoPath))
            Files.delete(videoPath);

        Path audioPath = Paths.get("audio.m3u");
        if (Files.exists(audioPath))
            Files.delete(audioPath);
    }

    @Test
    public void downloadsBothVideoAndAudio() {
        TargetDurationTag targetDurationTag = new TargetDurationTag(BigDecimal.TEN);
        StreamPartDurationTag partDurationTag = new StreamPartDurationTag(BigDecimal.ONE);
        RawString videoUrl = new RawString(URL.toString());
        RawString audioUrl = new RawString(AUDIO_URL.toString());

        Mockito.when(hlsParser.parse(URL)).thenReturn(ImmutableList.of(targetDurationTag, partDurationTag, videoUrl));
        Mockito.when(hlsParser.parse(AUDIO_URL)).thenReturn(ImmutableList.of(targetDurationTag, partDurationTag, audioUrl));

        Mockito.when(ffmpegProcessor.processFfmpeg("source.m3u", "source.m3u", "audio.m3u"))
                .thenReturn(CompletableFuture.completedFuture(null));

        multiMediaPlaylist.download().join();

        Mockito.verify(hlsParser).parse(URL);
        Mockito.verify(hlsParser).parse(AUDIO_URL);
        Mockito.verify(ffmpegProcessor).processFfmpeg("source.m3u", "source.m3u", "audio.m3u");
    }

}
