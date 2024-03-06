# SoundcloudSongService Documentation 🎵

`SoundcloudSongService.java` is part of a Java backend application designed to handle operations related to playing, downloading, and managing songs from SoundCloud. This service implements the `SongServiceInterface`, ensuring the class adheres to a defined contract for song operations.

## Table of Contents

- [Service Overview](#service-overview)
- [Methods](#methods)
- [Dependencies](#dependencies)
- [How to Use](#how-to-use)

## Service Overview

This service is dedicated to performing various actions such as validating song links, playing songs, stopping playback, and managing audio file downloads from SoundCloud as a source. It utilizes the `YtDlpService` for downloading dependencies and fetching song information, and it interacts with a `SongQueueService` for song queue management.

## Methods

The following table outlines the key methods provided by the `SoundcloudSongService`:

| Method | Description |
| ------ | ----------- |
| `validateSong(String link)` | Validates a SoundCloud song link and fetches its information. |
| `play(Song input)` | Starts or resumes the playback of a song. |
| `stop(Song input)` | Stops the playback of a given song. |
| `close(Song input)` | Closes the audio stream of a given song. |
| `downloadDependencies(Song input)` | Downloads the audio file for a song. |
| `fetchInfos(Song song)` | Fetches or updates the song information. |
| `getInfos(Song input)` | Retrieves song information. |
| `replay(Song input)` | Restarts the playback of a given song. |
| `changeVolume(Song input, int volume)` | Changes the playback volume. |
| `getVolume(Song input)` | Retrieves the current playback volume. |

## Dependencies

`SoundcloudSongService` relies on the following components:

- **YtDlpService**: For downloading songs and fetching song metadata.
- **SongQueueService**: For managing the song queue and playback settings (e.g., volume).
- **SongInfoRepository**: For storing and retrieving song information history.
- **ConvertSongTitle**: For converting song titles into a standardized file name format.
- **Logging (SLF4J)**: For logging errors and important information.

## How to Use

To utilize this service in your project:

1. **Validate a Song**:  
   Before playing, ensure the song link is valid using `validateSong(String link)`.
   ```java
   Song validSong = soundcloudSongService.validateSong("https://soundcloud.com/...");
   ```

2. **Play a Song**:  
   To play a song, simply pass the validated `Song` object to `play(Song input)`.
   ```java
   soundcloudSongService.play(validSong);
   ```

3. **Manage Playback**:  
   Control the playback using `stop(Song input)`, `replay(Song input)`, and `changeVolume(Song input, int volume)`.
   ```java
   // Stop the song
   soundcloudSongService.stop(validSong);
   // Replay the song
   soundcloudSongService.replay(validSong);
   // Change volume to 50%
   soundcloudSongService.changeVolume(validSong, 50);
   ```

4. **Song Downloading**:  
   The service automatically manages downloading dependencies when a song is played for the first time. However, you can manually trigger downloading using `downloadDependencies(Song input)`.

---

This service abstracts the complexities involved in handling SoundCloud songs, providing a straightforward interface for playing, downloading, and managing songs within a Java application.