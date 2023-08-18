type httpMethod = "GET" | "POST" | "PUT" | "DELETE" | "PATCH";

export default class HttpClient {
    request(url: string, data: unknown, method: httpMethod, headers: Record<string, string>) {
        let jsonData = JSON.stringify(data);

        return fetch(url, {
            method: method,
            headers: headers,
            body: jsonData
        });
    }

    rest(path: string, data: unknown, method: httpMethod) {
        let headers = {"Content-Type": "application/json"};

        const url = "/api" + path;
        return this.request(url, data, method, headers);
    }

    post(path: string, data: unknown) {
        return this.rest(path, data, "POST");
    }
}