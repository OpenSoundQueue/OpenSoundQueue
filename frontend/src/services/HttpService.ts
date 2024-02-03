import HttpClient from "@/services/HttpClient";
import {Song} from "@/models/Song";
import {User} from "@/models/User";
import * as cookieService from "@/services/cookieService";
import type {Role} from "@/models/Role";
import {setLanguage} from "@/plugins/TranslationPlugin";
import type {ApplicationSettings} from "@/models/ApplicationSettings";

const httpClient = new HttpClient();

export class HttpService {
    async getQueuePage(pageNumber: number, pageSize: number) {
        type QueuePage = {
            page: [{
                numberInQueue: number,
                song: Song
            }],
            numberOfPages: number
        }

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

    async getQueueAll() {
        type Queue = Array<{ numberInQueue: number, song: Song }>;

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

    async getNowPlaying() {
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

    async postQueueAdd(link: string, apiKey: string) {
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

    async getVoteSkipStatus(apiKey: string) {
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

    async getVoteSkipVote(apiKey: string) {
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

    async getVoteSkipWithdraw(apiKey: string) {
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

    async postSkip(apiKey: string) {
        return httpClient.post("/queue/skip", "", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response;
            })
    }

    async postReplay(apiKey: string) {
        return httpClient.post("/queue/replay", "", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response;
            })
    }

    async postStart(apiKey: string) {
        return httpClient.post("/queue/start", "", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response;
            })
    }

    async postStop(apiKey: string) {
        return httpClient.post("/queue/stop", "", apiKey)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response;
            })
    }

    async getSearchHistory(searchTerm: string, maxResults: number, apiKey: string) {
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

    async postPrivateAuthLogin(username: string, password: string, entryCode: string) {
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

    async postPublicAuthLogin(username: string, password: string) {
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

    async postResendMail(email: string) {
        return httpClient.post(`/register/resend-email`, {email: email})
            .then(response => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
            });
    }

    async postLogout(apiKey: string) {
        return await httpClient.post(`/logout`, undefined, apiKey)
            .then(response => {
                cookieService.clearApiKey();

                if (!response.ok) {
                    return Promise.reject(response.status);
                }
            });
    }

    async getVerifyApiKey(apiKey: string) {
        return await httpClient.get(`/verify/api-key`, apiKey)
            .then(response => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
            });
    }

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

    async setInstallationStateComplete() {
        return httpClient.patch(`/system/installation-state/complete`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
            }).then((data) => {
                return data;
            });
    }


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

    async patchSetApplicationSettings(applicationSettings: object) {
        console.log(applicationSettings)
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
}