package eu.goodlike.hls.download.m3u.parse;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import eu.goodlike.hls.download.http.HttpStreamer;
import eu.goodlike.str.Str;
import okhttp3.HttpUrl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static eu.goodlike.hls.download.m3u.M3U8Defaults.*;
import static eu.goodlike.validate.CommonValidators.NOT_BLANK;

/**
 * Gets an {@link InputStream} from the {@link HttpUrl}, then parses it using a scanner
 */
public final class StreamingHlsParser implements HlsParser {

    @Override
    public List<HlsTag> parse(HttpUrl url) {
        List<HlsTag> tags = new ArrayList<>();

        InputStream inputStream = httpStreamer.getStream(url);
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine())
                parseTag(scanner.nextLine()).ifPresent(tags::add);
        }

        return ImmutableList.copyOf(tags);
    }

    // CONSTRUCTORS

    @Inject StreamingHlsParser(HttpStreamer httpStreamer) {
        this.httpStreamer = httpStreamer;
    }

    // PRIVATE

    private final HttpStreamer httpStreamer;

    private Optional<HlsTag> parseTag(String tag) {
        return tag.startsWith(M3U8_TAG_START)
                ? parseSpecificTag(tag)
                : parseRaw(tag);
    }

    private Optional<HlsTag> parseSpecificTag(String tag) {
        return tag.startsWith(M3U8_MASTER_MEDIA_TAG)
                ? parseMediaTag(tag)
                : tag.startsWith(M3U8_MASTER_STREAM_INFO_TAG)
                ? parseStreamInfoTag(tag)
                : tag.startsWith(M3U8_MEDIA_TARGET_DURATION_TAG)
                ? parseTargetDurationTag(tag)
                : tag.startsWith(M3U8_MEDIA_PART_DURATION)
                ? parsePartDurationTag(tag)
                : Optional.empty();
    }

    private Optional<HlsTag> parseRaw(String tag) {
        return Optional.of(tag).filter(NOT_BLANK).map(RawString::new);
    }

    private Optional<HlsTag> parseMediaTag(String tag) {
        String groupId = extractValueOrNull(tag, M3U8_MASTER_MEDIA_GROUP_ID_ATTRIBUTE);
        String name = extractValueOrNull(tag, M3U8_MASTER_MEDIA_NAME_ATTRIBUTE);
        String uri = extractValueOrNull(tag, M3U8_MASTER_MEDIA_URI_ATTRIBUTE);
        return groupId == null && name == null && uri == null
                ? Optional.empty()
                : Optional.of(new MediaTag(groupId, name, uri));
    }

    private Optional<HlsTag> parseStreamInfoTag(String tag) {
        String resolution = extractValueOrNull(tag, M3U8_MASTER_STREAM_INFO_RESOLUTION_ATTRIBUTE);
        String audioStreamId = extractValueOrNull(tag, M3U8_MASTER_STREAM_INFO_AUDIO_ATTRIBUTE);
        return resolution == null && audioStreamId == null
                ? Optional.empty()
                : Optional.of(new StreamInfoTag(resolution, audioStreamId));
    }

    private Optional<HlsTag> parseTargetDurationTag(String tag) {
        String value = tag.substring(M3U8_MEDIA_TARGET_DURATION_TAG.length());
        return Optional.of(value).map(BigDecimal::new).map(TargetDurationTag::new);
    }

    private Optional<HlsTag> parsePartDurationTag(String tag) {
        int endIndex = tag.indexOf(',');
        if (endIndex < 0)
            endIndex = tag.length();

        String value = tag.substring(M3U8_MEDIA_PART_DURATION.length(), endIndex);
        return Optional.of(value).map(BigDecimal::new).map(StreamPartDurationTag::new);
    }

    private Optional<String> extractValue(String tag, String attribute) {
        int nameIndex = tag.indexOf(attribute);
        if (nameIndex < 0)
            return Optional.empty();

        int startIndex = nameIndex + attribute.length();
        if (startIndex > tag.length())
            throw new IllegalStateException(Str.format("Missing value for attribute '{}' from tag: {}", attribute, tag));

        boolean quotedValue = tag.charAt(startIndex) == '"';
        if (quotedValue)
            startIndex++;

        int endIndex = quotedValue
                ? tag.indexOf('"', startIndex)
                : tag.indexOf(',', startIndex);

        if (endIndex < 0) {
            if (quotedValue)
                throw new IllegalStateException(Str.format("String quotes not closed in attribute '{}' from tag: {}", attribute, tag));

            endIndex = tag.length();
        }

        return Optional.of(tag.substring(startIndex, endIndex));
    }

    private String extractValueOrNull(String tag, String attribute) {
        return extractValue(tag, attribute).orElse(null);
    }

}
