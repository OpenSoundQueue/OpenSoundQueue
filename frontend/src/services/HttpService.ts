import HttpClient from "@/services/HttpClient";
import {Song} from "@/models/Song";

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

    async postQueueAdd(link: string) {
        return httpClient.post("/queue/add", {link: link})
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            })
    }

    async getVoteSkipStatus() {
        return httpClient.get("/vote-skip/status")
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            })
    }

    async getVoteSkipVote() {
        return httpClient.get("/vote-skip/vote")
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            })
    }

    async getVoteSkipWithdraw() {
        return httpClient.get("/vote-skip/withdraw")
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data) => {
                return data;
            })
    }

    async getSearchHistory(searchTerm: string, maxResults: number) {
        return httpClient.get(`/search/history/${searchTerm}/max-results/${maxResults}`)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: Song[]) => {
                return data;
            });
    }
}