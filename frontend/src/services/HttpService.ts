import HttpClient from "@/services/HttpClient";
import {Song} from "@/models/Song";
import {User} from "@/models/User";
import * as cookieService from "@/services/cookieService";
import type {Role} from "@/models/Role";
import {setLanguage} from "@/plugins/TranslationPlugin";
import type {ApplicationSettings} from "@/models/ApplicationSettings";

// Instantiates an HttpClient object for making HTTP requests.
const httpClient = new HttpClient();

// Provides methods for interacting with the backend API.
export class HttpService {
    // Retrieves a page of songs in the queue based on the provided page number and page size.
    async getQueuePage(pageNumber: number, pageSize: number) {
        // Defines the type of the queue page response.
        type QueuePage = {
            page: [{
                numberInQueue: number,
                song: Song
            }],
            numberOfPages: number
        }

        // Sends a GET request to the server to fetch the specified page of the queue.
        return httpClient.get(`/queue/page/${pageNumber}/page-size/${pageSize}`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: QueuePage) => {
                return data;
            });
    }

    // Retrieves all songs in the queue.
    async getQueueAll() {
        // Defines the type of the queue response.
        type Queue = Array<{ numberInQueue: number, isSelected?: boolean,song: Song }>;

        // Sends a GET request to the server to fetch all songs in the queue.
        return httpClient.get(`/queue/all`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: Queue) => {
                return data;
            });
    }

    // Retrieves information about the currently playing song.
    async getNowPlaying() {
        // Sends a GET request to the server to fetch information about the currently playing song.
        return httpClient.get("/queue/now-playing")
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            })
    }

    // Adds a song to the queue.
    async postQueueAdd(link: string, apiKey: string) {
        // Sends a POST request to the server to add a song to the queue.
        return httpClient.post("/queue/add", {link: link}, apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            })
    }

    // Retrieves the vote skip status.
    async getVoteSkipStatus(apiKey: string) {
        // Sends a GET request to the server to retrieve the vote skip status.
        return httpClient.get("/vote-skip/status", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            })
    }

    // Retrieves the user's vote skip vote.
    async getVoteSkipVote(apiKey: string) {
        // Sends a GET request to the server to retrieve the user's vote skip vote.
        return httpClient.get("/vote-skip/vote", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            })
    }

    // Withdraws the user's vote skip vote.
    async getVoteSkipWithdraw(apiKey: string) {
        // Sends a GET request to the server to withdraw the user's vote skip vote.
        return httpClient.get("/vote-skip/withdraw", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            })
    }

    // Skips the currently playing song.
    async postSkip(apiKey: string) {
        // Sends a POST request to the server to skip the currently playing song.
        return httpClient.post("/queue/skip", "", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response;
            })
    }

    // Replays the currently playing song.
    async postReplay(apiKey: string) {
        // Sends a POST request to the server to replay the currently playing song.
        return httpClient.post("/queue/replay", "", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response;
            })
    }

    // Starts playing songs in the queue.
    async postStart(apiKey: string) {
        // Sends a POST request to the server to start playing songs in the queue.
        return httpClient.post("/queue/start", "", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response;
            })
    }

    // Stops playing songs in the queue.
    async postStop(apiKey: string) {
        // Sends a POST request to the server to stop playing songs in the queue.
        return httpClient.post("/queue/stop", "", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response;
            })
    }

    // Retrieves the search history for the specified search term.
    async getSearchHistory(searchTerm: string, maxResults: number, apiKey: string) {
        // Sends a GET request to the server to retrieve the search history.
        return httpClient.get(`/search/history/${searchTerm}/max-results/${maxResults}`, apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data: Song[]) => {
                return data;
            });
    }

    // Logs in with a private authentication method.
    async postPrivateAuthLogin(username: string, password: string, entryCode: string) {
        // Sends a POST request to the server to log in with private authentication.
        return await httpClient.post(`/login/private/auth`, {
            username: username,
            password: password,
            entryCode: entryCode
        })
            .then(async response => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data: Record<string, string>) => {
                return data.apiKey;
            });
    }

    // Logs in with a public authentication method.
    async postPublicAuthLogin(username: string, password: string) {
        // Sends a POST request to the server to log in with public authentication.
        return await httpClient.post(`/login/public/auth`, {
            username: username,
            password: password
        })
            .then(async response => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data: Record<string, string>) => {
                return data.apiKey;
            });
    }

    async postPrivateLogin(username: string, entryCode: string) {
        // Sends a POST request to the server to log in with private authentication.
        return await httpClient.post(`/login/private`, {
            username: username,
            entryCode: entryCode
        })
            .then(async response => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data: Record<string, string>) => {
                return data.apiKey;
            });
    }

    // Performs a public login operation with the provided username.
    async postPublicLogin(username: string) {
        return await httpClient.post(`/login/public`, {
            username: username
        })
            .then(async response => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data: Record<string, string>) => {
                return data.apiKey;
            });
    }

    // Creates a new user account with the provided username, email, and password.
    async postRegisterCreateAccount(username: string, email: string, password: string) {
        return httpClient.post(`/register/create-account`, {username: username, email: email, password: password})
            .then(async response => {
                if (!response.ok) {
                    const data = await response.json()
                    return Promise.reject(data);
                }
                return response;
            });
    }

    // Verifies the registration code associated with the provided email address.
    async postRegisterVerify(code: string, email: string) {
        return httpClient.post(`/register/verify`, {code: code, email: email})
            .then(response => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            })
            .then((apiKey) => {
                return apiKey.apiKey;
            });
    }

    // Resends the verification email to the specified email address.
    async postResendMail(email: string) {
        return httpClient.post(`/register/resend-email`, {email: email})
            .then(response => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
            });
    }

    // Logs out the user associated with the provided API key.
    async postLogout(apiKey: string) {
        return await httpClient.post(`/logout`, undefined, apiKey)
            .then(response => {
                cookieService.clearApiKey();

                if (!response.ok) {
                    return Promise.reject(response.status);
                }
            });
    }

    // Verifies the provided API key.
    async getVerifyApiKey(apiKey: string) {
        return await httpClient.get(`/verify/api-key`, apiKey)
            .then(response => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
            });
    }

    // Retrieves a list of users.
    async getUsers() {
        return httpClient.get(`/users`, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: User[]) => {
                return data;
            });
    }

    // Retrieves information about the current user.
    async getSelf() {
        return httpClient.get(`/self`, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: User) => {
                return data;
            });
    }

    // Deletes a user with the specified ID.
    async deleteUser(userID: number) {
        return httpClient.delete(`/user/` + userID, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: User[]) => {
                return data;
            });
    }

    // Changes the order of songs in the queue.
    async queueChangeOrder(oldPos: number, newPos: number) {
        return httpClient.patch(`/queue/change-order`,
            {oldPos: oldPos, newPos: newPos},
            cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Retrieves a list of roles.
    async getRoles() {
        return httpClient.get(`/roles`, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: Role[]) => {
                return data;
            });
    }

    // Retrieves a list of permissions.
    async getPermissions() {
        return httpClient.get(`/permissions`, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: string[]) => {
                return data;
            });
    }

    // Retrieves the permissions of the current user.
    async getOwnPermissions() {
        return httpClient.get(`/user/permissions`, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: string[]) => {
                return data;
            });
    }

    // Retrieves information about the role with the specified ID.
    async getRole(id: number) {
        return httpClient.get(`/role/get/${id}`, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: Role) => {
                return data;
            });
    }

    // Deletes the role with the specified ID.
    async deleteRole(id: number) {
        return httpClient.delete(`/role/${id}`, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
            }).then((data) => {
                return data;
            });
    }

    // Updates the role information.
    async updateRole(role: object) {
        return httpClient.patch(`/role/edit`, role, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data: Role) => {
                return data;
            });
    }

    // Creates a new role.
    async createRole(role: object) {
        return httpClient.post(`/role/create`, role, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data: Role) => {
                return data;
            });
    }

    // Retrieves the current language settings.
    async getLanguage() {
        return httpClient.get(`/system/language`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Sets the language settings.
    async setLanguage(language: string) {
        return httpClient.patch(`/system/language/set/${language}`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                setLanguage(language);
                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Retrieves the current privacy settings.
    async getPrivacy() {
        return httpClient.get(`/system/privacy`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data) => {
                return data;
            });
    }


    async getLoginState() {
        return httpClient.get(`/system/loginstate`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Sets the privacy settings.
    async setPrivacy(privacySettings: object) {
        return httpClient.patch(`/system/privacy/set`, privacySettings)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Retrieves the current authentication settings.
    async getAuthentication() {
        return httpClient.get(`/system/email-auth`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Sets the authentication settings.
    async setAuthentication(requireEMailAuthentication: boolean) {
        return httpClient.patch(`/system/email-auth/set/${requireEMailAuthentication}`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Retrieves a list of supported sources.
    async getSupportedSources() {
        return httpClient.get(`/system/supported-sources`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Retrieves the current sources configuration.
    async getSources() {
        return httpClient.get(`/system/sources`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Sets the sources configuration.
    async setSources(sources: string[]) {
        return httpClient.patch(`/system/sources/set`, {sources: sources})
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Retrieves the installation state.
    async getInstallationState() {
        return httpClient.get(`/system/installation-state`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Sets the installation state to complete.
    async setInstallationStateComplete() {
        return httpClient.patch(`/system/installation-state/complete`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Sets the volume of the queue.
    async postVolume(volume: number) {
        return httpClient.post(`/queue/volume/${volume}`, undefined, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Retrieves the current volume of the queue.
    async getVolume() {
        return httpClient.get(`/queue/current-volume`, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Mutes the queue.
    async postMute() {
        return httpClient.post(`/queue/mute`, undefined, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Unmutes the queue.
    async postUnmute() {
        return httpClient.post(`/queue/unmute`, undefined, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            });
    }

    // Retrieves the application settings.
    async getApplicationSettings() {
        return httpClient.get(`/system/settings`, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: ApplicationSettings) => {
                return data;
            });
    }

    // Sets the application settings.
    async patchSetApplicationSettings(applicationSettings: object) {
        return httpClient.patch(`/system/settings/set`, applicationSettings, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: ApplicationSettings) => {
                return data;
            });
    }

    async deleteSongs(selectedSongs: { title: string, numberInQueue: number }[]) {
        return httpClient.delete(`/queue/delete`, selectedSongs, cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
            });
    }
}