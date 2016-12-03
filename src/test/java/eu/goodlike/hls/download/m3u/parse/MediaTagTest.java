package eu.goodlike.hls.download.m3u.parse;

import eu.goodlike.hls.download.m3u.data.builder.HlsBuilder;
import org.junit.Test;
import org.mockito.Mockito;

public class MediaTagTest {

    private static final String GROUP_ID = "audio";
    private static final String PLAYLIST_NAME = "playlistName";
    private static final String URI = "playlist.m3u";

    private final HlsBuilder builder = Mockito.mock(HlsBuilder.class);

    @Test
    public void tagWritesItself() {
        Mockito.when(builder.setNextGroupId(GROUP_ID)).thenReturn(builder);
        Mockito.when(builder.setNextPlaylistName(PLAYLIST_NAME)).thenReturn(builder);
        Mockito.when(builder.setNextString(URI)).thenReturn(builder);

        new MediaTag(GROUP_ID, PLAYLIST_NAME, URI).extractDataInto(builder);

        Mockito.verify(builder).setNextGroupId(GROUP_ID);
        Mockito.verify(builder).setNextPlaylistName(PLAYLIST_NAME);
        Mockito.verify(builder).setNextString(URI);
    }

}
