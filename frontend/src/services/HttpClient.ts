// Defines a type alias for HTTP methods supported by the HttpClient class.
type httpMethod = "GET" | "POST" | "PUT" | "DELETE" | "PATCH";

// Defines an HTTP client class for making RESTful API requests.
export default class HttpClient {
    // Performs an HTTP request with the specified URL, data, method, and headers.
    request(url: string, data: unknown, method: httpMethod, headers: Record<string, string>) {
        const jsonData = JSON.stringify(data);

        return fetch(url, {
            method: method,
            headers: headers,
            body: jsonData
        });
    }

    // Makes a RESTful API request with the given path, data, method, and API key.
    rest(path: string, data: unknown, method: httpMethod, apiKey?: string,) {
        const headers: Record<string, string> = {"Content-Type": "application/json", "mode": "cors"};

        // Adds the API key to the headers if provided.
        if (apiKey) {
            headers["X-API-KEY"] = apiKey;
        }

        // Constructs the API URL based on the environment and provided path.
        let url;
        // if (import.meta.env.DEV) {
            // url = "/api" + path;
        //} else {
            url = `http://${window.location.hostname}:8080/api` + path;
        //}

        // Performs the HTTP request with the constructed URL, data, method, and headers.
        return this.request(url, data, method, headers);
    }

    // Makes a GET request to the specified path with an optional API key.
    get(path: string, apiKey?: string) {
        return this.rest(path, undefined, "GET", apiKey);
    }

    // Makes a DELETE request to the specified path with an optional API key.
    delete(path: string, data?: unknown,apiKey?: string) {
        return this.rest(path, data, "DELETE", apiKey);
    }

    // Makes a POST request to the specified path with the provided data and an optional API key.
    post(path: string, data: unknown, apiKey?: string) {
        return this.rest(path, data, "POST", apiKey);
    }

    // Makes a PATCH request to the specified path with the provided data and an optional API key.
    patch(path: string, data?: unknown, apiKey?: string) {
        return this.rest(path, data, "PATCH", apiKey);
    }
}