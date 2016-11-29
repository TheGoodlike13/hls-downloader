package eu.goodlike.hls.download.m3u.data;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.m3u.FileMediaPlaylistWriter;
import eu.goodlike.hls.download.m3u.MediaPlaylistWriter;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MediaPartTest {

    private static final Path PATH = Paths.get("mediaPartTest.txt");
    private static final BigDecimal DURATION = BigDecimal.ONE;
    private static final HttpUrl LOCATION = HttpUrl.parse("https://localhost:8080/");

    @After
    public void tearDown() throws IOException {
        if (Files.exists(PATH))
            Files.delete(PATH);
    }

    @Test
    public void correctlyWritesItself() throws Exception {
        MediaPart mediaPart = new MediaPart(DURATION, LOCATION);

        try (MediaPlaylistWriter<Path> writer = new FileMediaPlaylistWriter(PATH)) {
            mediaPart.writeInto(writer);
        }

        List<String> rows = Files.readAllLines(PATH);

        List<String> expected = ImmutableList.of(
                "#EXTINF:1,",
                "https://localhost:8080/"
        );

        assertThat(rows).isEqualTo(expected);
    }

}
