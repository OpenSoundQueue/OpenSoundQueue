# YoutubeSongService Documentation 🎵🎥

The `YoutubeSongService` class, part of a backend service, is meticulously designed to manage and execute all actions related to songs sourced from YouTube. This service is a cornerstone for applications that require fetching, processing, and playing songs from YouTube, ensuring a seamless musical experience.

## Overview

| Aspect             | Detail                                           |
|--------------------|--------------------------------------------------|
| Package            | `com.example.backend.streaming.youtube`          |
| Inheritance        | Implements `SongServiceInterface`                |
| Dependencies       | `javax.sound.sampled.*`, `java.io.*`, `org.slf4j`, `org.springframework` |

## Core Functionalities

### Song Validation
```java
public Song validateSong(String link)
```
Validates the provided YouTube link for song correctness. It fetches song information, returning a `Song` object if the link is valid; otherwise, it returns `null`.

### Playback Control
- **Play**: Initiates the playback of a song.
    ```java
    public void play(Song input)
    ```
- **Stop**: Stops the playback of a given song.
    ```java
    public void stop(Song input)
    ```
- **Close**: Closes the audio stream of a given song, releasing resources.
    ```java
    public void close(Song input)
    ```
- **Replay**: Restarts the playback of a given song from the beginning.
    ```java
    public void replay(Song input)
    ```
- **Change Volume**: Adjusts the playback volume of a given song.
    ```java
    public void changeVolume(Song input, int volume)
    ```
- **Get Volume**: Retrieves the current playback volume of a given song.
    ```java
    public int getVolume(Song input)
    ```

### Song Information Management
- **Download Dependencies**: Downloads the audio file of a song from YouTube.
    ```java
    public void downloadDependencies(Song input)
    ```
- **Fetch Infos**: Retrieves or updates the information of a given song using `yt-dlp`.
    ```java
    public void fetchInfos(Song song)
    ```
- **Get Infos**: Gets the song information such as artist, duration, and title.
    ```java
    public SongInfo getInfos(Song input)
    ```

## Dependency Injection

The class utilizes Spring's `@Autowired` annotation to inject dependencies required for its operations:

- `SongQueueService`
- `SongInfoRepository`
- `YtDlpService`
- `ConvertSongTitle`

## Exception Handling

Throughout its operations, the service meticulously handles various exceptions related to audio processing, file I/O, and thread interruptions, ensuring robustness and stability.

## Logging

Utilizes SLF4J for logging errors, especially during the file not found scenarios or any issues in processing the songs.

## Usage Scenarios

- **Music Streaming Apps**: Can be used in applications that stream music directly from YouTube, managing song queues and playback.
- **Content Management Systems**: Suitable for CMS that requires background music or sound effects sourced from YouTube.
- **Gaming Applications**: For integrating YouTube-sourced background tracks or sound effects.

## Conclusion

`YoutubeSongService` is a comprehensive service handling the complexities of processing and managing songs from YouTube, providing an essential toolkit for developers aiming to incorporate music functionalities into their applications.