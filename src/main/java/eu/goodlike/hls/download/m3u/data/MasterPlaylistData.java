package eu.goodlike.hls.download.m3u.data;

import com.google.common.base.MoreObjects;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.goodlike.hls.download.m3u.DownloadableMediaPlaylist;
import eu.goodlike.io.InputReader;
import eu.goodlike.neat.Null;
import eu.goodlike.validate.impl.StringValidator;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static eu.goodlike.validate.CommonValidators.NOT_BLANK;
import static eu.goodlike.validate.Validate.aPrimInt;

/**
 * Defines how master playlist should handle its own data
 */
public final class MasterPlaylistData implements PlaylistData {

    @Override
    public CompletableFuture<?> handlePlaylistData() {
        return choosePlaylist().download();
    }

    // CONSTRUCTORS

    @Inject
    public MasterPlaylistData(@Assisted List<DownloadableMediaPlaylist> playlists,
                              InputReader userInputReader) {
        Null.checkList(playlists).as("playlists");
        if (playlists.isEmpty())
            throw new IllegalArgumentException("Master playlist must have at least one media playlist");

        this.playlists = playlists;

        this.userInputReader = userInputReader;
    }

    // PRIVATE

    private final List<DownloadableMediaPlaylist> playlists;

    private final InputReader userInputReader;

    private DownloadableMediaPlaylist choosePlaylist() {
        return playlists.size() < 2
                ? playlists.get(0)
                : letUserChoosePlaylist();
    }

    private DownloadableMediaPlaylist letUserChoosePlaylist() {
        printPlaylists();

        StringValidator positionValidator = NOT_BLANK.isInt(aPrimInt().isBetween(1, playlists.size()));
        String chosenPosition = userInputReader.readLine();
        while (positionValidator.isInvalid(chosenPosition)) {
            System.out.println(System.lineSeparator() + "Invalid position: " + chosenPosition + System.lineSeparator());
            printPlaylists();
            chosenPosition = userInputReader.readLine();
        }

        return playlists.get(Integer.valueOf(chosenPosition) - 1);
    }

    private void printPlaylists() {
        System.out.println("Available playlists:" + System.lineSeparator());

        int size = playlists.size();
        int digitCount = digitCount(size);
        for (int i = 0; i < size; i++)
            System.out.println(padIfNeeded(i + 1, digitCount) + ": " + playlists.get(i));

        System.out.print(System.lineSeparator() + "Please choose by position:");
    }

    private int digitCount(int number) {
        int count = 0;
        while (number > 0) {
            number /= 10;
            count++;
        }
        return count;
    }

    private String padIfNeeded(int number, int digits) {
        String numberString = String.valueOf(number);
        int necessaryPadding = digits - numberString.length();
        if (necessaryPadding <= 0)
            return numberString;

        StringBuilder builder = new StringBuilder();
        while (necessaryPadding-- > 0)
            builder.append(" ");

        return builder.append(numberString).toString();
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("MasterPlaylistData")
                .add("playlists", playlists)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MasterPlaylistData)) return false;
        MasterPlaylistData that = (MasterPlaylistData) o;
        return Objects.equals(playlists, that.playlists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlists);
    }

}
