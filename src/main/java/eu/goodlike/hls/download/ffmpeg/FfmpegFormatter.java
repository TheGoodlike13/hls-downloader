package eu.goodlike.hls.download.ffmpeg;

import java.util.Optional;

/**
 * Creates output using input and ffmpeg
 */
public interface FfmpegFormatter {

    /**
     * Spawns an ffmpeg process with given input, output and any additional params provided by the implementation
     *
     * @param outputFileName output file name
     * @param inputFileNames input file names
     * @return spawned ffmpeg process; {@link Optional#empty()} if process could not be spawned
     * @throws NullPointerException if inputFileNames or outputFileName is or contains null
     */
    Optional<Process> runFfmpeg(String outputFileName, String... inputFileNames);

}
