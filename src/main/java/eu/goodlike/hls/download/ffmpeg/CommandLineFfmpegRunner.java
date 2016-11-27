package eu.goodlike.hls.download.ffmpeg;

import com.google.inject.Inject;
import eu.goodlike.cmd.ProcessRunner;

import java.util.List;
import java.util.Optional;

/**
 * Runs ffmpeg command on a CLI
 */
public final class CommandLineFfmpegRunner implements FfmpegRunner {

    @Override
    public Optional<Process> runFfmpeg(List<String> args) {
        return processRunner.execute(FFMPEG_COMMAND, args);
    }

    // CONSTRUCTORS

    @Inject CommandLineFfmpegRunner(ProcessRunner processRunner) {
        this.processRunner = processRunner;
    }

    // PRIVATE

    private final ProcessRunner processRunner;

    private static final String FFMPEG_COMMAND = "ffmpeg";

}
