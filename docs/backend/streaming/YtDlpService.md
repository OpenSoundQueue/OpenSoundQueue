# YtDlpService Java Documentation 🎵🔍

`YtDlpService.java` is a vital part of a backend application that integrates with `yt-dlp`, a command-line program to download videos from YouTube and other video platforms. This service is designed to perform various operations such as fetching song information and downloading audio files, specifically tailored for a music streaming application. Below is a detailed breakdown of its functionalities, structure, and usage.

## Table of Contents

- [Overview](#overview)
- [Class Summary](#class-summary)
- [Methods](#methods)
- [Usage Example](#usage-example)

## Overview

**Package:** `com.example.backend.streaming.ytdlp`

**Dependencies:**
- Spring Framework (`@Service`, `@Autowired`)
- SLF4J for logging
- Jackson for JSON parsing
- Java HttpClient for making HTTP requests

**Primary Functions:**
- Retrieve detailed information about a song from its URL.
- Verify artist names against a database.
- Download the audio file of a song in `.wav` format.

## Class Summary

| **Element** | **Description** |
|-------------|-----------------|
| `YtDlpService` | Main class providing services to interact with `yt-dlp`. |

### Attributes

- `LOG`: Logger instance for logging.
- `songInfoRepository`: Repository for accessing song information.
- `convertSongTitle`: Utility to convert song titles into a file-friendly format.

### Methods

#### `getInfos(Song song)`

Retrieves and parses information about a given song from its link.

- **Parameters:**
  - `song`: The song entity containing the link.
- **Returns:** `SongInfo` object with artist, duration, and title.

#### `checkDatabaseForArtist(String artist)`

Checks if an artist exists in a database by sending a HTTP request.

- **Parameters:**
  - `artist`: Name of the artist.
- **Returns:** `boolean` indicating if the artist was found.

#### `downloadSong(Song song, String downloadPath, String url)`

Downloads the audio file of a song in `.wav` format.

- **Parameters:**
  - `song`: The song to download.
  - `downloadPath`: Path where the audio file will be saved.
  - `url`: Link of the song.

## Usage Example

### Fetching Song Information

```java
Song mySong = new Song();
mySong.setLink("https://youtube.com/watch?...");
YtDlpService service = new YtDlpService();
SongInfo songInfo = service.getInfos(mySong);
System.out.println("Artist: " + songInfo.getArtist() + ", Duration: " + songInfo.getDuration() + ", Title: " + songInfo.getTitle());
```

### Verifying Artist Name

```java
String artistName = "John Doe";
YtDlpService service = new YtDlpService();
boolean exists = service.checkDatabaseForArtist(artistName);
if(exists) {
    System.out.println(artistName + " exists in the database.");
}
```

### Downloading Song Audio

```java
Song mySong = new Song();
mySong.setLink("https://youtube.com/watch?...");
mySong.setArtist("John Doe");
mySong.setTitle("My First Song");
YtDlpService service = new YtDlpService();
service.downloadSong(mySong, "/path/to/save/", mySong.getLink());
```

## Conclusion

`YtDlpService.java` is a comprehensive backend service leveraging `yt-dlp` for extracting and downloading song data, proving essential for music streaming applications. It showcases the effective use of external processes, JSON parsing, and HTTP requests within a Spring Boot application context.