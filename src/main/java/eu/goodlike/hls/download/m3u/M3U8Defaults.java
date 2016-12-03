package eu.goodlike.hls.download.m3u;

/**
 * Various constants pertaining to m3u8 format
 */
public final class M3U8Defaults {

    public static final String M3U8_TAG_START = "#";

    public static final String M3U8_FILE_START = "#EXTM3U";

    public static final String M3U8_MASTER_MEDIA_TAG = "#EXT-X-MEDIA:";
    public static final String M3U8_MASTER_MEDIA_GROUP_ID_ATTRIBUTE = "GROUP-ID=";
    public static final String M3U8_MASTER_MEDIA_NAME_ATTRIBUTE = "NAME=";
    public static final String M3U8_MASTER_MEDIA_URI_ATTRIBUTE = "URI=";

    public static final String M3U8_MASTER_STREAM_INFO_TAG = "#EXT-X-STREAM-INF:";
    public static final String M3U8_MASTER_STREAM_INFO_RESOLUTION_ATTRIBUTE = "RESOLUTION=";
    public static final String M3U8_MASTER_STREAM_INFO_AUDIO_ATTRIBUTE = "AUDIO=";

    public static final String M3U8_MEDIA_TARGET_DURATION_TAG = "#EXT-X-TARGETDURATION:";

    public static final String M3U8_MEDIA_PART_DURATION = "#EXTINF:";

    public static final String M3U8_MEDIA_END_OF_FILE = "#EXT-X-ENDLIST";

    // PRIVATE

    private M3U8Defaults() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }

}
