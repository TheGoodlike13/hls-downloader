package eu.goodlike.hls.download.m3u.data.builder;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.m3u.MediaPlaylist;
import eu.goodlike.hls.download.m3u.MediaPlaylistFactory;
import eu.goodlike.hls.download.m3u.MultiMediaPlaylist;
import eu.goodlike.hls.download.m3u.MultiMediaPlaylistFactory;
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
    private static final String FILENAME = "source.m3u";

    private static final String AUDIO_GROUP_ID = "audio";
    private static final String AUDIO_FILENAME = "audio.m3u";

    private final MasterPlaylistDataFactory masterFactory = playlists ->
            new MasterPlaylistData(playlists, null);

    private final MediaPlaylistFactory mediaPlaylistFactory = (name, resolution, url) ->
            new MediaPlaylist(name, resolution, url, null, null);

    private final MultiMediaPlaylistFactory multiMediaPlaylistFactory = (videoStream, audioStream) ->
            new MultiMediaPlaylist(videoStream, audioStream, null);

    private MasterPlaylistBuilder getMasterPlaylistBuilder(HttpUrl url) {
        return new MasterPlaylistBuilder(url, masterFactory, mediaPlaylistFactory, multiMediaPlaylistFactory);
    }

    @Test
    public void createMasterPlaylist() {
        PlaylistData testData = getMasterPlaylistBuilder(URL)
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
    public void createMasterPlaylistWithSeparateAudioAndVideoStreams() {
        PlaylistData testData = getMasterPlaylistBuilder(URL)
                .setNextGroupId(AUDIO_GROUP_ID).setNextString(AUDIO_FILENAME)
                .setNextAudioStreamId(AUDIO_GROUP_ID).setNextString(FILENAME)
                .build();

        MediaPlaylist audioPlaylist = mediaPlaylistFactory.createMediaPlaylist(null, null, URL.resolve(AUDIO_FILENAME));
        MediaPlaylist videoPlaylist = mediaPlaylistFactory.createMediaPlaylist(null, null, URL.resolve(FILENAME));
        MasterPlaylistData expected = masterFactory.createMasterPlaylistData(ImmutableList.of(
                audioPlaylist,
                multiMediaPlaylistFactory.createMultiMediaPlaylist(videoPlaylist, audioPlaylist)
        ));

        assertThat(testData).isEqualTo(expected);
    }

    @Test
    public void failOnMultipleNamesInARow() {
        HlsBuilder failBuilder = getMasterPlaylistBuilder(URL)
                .setNextPlaylistName(PLAYLIST_NAME);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextPlaylistName(PLAYLIST_NAME));
    }

    @Test
    public void failOnDanglingName() {
        HlsBuilder failBuilder = getMasterPlaylistBuilder(URL)
                .setNextString(FILENAME)
                .setNextPlaylistName(PLAYLIST_NAME);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(failBuilder::build);
    }

    @Test
    public void failOnMultipleResolutionsInARow() {
        HlsBuilder failBuilder = getMasterPlaylistBuilder(URL)
                .setNextPlaylistResolution(RESOLUTION);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextPlaylistResolution(RESOLUTION));
    }

    @Test
    public void failOnDanglingResolution() {
        HlsBuilder failBuilder = getMasterPlaylistBuilder(URL)
                .setNextString(FILENAME)
                .setNextPlaylistResolution(RESOLUTION);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(failBuilder::build);
    }

    @Test
    public void failOnNoPlaylists() {
        HlsBuilder failBuilder = getMasterPlaylistBuilder(URL);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(failBuilder::build);
    }

    @Test
    public void failOnMultipleGroupId() {
        HlsBuilder failBuilder = getMasterPlaylistBuilder(URL)
                .setNextGroupId(AUDIO_GROUP_ID);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextGroupId(AUDIO_GROUP_ID + "-other"));
    }

    @Test
    public void failOnDuplicateGroupId() {
        HlsBuilder failBuilder = getMasterPlaylistBuilder(URL)
                .setNextGroupId(AUDIO_GROUP_ID).setNextString(AUDIO_FILENAME);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextGroupId(AUDIO_GROUP_ID));
    }

    @Test
    public void failOnDuplicateAudioStreamId() {
        HlsBuilder failBuilder = getMasterPlaylistBuilder(URL)
                .setNextAudioStreamId(AUDIO_GROUP_ID);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> failBuilder.setNextAudioStreamId(AUDIO_GROUP_ID));
    }

    @Test
    public void failOnMissingAudioStreamId() {
        HlsBuilder failBuilder = getMasterPlaylistBuilder(URL)
                .setNextAudioStreamId(AUDIO_GROUP_ID).setNextString(FILENAME);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(failBuilder::build);
    }

}
