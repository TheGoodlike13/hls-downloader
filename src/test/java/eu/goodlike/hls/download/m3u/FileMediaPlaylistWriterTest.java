package eu.goodlike.hls.download.m3u;

import com.google.common.collect.ImmutableList;
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

public class FileMediaPlaylistWriterTest {

    private static final Path PATH = Paths.get("fileMediaPlaylistWriterTest.txt");
    private static final BigDecimal DURATION = BigDecimal.ONE;
    private static final HttpUrl LOCATION = HttpUrl.parse("https://localhost:8080/");

    @After
    public void tearDown() throws IOException {
        if (Files.exists(PATH))
            Files.delete(PATH);
    }

    @Test
    public void writesFile() throws Exception {
        try (MediaPlaylistWriter<Path> writer = new FileMediaPlaylistWriter(PATH)) {
            writer.writeStart()
                    .writeTargetDuration(DURATION)
                    .writeDurationTag(DURATION)
                    .writeLocation(LOCATION)
                    .writeEnd();
        }

        List<String> rows = Files.readAllLines(PATH);

        List<String> expected = ImmutableList.of(
                "#EXTM3U",
                "",
                "#EXT-X-TARGETDURATION:1",
                "",
                "#EXTINF:1,",
                "https://localhost:8080/",
                "",
                "#EXT-X-ENDLIST",
                ""
        );

        assertThat(rows).isEqualTo(expected);
    }

}
