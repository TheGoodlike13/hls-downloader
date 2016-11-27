package eu.goodlike.hls.download.ffmpeg;

import eu.goodlike.cmd.ProcessRunner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandLineFfmpegRunnerTest {

    private ProcessRunner processRunner;
    private FfmpegRunner ffmpegRunner;

    @Before
    public void setUp() {
        processRunner = Mockito.mock(ProcessRunner.class);
        ffmpegRunner = new CommandLineFfmpegRunner(processRunner);
    }

    @Test
    public void runsFfmpegCommandLine() {
        Process process = Mockito.mock(Process.class);

        Mockito.when(processRunner.execute("ffmpeg", Collections.emptyList()))
                .thenReturn(Optional.of(process));

        assertThat(ffmpegRunner.runFfmpeg(Collections.emptyList()))
                .contains(process);
    }

}
