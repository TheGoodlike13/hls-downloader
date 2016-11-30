# Simple HLS/.m3u/.m3u8 downloader

Downloads streamed videos by using ffmpeg

Likely doesn't support everything, but should work for most simple cases

## How to?

0. Install Java 8u112 or later: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
1. Install ffmpeg: https://ffmpeg.org/
2. Put this code somewhere
3. Run "gradlew shadowJar" in that folder
4. Take all the files from "copy" folder and run "hlsDL {insert_url_of_playlist_here}"
