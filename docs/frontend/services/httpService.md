# HttpService Documentation 📄

The `HttpService` class provides a comprehensive set of methods for interacting with the backend API, facilitating operations related to songs in the queue, user authentication, role and permission management, system settings, and more. This document outlines the functionalities provided by the `HttpService` class, categorized by their primary purpose.

## Table of Contents

- [Queue Management](#queue-management)
- [User Authentication](#user-authentication)
- [User and Role Management](#user-and-role-management)
- [System Settings](#system-settings)
- [Miscellaneous](#miscellaneous)

## Queue Management

| Method | Description |
| ------ | ----------- |
| `getQueuePage(pageNumber, pageSize)` | Retrieves a page of songs in the queue. |
| `getQueueAll()` | Fetches all songs currently in the queue. |
| `getNowPlaying()` | Gets information about the currently playing song. |
| `postQueueAdd(link, apiKey)` | Adds a new song to the queue. |
| `postSkip(apiKey)` | Skips the currently playing song. |
| `postReplay(apiKey)` | Replays the currently playing song. |
| `postStart(apiKey)` | Starts playing songs from the queue. |
| `postStop(apiKey)` | Stops playing songs from the queue. |
| `postVolume(volume)` | Sets the volume of the queue. |
| `getVolume()` | Retrieves the current volume of the queue. |
| `postMute()` | Mutes the queue. |
| `postUnmute()` | Unmutes the queue. |
| `deleteSongs(selectedSongs)` | Deletes selected songs from the queue. |

## User Authentication

| Method | Description |
| ------ | ----------- |
| `postPrivateAuthLogin(username, password, entryCode)` | Logs in with private authentication method. |
| `postPublicAuthLogin(username, password)` | Logs in with public authentication method. |
| `postPrivateLogin(username, entryCode)` | Performs a private login operation. |
| `postPublicLogin(username)` | Performs a public login operation. |
| `postLogout(apiKey)` | Logs out the user associated with the provided API key. |
| `getVerifyApiKey(apiKey)` | Verifies the provided API key. |

## User and Role Management

| Method | Description |
| ------ | ----------- |
| `getUsers()` | Retrieves a list of users. |
| `getSelf()` | Fetches information about the current user. |
| `deleteUser(userID)` | Deletes a user with the specified ID. |
| `getRoles()` | Retrieves a list of roles. |
| `getPermissions()` | Fetches a list of permissions. |
| `getOwnPermissions()` | Retrieves the permissions of the current user. |
| `getRole(id)` | Gets information about the role with the specified ID. |
| `deleteRole(id)` | Deletes the role with the specified ID. |
| `updateRole(role)` | Updates the role information. |
| `createRole(role)` | Creates a new role. |

## System Settings

| Method | Description |
| ------ | ----------- |
| `getLanguage()` | Fetches the current language settings. |
| `setLanguage(language)` | Sets the language settings. |
| `getPrivacy()` | Retrieves the current privacy settings. |
| `setPrivacy(privacySettings)` | Sets the privacy settings. |
| `getAuthentication()` | Fetches the current authentication settings. |
| `setAuthentication(requireEmailAuthentication)` | Sets the authentication settings. |
| `getSupportedSources()` | Retrieves a list of supported sources. |
| `getSources()` | Fetches the current sources configuration. |
| `setSources(sources)` | Sets the sources configuration. |
| `getApplicationSettings()` | Retrieves the application settings. |
| `patchSetApplicationSettings(applicationSettings)` | Sets the application settings. |
| `getInstallationState()` | Fetches the installation state. |
| `setInstallationStateComplete()` | Sets the installation state to complete. |

## Miscellaneous

| Method | Description |
| ------ | ----------- |
| `getSearchHistory(searchTerm, maxResults, apiKey)` | Retrieves the search history for the specified term. |
| `postRegisterCreateAccount(username, email, password)` | Creates a new user account. |
| `postRegisterVerify(code, email)` | Verifies the registration code. |
| `postResendMail(email)` | Resends the verification email. |

This documentation provides a solid foundation for understanding the capabilities of the `HttpService` class and how it interacts with the backend API to perform a wide array of operations.