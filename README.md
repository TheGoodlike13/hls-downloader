# Simple HLS/.m3u/.m3u8 downloader

Downloads streamed videos by using ffmpeg

Likely doesn't support everything, but should work for most simple cases

## How to?

1. Install Java 8u112 or later: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
2. Install ffmpeg: https://ffmpeg.org/
3. Put this code somewhere
4. Run "gradlew shadowJar" in that folder
5. Take all the files from "copy" folder and run "hlsDL {insert_url_of_playlist_here}"

## Future

While I am only ~90% happy with the design, since I did learn from it, I'm gonna call it a day and
move to the next project. It was not too difficult to add separate audio/video streams, for example.
If you're interested in what I'd do differently, feel free to ask (although I cannot guarantee that
it would be better :D)

If I encounter a bug I'll try to fix it, though. I'll use this myself from time to time, after all.
