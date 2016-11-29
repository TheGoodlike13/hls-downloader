package eu.goodlike.hls.download.m3u.data.builder;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.m3u.MediaPlaylist;
import eu.goodlike.hls.download.m3u.MediaPlaylistFactory;
import eu.goodlike.hls.download.m3u.data.MasterPlaylistData;
import eu.goodlike.hls.download.m3u.data.MasterPlaylistDataFactory;
import eu.goodlike.hls.download.m3u.data.PlaylistData;
import okhttp3.HttpUrl;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MasterPlaylistBuilderTest {

    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/");
    private static final String PLAYLIST_NAME = "source";
    private static final String RESOLUTION = "1920x1080";
    private static final String FILENAME = "file.txt";

    private final MasterPlaylistDataFactory masterFactory = playlists ->
            new MasterPlaylistData(playlists, null);

    private final MediaPlaylistFactory mediaPlaylistFactory = (name, resolution, url) ->
            new MediaPlaylist(name, resolution, url, null, null);

    @Test
    public void createMasterPlaylist() {
        PlaylistData testData = new MasterPlaylistBuilder(URL, masterFactory, mediaPlaylistFactory)
                .setNextPlaylistName(PLAYLIST_NAME)
                .setNextPlaylistResolution(RESOLUTION)
                .setNextString(FILENAME)
                .build();

        MasterPlaylistData expected = masterFactory.createMasterPlaylistData(
                ImmutableList.of(mediaPlaylistFactory.createMediaPlaylist(PLAYLIST_NAME, RESOLUTION, URL.resolve(FILENAME)))
        );

        assertThat(testData).isEqualTo(expected);
    }

    @Test
    public void failOnMultipleNamesInARow() {
        HlsBuilder failBuilder = new MasterPlaylistBuilder(URL, masterFactory, mediaPlaylistFactory)
                .setNextPlaylistName(PLAYLIST_NAME);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextPlaylistName(PLAYLIST_NAME));
    }

    @Test
    public void failOnDanglingName() {
        HlsBuilder failBuilder = new MasterPlaylistBuilder(URL, masterFactory, mediaPlaylistFactory)
                .setNextString(FILENAME)
                .setNextPlaylistName(PLAYLIST_NAME);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(failBuilder::build);
    }

    @Test
    public void failOnMultipleResolutionsInARow() {
        HlsBuilder failBuilder = new MasterPlaylistBuilder(URL, masterFactory, mediaPlaylistFactory)
                .setNextPlaylistResolution(RESOLUTION);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextPlaylistResolution(RESOLUTION));
    }

    @Test
    public void failOnDanglingResolution() {
        HlsBuilder failBuilder = new MasterPlaylistBuilder(URL, masterFactory, mediaPlaylistFactory)
                .setNextString(FILENAME)
                .setNextPlaylistResolution(RESOLUTION);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(failBuilder::build);
    }

    @Test
    public void failOnNoPlaylists() {
        HlsBuilder failBuilder = new MasterPlaylistBuilder(URL, masterFactory, mediaPlaylistFactory);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(failBuilder::build);
    }

}
