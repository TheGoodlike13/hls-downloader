package eu.goodlike.hls.download.ffmpeg;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class HlsFfmpegFormatterTest {

    private static final String INPUT_FILE = "NewHLS-1080P.m3u8";
    private static final String OUTPUT_FILE = "NewHLS-1080P.mp4";

    private FfmpegRunner ffmpegRunner;
    private FfmpegFormatter ffmpegFormatter;

    @Before
    public void setUp() {
        ffmpegRunner = Mockito.mock(FfmpegRunner.class);
        ffmpegFormatter = new HlsFfmpegFormatter(ffmpegRunner);
    }

    @Test
    public void ffmpegParamsAreCorrectlyFormatted() {
        Process process = Mockito.mock(Process.class);
        List<String> expectedInput = ImmutableList.of(
            "-i", INPUT_FILE, "-bsf:a", "aac_adtstoasc", "-c", "copy", OUTPUT_FILE
        );

        Mockito.when(ffmpegRunner.runFfmpeg(expectedInput))
                .thenReturn(Optional.of(process));

        assertThat(ffmpegFormatter.runFfmpeg(INPUT_FILE, OUTPUT_FILE))
                .contains(process);
    }

}
