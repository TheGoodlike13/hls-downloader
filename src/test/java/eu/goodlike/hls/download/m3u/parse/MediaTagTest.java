package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import org.junit.Test;
import org.mockito.Mockito;

public class MediaTagTest {

    private static final String PLAYLIST_NAME = "playlistName";

    private final HlsBuilder builder = Mockito.mock(HlsBuilder.class);

    @Test
    public void tagWritesItself() {
        Mockito.when(builder.setNextPlaylistName(PLAYLIST_NAME)).thenReturn(builder);

        new MediaTag(PLAYLIST_NAME).extractDataInto(builder);

        Mockito.verify(builder).setNextPlaylistName(PLAYLIST_NAME);
    }

}
