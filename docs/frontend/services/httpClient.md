# HttpClient.ts Documentation

The `HttpClient.ts` file defines a versatile and reusable HTTP client class for making RESTful API requests from your JavaScript or TypeScript applications. This document provides a comprehensive overview of its capabilities, methods, and usage.

## Overview

The `HttpClient` class provides a streamlined approach to executing network requests such as GET, POST, DELETE, PATCH, and PUT. It abstracts away the complexity of directly using the `fetch` API by offering convenient methods tailored for quick integration with RESTful APIs.

### Key Features

- Supports basic HTTP methods (`GET`, `POST`, `PUT`, `DELETE`, `PATCH`)
- Simplifies making requests with JSON data
- Handles API keys and CORS mode in request headers
- Automatically constructs API URLs based on the environment (development or production)

## Methods

The table below summarizes the methods provided by the `HttpClient` class:

| Method | Description | Parameters |
|--------|-------------|------------|
| `request` | Performs an HTTP request with specified URL, data, method, and headers. | `url`: string, `data`: unknown, `method`: httpMethod, `headers`: Record<string, string> |
| `rest` | Facilitates RESTful API requests with a given path, data, and method. Optionally includes an API key. | `path`: string, `data`: unknown, `method`: httpMethod, `apiKey`: string (optional) |
| `get` | Executes a GET request to a specified path. Optionally includes an API key. | `path`: string, `apiKey`: string (optional) |
| `delete` | Executes a DELETE request to a specified path, with optional data and API key. | `path`: string, `data`: unknown (optional), `apiKey`: string (optional) |
| `post` | Executes a POST request with provided data to a specified path. Optionally includes an API key. | `path`: string, `data`: unknown, `apiKey`: string (optional) |
| `patch` | Executes a PATCH request with provided data to a specified path. Optionally includes an API key. | `path`: string, `data`: unknown (optional), `apiKey`: string (optional) |

### Code Examples

**Creating an instance of HttpClient**

```typescript
import HttpClient from './HttpClient.ts';

const httpClient = new HttpClient();
```

**Making a GET request**

```typescript
httpClient.get('/users')
  .then(response => response.json())
  .then(data => console.log(data));
```

**Posting data with an API key**

```typescript
const userData = { name: "John Doe", email: "john@example.com" };

httpClient.post('/users', userData, 'YOUR_API_KEY_HERE')
  .then(response => response.json())
  .then(data => console.log(data));
```

## Usage Considerations

- **API Key**: When making requests to APIs requiring authentication, ensure to pass the API key (if needed) while calling the respective method.
- **CORS**: The `HttpClient` class sets the request mode to `cors` by default to facilitate cross-origin requests.
- **Environment**: The class automatically determines the API URL based on the development or production environment. Ensure that `import.meta.env.DEV` is correctly configured for development scenarios.

## Conclusion

The `HttpClient` class provides a robust and simplified approach to making HTTP requests in modern web applications. By abstracting the complexities of the `fetch` API and providing convenience methods for various HTTP operations, it enables developers to focus on building features without worrying about the underlying network request mechanics.