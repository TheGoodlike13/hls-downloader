package eu.goodlike.hls.download.ffmpeg;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import eu.goodlike.neat.Null;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Uses ffmpeg to download a .m3u file
 */
public final class HlsFfmpegFormatter implements FfmpegFormatter {

    @Override
    public Optional<Process> runFfmpeg(String outputFileName, String firstInputFileName, String... inputFileNames) {
        Null.check(outputFileName).as("outputFileName");
        Null.check(firstInputFileName).as("firstInputFileName");
        Null.checkArray(inputFileNames).as("inputFileNames");
        return ffmpegRunner.runFfmpeg(getFullArgList(outputFileName, firstInputFileName, inputFileNames));
    }

    // CONSTRUCTORS

    @Inject HlsFfmpegFormatter(FfmpegRunner ffmpegRunner) {
        this.ffmpegRunner = ffmpegRunner;
    }

    // PRIVATE

    private final FfmpegRunner ffmpegRunner;

    private List<String> getFullArgList(String outputFileName, String firstInputFileName, String... inputFileNames) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        builder.addAll(PROTOCOLS_USED_BY_HLS);
        builder.add(INPUT_ARG).add(firstInputFileName);
        Stream.of(inputFileNames).forEach(input -> builder.add(INPUT_ARG).add(input));
        return builder
                .addAll(DEFAULT_ARGS)
                .add(outputFileName)
                .build();
    }

    private static final List<String> PROTOCOLS_USED_BY_HLS = ImmutableList.of("-protocol_whitelist", "file,tcp,http,https,tls");
    private static final String INPUT_ARG = "-i";
    private static final List<String> DEFAULT_ARGS = ImmutableList.of("-bsf:a", "aac_adtstoasc", "-c", "copy");

}
