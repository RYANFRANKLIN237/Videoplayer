# Video Player App

## Overview

This repository contains a robust and versatile video player app built with Kotlin, utilizing the bare essentials of the Media3 and ExoPlayer libraries. The player supports adaptive streaming and buffering, ensuring smooth playback even in varying network conditions. Additionally, the app allows for extensive customization of player controls, providing a flexible solution for diverse use cases.

## Features

- **Adaptive Streaming**: Seamless playback with automatic adjustment to network conditions, ensuring minimal interruptions.
- **Efficient Buffering**: Optimized buffering strategies to deliver a smooth viewing experience.
- **Customizable Controls**: Easily extend and modify player controls to fit specific requirements.
- **Lightweight Implementation**: Built using only the necessary components of Media3 and ExoPlayer, keeping the app lightweight and performant.

## Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/RYANFRANKLIN237/Videoplayer.git
    cd Videoplayer
    ```

2. **Open in Android Studio**: Import the project into Android Studio.

3. **Build and Run**: Build the project and run it on your preferred emulator or physical device.

## Usage

### Basic Playback

To start a basic video playback, initialize the player with a media source:

```kotlin
val player = ExoPlayer.Builder(context).build()
val mediaItem = MediaItem.fromUri("your_video_url")
player.setMediaItem(mediaItem)
player.prepare()
player.play()
```

## Custom controls

Customizing the player controls is straightforward. You can create your own UI components and link them to the player's control methods.

```kotlin
// Example: Custom play/pause button
val playPauseButton: Button = findViewById(R.id.play_pause_button)
playPauseButton.setOnClickListener {
    if (player.isPlaying) {
        player.pause()
    } else {
        player.play()
    }
}
```

## Adaptive streaming
The player automatically handles adaptive streaming. Ensure your media source supports adaptive bitrates.

```kotlin
val mediaItem = MediaItem.Builder()
    .setUri("your_adaptive_stream_url")
    .setMimeType(MimeTypes.APPLICATION_MPD) // Example for DASH
    .build()
player.setMediaItem(mediaItem)
```

## Dependecies
- Ensure you include the necessary dependencies in your 'build.gradle' file:
- Make sure to replace the versions with your preferred versions of the library.

```groovy
implementation "androidx.media3:media3-exoplayer:1.3.1"
implementation "androidx.media3:media3-ui:1.3.1"
implementation "androidx.media3:media3-exoplayer-dash:1.3.1"
```
## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests for improvements and bug fixes.

Feel free to reach out if you have any questions or need further assistance. Happy coding! 🎉

**Note:**  Replace "your_video_url" and "your_adaptive_stream_url" with actual URLs for testing. Ensure you handle permissions and network security configurations as required by your project specifications.
