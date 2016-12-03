package eu.goodlike.hls.download.ffmpeg;

import com.google.inject.Inject;
import eu.goodlike.cmd.ProcessHookAttacher;
import eu.goodlike.neat.Null;
import org.apache.commons.io.FilenameUtils;

import java.util.concurrent.CompletableFuture;

/**
 * Processor which sets the output file to .mp4 format
 */
public final class Mp4FfmpegProcessor implements FfmpegProcessor {

    @Override
    public CompletableFuture<?> processFfmpeg(String output, String firstInput, String... inputs) {
        Null.check(output).as("output");

        Process process = ffmpegFormatter.runFfmpeg(setToMp4(output), firstInput, inputs)
                .orElseThrow(() -> new IllegalStateException("Failed to spawn ffmpeg process"));

        CompletableFuture<Process> processCompletionFuture = new CompletableFuture<>();
        processHookAttacher.attachAfter(process, processCompletionFuture::complete);
        return processCompletionFuture;
    }

    // CONSTRUCTORS

    @Inject Mp4FfmpegProcessor(FfmpegFormatter ffmpegFormatter, ProcessHookAttacher processHookAttacher) {
        this.ffmpegFormatter = ffmpegFormatter;
        this.processHookAttacher = processHookAttacher;
    }

    // PRIVATE

    private final FfmpegFormatter ffmpegFormatter;
    private final ProcessHookAttacher processHookAttacher;

    private String setToMp4(String filename) {
        return FilenameUtils.getBaseName(filename) + ".mp4";
    }

}
