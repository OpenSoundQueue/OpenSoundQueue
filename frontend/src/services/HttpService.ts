import HttpClient from "@/services/HttpClient";
import {Song} from "@/models/Song";

const httpClient = new HttpClient();

export class HttpService {
    postQueuePage(pageNumber: number, pageSize: number) {
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
            }).then((data: {page: Song[], numberOfPages: number}) => {
                return data;
            });
    }
}