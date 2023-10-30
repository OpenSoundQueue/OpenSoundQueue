import HttpClient from "@/services/HttpClient";
import {Song} from "@/models/Song";
import {User} from "@/models/User";

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
            entryCode: entryCode})
            .then(async response => {
                if (!response.ok){
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
            password: password})
            .then(async response => {
                if (!response.ok){
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
            entryCode: entryCode})
            .then(async response => {
                if (!response.ok){
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data: Record<string, string>) => {
                return data.apiKey;
            });
    }

    async postPublicLogin(username: string) {
        return await httpClient.post(`/login/public`, {
            username: username})
            .then(async response => {
                if (!response.ok){
                    return Promise.reject(response.status);
                }
                return response.json();
            }).then((data: Record<string, string>) => {
                return data.apiKey;
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
        return httpClient.get(`/users`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: User[]) => {
                return data;
            });
    }

    async deleteUser(userID: number) {
        return httpClient.delete(`/user/`+userID)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: Record<string,string>) => {
                return data;
            });
    }
}