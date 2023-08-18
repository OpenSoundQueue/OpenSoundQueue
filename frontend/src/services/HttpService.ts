import HttpClient from "@/services/HttpClient";
import {Song} from "@/models/Song";

const httpClient = new HttpClient();

export class HttpService {
    postQueue(pageNumber: number, pageSize: number) {
        const data = {
            pageNumber: pageNumber,
            pageSize: pageSize
        }

        return httpClient.post("/queue", data)
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