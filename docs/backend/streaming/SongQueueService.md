# SongQueueService Documentation 🎵

The `SongQueueService.java` file is an essential component of a backend system designed for managing a song queue. This service is responsible for adding songs to the queue, playing, skipping, changing the order of songs, and managing volume and vote-skip functionalities among others.

## Table of Contents
1. [Overview](#overview)
2. [Methods](#methods)
3. [Auxiliary Features](#auxiliary-features)

## Overview

**Package:** `com.example.backend.streaming`

**Dependencies:**
- `SongService`
- `SongInfoRepository`
- `UserService`

**Key Fields:**
- `List<Song> songQueue`: The list of songs in the queue.
- `Song currentSong`: The currently playing song.
- `boolean isPlaying`: A flag to indicate if a song is currently playing.
- `int voteSkipRequired`: The required number of votes to skip the current song.
- `Set<Long> voteSkipUserList`: A set of user IDs that have voted to skip the current song.
- `int volume`: The current volume level.
- `boolean isMuted`: A flag to indicate if the playback is muted.

## Methods

| Method | Description |
| ------ | ----------- |
| `addSong(String link)` | Adds a song to the queue and starts playing if no song is currently playing. |
| `skip()` | Skips the currently playing song and starts the next song in the queue. |
| `changeOrder(int oldPos, int newPos)` | Changes the order of a song in the queue. |
| `getQueue()` | Returns a list of all songs currently in the queue. |
| `getQueue(int pageNumber)` | Returns a paginated list of songs in the queue. |
| `getQueue(int pageNumber, int pageSize)` | Returns a paginated list of songs in the queue with a custom page size. |
| `play()` | Starts playback of the current song. |
| `stop()` | Stops playback of the current song. |
| `getTotalPages()` | Returns the total number of pages of songs in the queue. |
| `getTotalPages(int pageSize)` | Returns the total number of pages of songs in the queue with a custom page size. |
| `getDefaultPageSize()` | Returns the default page size for pagination. |
| `isPlaying()` | Returns the current playback state. |
| `getCurrentPlayingSong()` | Returns details of the currently playing song. |
| `getVoteSkipStatus(Long userId)` | Returns the current vote skip status for a user. |
| `setVoteSkip(String userToken)` | Registers a vote to skip the current song. |
| `withdrawVoteSkip(String userToken)` | Withdraws a previously cast vote to skip. |
| `searchSongHistory(String searchTerm, int maxResults)` | Searches the song history based on a search term. |
| `replaySong()` | Restarts playback of the current song. |
| `changeVolume(int volume)` | Changes the playback volume. |
| `getVolume()` | Returns the current state of playback volume. |
| `mute()` | Mutes playback. |
| `unmute()` | Unmutes playback. |
| `getVoteSkipRequired()` | Calculates and returns the number of votes required to skip a song. |
| `loadPreSetSongs(String fileName)` | Loads predefined songs into the queue from a file. |
| `removeSong(int pos, String title)` | Removes a song from the queue. |

## Auxiliary Features

- **Pagination**: This service supports retrieving the song queue in a paginated manner, which is essential for handling large queues efficiently.
- **Vote Skipping**: Users can vote to skip the currently playing song. A configurable number of votes is required to skip a song.
- **Volume Control**: The service provides methods for adjusting the playback volume, including muting and unmuting functionalities.

---

This service acts as the backbone for any application requiring a comprehensive system for managing a music queue, ensuring a seamless and interactive music experience for users.