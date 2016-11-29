package eu.goodlike.hls.download.m3u;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.m3u.data.PlaylistData;
import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import eu.goodlike.hls.download.m3u.data.builder.MediaPlaylistBuilderFactory;
import eu.goodlike.hls.download.m3u.parse.HlsParser;
import eu.goodlike.hls.download.m3u.parse.StreamPartDurationTag;
import eu.goodlike.hls.download.m3u.parse.TargetDurationTag;
import eu.goodlike.hls.download.m3u.parse.UrlTag;
import okhttp3.HttpUrl;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class MediaPlaylistTest {

    private static final String PLAYLIST_NAME = "source";
    private static final String RESOLUTION = "1920x1080";
    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");

    private final HlsParser hlsParser = Mockito.mock(HlsParser.class);
    private final HlsBuilder hlsBuilder = Mockito.mock(HlsBuilder.class);
    private final MediaPlaylistBuilderFactory mediaPlaylistBuilderFactory = source -> hlsBuilder;
    private final MediaPlaylist mediaPlaylist = new MediaPlaylist(PLAYLIST_NAME, RESOLUTION, URL,
            hlsParser, mediaPlaylistBuilderFactory);

    @Test
    public void downloadsItself() {
        TargetDurationTag targetDurationTag = new TargetDurationTag(BigDecimal.TEN);
        StreamPartDurationTag partDurationTag = new StreamPartDurationTag(BigDecimal.ONE);
        UrlTag urlTag = new UrlTag(URL);

        Mockito.when(hlsParser.parse(URL)).thenReturn(ImmutableList.of(targetDurationTag, partDurationTag, urlTag));
        Mockito.when(hlsBuilder.setTargetDuration(BigDecimal.TEN)).thenReturn(hlsBuilder);
        Mockito.when(hlsBuilder.setNextPartDuration(BigDecimal.ONE)).thenReturn(hlsBuilder);
        Mockito.when(hlsBuilder.setNextUrl(URL)).thenReturn(hlsBuilder);

        PlaylistData playlistData = Mockito.mock(PlaylistData.class);
        Mockito.when(playlistData.handlePlaylistData()).thenReturn(CompletableFuture.completedFuture(null));

        Mockito.when(hlsBuilder.build()).thenReturn(playlistData);

        mediaPlaylist.download().join();

        Mockito.verify(hlsParser).parse(URL);
        Mockito.verify(hlsBuilder).setTargetDuration(BigDecimal.TEN);
        Mockito.verify(hlsBuilder).setNextPartDuration(BigDecimal.ONE);
        Mockito.verify(hlsBuilder).setNextUrl(URL);
        Mockito.verify(playlistData).handlePlaylistData();
        Mockito.verify(hlsBuilder).build();
    }

}
