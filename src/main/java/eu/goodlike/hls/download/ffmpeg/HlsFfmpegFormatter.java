package eu.goodlike.hls.download.ffmpeg;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import java.util.List;
import java.util.Optional;

/**
 * Uses ffmpeg to download a .m3u file
 */
public final class HlsFfmpegFormatter implements FfmpegFormatter {

    @Override
    public Optional<Process> runFfmpeg(String inputFileName, String outputFileName) {
        return ffmpegRunner.runFfmpeg(getFullArgList(inputFileName, outputFileName));
    }

    // CONSTRUCTORS

    @Inject HlsFfmpegFormatter(FfmpegRunner ffmpegRunner) {
        this.ffmpegRunner = ffmpegRunner;
    }

    // PRIVATE

    private final FfmpegRunner ffmpegRunner;

    private List<String> getFullArgList(String inputFileName, String outputFileName) {
        return ImmutableList.<String>builder()
                .add(INPUT_ARG).add(inputFileName)
                .addAll(DEFAULT_ARGS)
                .add(outputFileName)
                .build();
    }

    private static final String INPUT_ARG = "-i";
    private static final List<String> DEFAULT_ARGS = ImmutableList.of("-bsf:a", "aac_adtstoasc", "-c", "copy");

}
