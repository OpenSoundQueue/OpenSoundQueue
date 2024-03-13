# SoundQueueRest Documentation 🎵

The `SoundQueueRest` class is a crucial component of a backend service, designed to manage and control the queue of songs for a music streaming application. This class exposes various REST API endpoints to perform operations such as adding songs to the queue, playing the next song, handling volume control, and more. Below is a detailed breakdown of its functionalities and how they're used.

## Table of Contents
- [Endpoints](#endpoints)
  - [Retrieve Song Queue](#retrieve-song-queue)
  - [Paged Song Retrieval](#paged-song-retrieval)
  - [Now Playing](#now-playing)
  - [Queue Operations](#queue-operations)
  - [Volume Control](#volume-control)
  - [Song History and Management](#song-history-and-management)
- [Setup and Initialization](#setup-and-initialization)

## Endpoints

### Retrieve Song Queue
- **Get all songs in the queue**
  ```http
  GET /api/queue/all
  ```
- **Get songs in queue paged**
  ```http
  GET /api/queue/page/{page-number}
  ```
- **Get songs in queue with custom page size**
  ```http
  GET /api/queue/page/{page-number}/page-size/{page-size}
  ```

### Paged Song Retrieval
Allows users to fetch songs from the queue in a paginated manner, either with a default or a custom page size.

### Now Playing
- **Get currently playing song**
  ```http
  GET /api/queue/now-playing
  ```

### Queue Operations
- **Add song to the queue**
  ```http
  POST /api/queue/add
  ```
- **Skip currently playing song**
  ```http
  POST /api/queue/skip
  ```
- **Vote to skip the current song**
  ```http
  GET /api/vote-skip/vote
  ```
- **Withdraw vote-skip**
  ```http
  GET /api/vote-skip/withdraw
  ```
- **Change order of songs in the queue**
  ```http
  PATCH /api/queue/change-order
  ```
- **Start/Stop playback**
  ```http
  POST /api/queue/start
  POST /api/queue/stop
  ```
- **Replay currently playing song**
  ```http
  POST /api/queue/replay
  ```
- **Delete a song from the queue**
  ```http
  DELETE /api/queue/delete
  ```

### Volume Control
- **Change the volume**
  ```http
  POST /api/queue/volume/{volume}
  ```
- **Get current volume**
  ```http
  GET /api/queue/current-volume
  ```
- **Mute/Unmute playback**
  ```http
  POST /api/queue/mute
  POST /api/queue/unmute
  ```

### Song History and Management
- **Search songs in history**
  ```http
  GET /api/search/history/{search-term}/max-results/{max-results}
  ```

## Setup and Initialization
The `SoundQueueRest` class is initialized with two main services:
- `SongQueueService`: Manages the queue of songs, including adding, skipping, and retrieving songs.
- `UserService`: Manages user information and interactions, such as updating the last online time based on actions performed.

```java
public SoundQueueRest(SongQueueService songQueueService, UserService userService) {
    this.songQueueService = songQueueService;
    this.userService = userService;
}
```

In summary, the `SoundQueueRest` class offers a comprehensive suite of RESTful APIs tailored for managing a dynamic and interactive song queue, enhancing the user experience in music streaming applications. Its design promotes easy navigation, control, and enjoyment of music by providing functionalities like song queue management, playback control, volume adjustment, and historical song search.