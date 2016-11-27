package eu.goodlike.hls.download.ffmpeg;

import java.util.Optional;

/**
 * Creates output using input and ffmpeg
 */
public interface FfmpegFormatter {

    /**
     * Spawns an ffmpeg process with given input, output and any additional params provided by the implementation
     *
     * @param inputFileName input file name
     * @param outputFileName output file name
     * @return spawned ffmpeg process; {@link Optional#empty()} if process could not be spawned
     * @throws NullPointerException if inputFileName or outputFileName is null
     */
    Optional<Process> runFfmpeg(String inputFileName, String outputFileName);

}
