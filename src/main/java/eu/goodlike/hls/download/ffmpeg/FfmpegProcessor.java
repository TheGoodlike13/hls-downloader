package eu.goodlike.hls.download.ffmpeg;

import java.util.concurrent.CompletableFuture;

/**
 * Defines how to hook onto an ffmpeg process to ensure completion
 */
public interface FfmpegProcessor {

    /**
     * Spawns an ffmpeg process with given input, output and any additional params provided by the implementation
     *
     * @param output output file name
     * @param firstInput input file name
     * @param inputs additional input file names
     * @return future which completes when the process finishes
     * @throws NullPointerException if output, firstInput or inputs is or contains null
     */
    CompletableFuture<?> processFfmpeg(String output, String firstInput, String... inputs);

}
