package eu.goodlike.hls.download.args;

import okhttp3.HttpUrl;

import java.util.List;
import java.util.Set;

/**
 * Defines how to extract urls from given arguments
 */
public interface ArgResolver {

    /**
     * Extracts urls from given arguments
     *
     * @param arguments list of urls or their containers
     * @return set of extracted urls, in the order they appear
     * @throws NullPointerException if arguments is null
     */
    Set<HttpUrl> getAllUrls(List<String> arguments);

}
