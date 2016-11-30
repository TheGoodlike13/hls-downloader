package eu.goodlike.hls.download.m3u.data;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.m3u.DownloadableMediaPlaylist;
import eu.goodlike.io.InputReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class MasterPlaylistDataTest {

    private final InputReader inputReader = Mockito.mock(InputReader.class);

    private MasterPlaylistData newMasterPlaylistData(DownloadableMediaPlaylist... mediaPlaylists) {
        return new MasterPlaylistData(ImmutableList.copyOf(mediaPlaylists), inputReader);
    }

    @Test
    public void playlistIsChosenAutomaticallyWhenOnlyOneIsAvailable() {
        DownloadableMediaPlaylist mediaPlaylist = Mockito.mock(DownloadableMediaPlaylist.class);
        MasterPlaylistData masterPlaylistData = newMasterPlaylistData(mediaPlaylist);
        Mockito.when(mediaPlaylist.download()).thenReturn(CompletableFuture.completedFuture(null));

        masterPlaylistData.handlePlaylistData().join();

        Mockito.verify(mediaPlaylist).download();
        Mockito.verify(inputReader, never()).readLine();
    }

    @Test
    public void playlistIsChosenByUserInput() {
        DownloadableMediaPlaylist chosenMediaPlaylist = Mockito.mock(DownloadableMediaPlaylist.class);
        DownloadableMediaPlaylist otherMediaPlaylist = Mockito.mock(DownloadableMediaPlaylist.class);

        MasterPlaylistData masterPlaylistData = newMasterPlaylistData(chosenMediaPlaylist, otherMediaPlaylist);

        Mockito.when(inputReader.readLine())
                .thenReturn("basdcbnlasdcukadsc")   // random input
                .thenReturn("10")                      // invalid position
                .thenReturn("1");                      // valid position

        Mockito.when(chosenMediaPlaylist.download()).thenReturn(CompletableFuture.completedFuture(null));
        Mockito.when(otherMediaPlaylist.download()).thenReturn(CompletableFuture.completedFuture(null));

        masterPlaylistData.handlePlaylistData().join();

        Mockito.verify(chosenMediaPlaylist).download();
        Mockito.verify(otherMediaPlaylist, never()).download();
        Mockito.verify(inputReader, times(3)).readLine();
    }

}
