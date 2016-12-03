package eu.goodlike.hls.download.m3u.data.builder;

import eu.goodlike.hls.download.m3u.data.MasterPlaylistData;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import eu.goodlike.hls.download.m3u.data.PlaylistData;
import okhttp3.HttpUrl;

import java.math.BigDecimal;

/**
 * Builder for media playlist data objects
 */
public interface HlsBuilder<PlaylistType extends PlaylistData> {

    /**
     * Sets next playlist name (optional)
     *
     * @param nextPlaylistName name of next playlist
     * @return builder for master playlists
     * @throws NullPointerException if nextPlaylistName is null
     * @throws IllegalStateException if nextPlaylistName is blank
     * @throws IllegalStateException if playlist name is set multiple times before playlist url
     * @throws IllegalStateException if this builder is for {@link MediaPlaylistData}
     */
    HlsBuilder<MasterPlaylistData> setNextPlaylistName(String nextPlaylistName);

    /**
     * Sets next playlist resolution (optional)
     *
     * @param nextPlaylistResolution resolution of next playlist
     * @return builder for master playlists
     * @throws NullPointerException if nextPlaylistResolution is null
     * @throws IllegalStateException if nextPlaylistResolution is blank
     * @throws IllegalStateException if playlist resolution is set multiple times before playlist url
     * @throws IllegalStateException if this builder is for {@link MediaPlaylistData}
     */
    HlsBuilder<MasterPlaylistData> setNextPlaylistResolution(String nextPlaylistResolution);

    /**
     * Sets target duration (every media playlist must have exactly one)
     *
     * @param targetDuration target duration of this playlist
     * @return builder for media playlists
     * @throws NullPointerException if targetDuration is null
     * @throws IllegalStateException if targetDuration is negative or 0
     * @throws IllegalStateException if targetDuration has already been set
     * @throws IllegalStateException if this builder is for {@link MasterPlaylistData}
     */
    HlsBuilder<MediaPlaylistData> setTargetDuration(BigDecimal targetDuration);

    /**
     * Sets next part duration (every media playlist part must have exactly one)
     *
     * @param nextPartDuration duration of next part
     * @return builder for media playlists
     * @throws NullPointerException if nextPartDuration is null
     * @throws IllegalStateException if nextPartDuration is negative or 0
     * @throws IllegalStateException if part duration is set multiple times before part url/filename
     * @throws IllegalStateException if this builder is for {@link MasterPlaylistData}
     */
    HlsBuilder<MediaPlaylistData> setNextPartDuration(BigDecimal nextPartDuration);

    /**
     * Sets next url; in master playlists the url refers to a media playlist; in media playlists the url refers to a
     * stream part; either way, at least one of these must be set
     *
     * @param url next url
     * @return builder for media playlists
     * @throws NullPointerException if url is null
     * @throws IllegalStateException if this is a builder for {@link MediaPlaylistData} and no part duration is set
     * for this url
     */
    HlsBuilder<PlaylistType> setNextUrl(HttpUrl url);

    /**
     * Sets next raw string; equivalent to {@link HlsBuilder#setNextUrl(HttpUrl)}, where the url is the location of the
     * playlist resolved against this string
     *
     * @param string next string
     * @return builder for media playlists
     * @throws NullPointerException if string is null
     * @throws IllegalStateException if string is blank
     * @throws IllegalStateException if this is a builder for {@link MediaPlaylistData} and no part duration is set
     * for this filename
     */
    HlsBuilder<PlaylistType> setNextString(String string);

    /**
     * Sets next group id; this id is usually used to resolve audio only streams into video streams and is part of media
     * tag
     *
     * @param groupId id of playlist described by this media tag
     * @return builder for master playlists
     * @throws NullPointerException if groupId is null
     * @throws IllegalStateException if groupId is blank
     * @throws IllegalStateException if groupId is already set for next playlist
     * @throws IllegalStateException if this groupId was already set for a different playlist
     * @throws IllegalStateException if this is a builder for {@link MediaPlaylistData}
     */
    HlsBuilder<MasterPlaylistData> setNextGroupId(String groupId);

    /**
     * Sets audio stream id for next media playlist; this id references a group id from another playlist in the same
     * master playlist
     *
     * @param groupId id of playlist that contains the audio for the next stream
     * @return builder for master playlists
     * @throws NullPointerException if groupId is null
     * @throws IllegalStateException if groupId is blank
     * @throws IllegalStateException if audio stream is already set for next playlist
     * @throws IllegalStateException if this is a builder for {@link MediaPlaylistData}
     */
    HlsBuilder<MasterPlaylistData> setNextAudioStreamId(String groupId);

    /**
     * Builds the playlist with data from this builder
     *
     * @return playlist data
     * @throws IllegalStateException if this is a builder for {@link MasterPlaylistData} and a name or resolution was
     * set without an url
     * @throws IllegalStateException if this is a builder for {@link MasterPlaylistData} and no url/string was set
     * @throws IllegalStateException if this is a builder for {@link MasterPlaylistData} and an audio stream id was
     * set to a group id that was never set by another playlist
     * @throws IllegalStateException if this is a builder for {@link MediaPlaylistData} and no target duration was set
     * @throws IllegalStateException if this is a builder for {@link MediaPlaylistData} and a part duration tag was
     * set without a part url/filename
     * @throws IllegalStateException if this is a builder for {@link MediaPlaylistData} and no part url/string was set
     */
    PlaylistType build();

}
