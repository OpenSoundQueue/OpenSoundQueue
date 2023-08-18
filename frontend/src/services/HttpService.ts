import HttpClient from "@/services/HttpClient";
import {Song} from "@/models/Song";

const httpClient = new HttpClient();

export class HttpService {
    postQueuePage(pageNumber: number, pageSize: number) {
        type QueuePage = {
            page: [{
                numberInQueue: number,
                song: Song
            }],
            numberOfPages: number
        }

        const data = {
            pageNumber: pageNumber,
            pageSize: pageSize
        }

        return httpClient.post("/queue-page", data)
            .then((response) => {
                if (!response.ok) {
                    return Promise.reject(response.status);
                }

                return response.json();
            }).then((data: QueuePage) => {
                return data;
            });
    }
}