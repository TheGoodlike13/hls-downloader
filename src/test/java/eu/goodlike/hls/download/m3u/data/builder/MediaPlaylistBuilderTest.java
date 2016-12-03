package eu.goodlike.hls.download.m3u.data.builder;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.m3u.data.MediaPart;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistDataFactory;
import eu.goodlike.hls.download.m3u.data.PlaylistData;
import okhttp3.HttpUrl;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MediaPlaylistBuilderTest {

    private static final String PLAYLIST_FILENAME = "playlist.m3u";
    private static final HttpUrl URL = HttpUrl.parse("https:/localhost:8080/");
    private static final BigDecimal TARGET_DURATION = BigDecimal.ONE;
    private static final String FILENAME = "file.txt";

    private final MediaPlaylistDataFactory mediaFactory = (filename, targetDuration, mediaParts) ->
            new MediaPlaylistData(filename, targetDuration, mediaParts, null);


    private MediaPlaylistBuilder getMediaPlaylistBuilder(HttpUrl url) {
        return new MediaPlaylistBuilder(url, mediaFactory);
    }

    @Test
    public void createMediaPlaylist() {
        PlaylistData testData = getMediaPlaylistBuilder(URL)
                .setTargetDuration(TARGET_DURATION)
                .setNextPartDuration(TARGET_DURATION)
                .setNextString(FILENAME)
                .build();

        MediaPlaylistData expected = mediaFactory.createMediaPlaylistData(PLAYLIST_FILENAME, TARGET_DURATION,
                ImmutableList.of(new MediaPart(TARGET_DURATION, URL.resolve(FILENAME))));

        assertThat(testData).isEqualTo(expected);
    }

    @Test
    public void failOnMissingTargetDuration() {
        HlsBuilder failBuilder = getMediaPlaylistBuilder(URL);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextPartDuration(TARGET_DURATION).build());
    }

    @Test
    public void failOnDuplicateTargetDuration() {
        HlsBuilder failBuilder = getMediaPlaylistBuilder(URL)
                .setTargetDuration(TARGET_DURATION);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setTargetDuration(TARGET_DURATION));
    }

    @Test
    public void failOnNoParts() {
        HlsBuilder failBuilder = getMediaPlaylistBuilder(URL)
                .setTargetDuration(TARGET_DURATION);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(failBuilder::build);
    }

    @Test
    public void failOnMultiplePartDurationsInARow() {
        HlsBuilder failBuilder = getMediaPlaylistBuilder(URL)
                .setTargetDuration(TARGET_DURATION)
                .setNextPartDuration(TARGET_DURATION);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextPartDuration(TARGET_DURATION));
    }

    @Test
    public void failOnPartWithoutDuration() {
        HlsBuilder failBuilder = getMediaPlaylistBuilder(URL)
                .setTargetDuration(TARGET_DURATION);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextString(FILENAME));
    }

    @Test
    public void failOnDanglingDurationTag() {
        HlsBuilder failBuilder = getMediaPlaylistBuilder(URL)
                .setTargetDuration(TARGET_DURATION)
                .setNextPartDuration(TARGET_DURATION);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(failBuilder::build);
    }

    @Test
    public void failOnFilenameInvalidForUrl() {
        HlsBuilder failBuilder = getMediaPlaylistBuilder(URL)
                .setTargetDuration(TARGET_DURATION)
                .setNextPartDuration(TARGET_DURATION);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextString("ftp://haha!"));
    }

}
