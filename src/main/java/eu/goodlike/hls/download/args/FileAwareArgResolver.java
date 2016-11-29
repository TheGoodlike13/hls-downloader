package eu.goodlike.hls.download.args;

import com.google.common.collect.ImmutableSet;
import eu.goodlike.io.FileUtils;
import eu.goodlike.neat.Null;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Extracts urls from given arguments, either by parsing as an url, or, if that fails, by parsing it as file containing
 * more args
 */
public final class FileAwareArgResolver implements ArgResolver {

    @Override
    public Set<HttpUrl> getAllUrls(List<String> arguments) {
        Null.checkList(arguments).as("arguments");
        return ImmutableSet.copyOf(getAllUrls(arguments, new ArrayList<>(), new HashSet<>()));
    }

    // PRIVATE

    private List<HttpUrl> getAllUrls(List<String> arguments, List<HttpUrl> urls, Set<String> visitedFiles) {
        for (String argument : arguments) {
            HttpUrl url = HttpUrl.parse(argument);
            if (url == null)
                Optional.of(argument)
                        .filter(file -> !visitedFiles.contains(file))
                        .flatMap(this::extractValuesFromFile)
                        .ifPresent(args -> getAllUrls(args, urls, add(visitedFiles, argument)));
            else
                urls.add(url);
        }

        return urls;
    }

    private Optional<List<String>> extractValuesFromFile(String filename) {
        return FileUtils.getPath(filename)
                .filter(Files::exists)
                .map(this::readAllLines);
    }

    private List<String> readAllLines(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            return null;
        }
    }

    private Set<String> add(Set<String> visitedFiles, String filename) {
        visitedFiles.add(filename);
        return visitedFiles;
    }

}
