package eu.goodlike.hls.download.ffmpeg;

import java.util.List;
import java.util.Optional;

/**
 * Defines how to run certain arguments with ffmpeg command
 */
public interface FfmpegRunner {

    /**
     * Spawns an ffmpeg process with given parameters, which is also bound to the executing program
     *
     * @param args arguments to pass into ffmpeg
     * @return spawned ffmpeg process; {@link Optional#empty()} if process could not be spawned
     * @throws NullPointerException if args is or contains null
     */
    Optional<Process> runFfmpeg(List<String> args);

}
