import HttpClient from "@/services/HttpClient";
import {Song} from "@/models/Song";
import {User} from "@/models/User";
import * as cookieService from "@/services/cookieService";
import type {Role} from "@/models/Role";
import type {Permission} from "@/models/Permission";

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
        return httpClient.get(`/roles`,cookieService.getApiKey())
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
        return httpClient.get(`/permissions`,cookieService.getApiKey())
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
        return httpClient.get(`/role/get/${id}`,cookieService.getApiKey())
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
        return httpClient.delete(`/role/${id}`,cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
            }).then((data) => {
                return data;
            });
    }

    async postRoleAdd(name: string, permissions: { [key: string]: boolean }[]) {
        return httpClient.patch(`/role/create`, {name: name, permissions: permissions},cookieService.getApiKey())
            .then((response) => {
                console.log(response.ok)
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: Permission[]) => {
                return data;
            });
    }

    async updateRole(role: object) {
        return httpClient.patch(`/role/edit`, role,cookieService.getApiKey())
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data:Role) => {
                return data;
            });
    }
}