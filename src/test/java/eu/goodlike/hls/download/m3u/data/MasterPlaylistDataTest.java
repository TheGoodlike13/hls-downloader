package eu.goodlike.hls.download.m3u.data;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.m3u.DownloadableMediaPlaylist;
import eu.goodlike.io.InputReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.times;

public class MasterPlaylistDataTest {

    private DownloadableMediaPlaylist mediaPlaylist;
    private InputReader inputReader;
    private MasterPlaylistData masterPlaylistData;

    @Before
    public void setUp() {
        mediaPlaylist = Mockito.mock(DownloadableMediaPlaylist.class);
        inputReader = Mockito.mock(InputReader.class);
        masterPlaylistData = new MasterPlaylistData(ImmutableList.of(mediaPlaylist), inputReader);
    }

    @Test
    public void playlistIsChosenByUserInput() {
        Mockito.when(inputReader.readLine())
                .thenReturn("basdcbnlasdcukadsc")   // random input
                .thenReturn("10")                      // invalid position
                .thenReturn("1");                      // valid position

        Mockito.when(mediaPlaylist.toString()).thenReturn("playlist_name");

        Mockito.when(mediaPlaylist.download()).thenReturn(CompletableFuture.completedFuture(null));

        masterPlaylistData.handlePlaylistData().join();

        Mockito.verify(mediaPlaylist).download();
        Mockito.verify(inputReader, times(3)).readLine();
    }

}
