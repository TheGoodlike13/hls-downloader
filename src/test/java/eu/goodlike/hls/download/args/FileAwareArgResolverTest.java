package eu.goodlike.hls.download.args;

import com.google.common.collect.ImmutableList;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class FileAwareArgResolverTest {

    private static final String FILE_1 = "FileAwareArgumentGrouperTest1.txt";
    private static final String FILE_2 = "FileAwareArgumentGrouperTest2.txt";

    private final ArgResolver argResolver = new FileAwareArgResolver();

    @Before
    public void setUp() throws IOException {
        List<String> file1Lines = ImmutableList.of(
                newUrl(1).toString(),
                newUrl(2).toString(),
                FILE_2,
                newUrl(4).toString());

        List<String> file2Lines = ImmutableList.of(
                newUrl(3).toString(),
                newUrl(2).toString(),
                FILE_1
        );

        Files.write(Paths.get(FILE_1), file1Lines, StandardOpenOption.CREATE_NEW);
        Files.write(Paths.get(FILE_2), file2Lines, StandardOpenOption.CREATE_NEW);
    }

    @After
    public void tearDown() throws IOException {
        Files.delete(Paths.get(FILE_1));
        Files.delete(Paths.get(FILE_2));
    }


    private HttpUrl newUrl(int index) {
        return HttpUrl.parse("https://localhost/" + index);
    }

    @Test
    public void parsingDoesNotLoopEndlesslyAndDoesNot() {
        Set<HttpUrl> allUrls = argResolver.getAllUrls(ImmutableList.of(
                newUrl(0).toString(),
                FILE_1,
                FILE_2
        ));

        assertThat(allUrls)
                .containsExactly(newUrl(0), newUrl(1), newUrl(2), newUrl(3), newUrl(4));
    }

}
