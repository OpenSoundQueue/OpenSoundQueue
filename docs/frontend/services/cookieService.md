# Cookie Service Documentation 📜

The `cookieService.ts` module provides a simple yet powerful interface for managing cookies within a web application. This documentation outlines the functionality provided by the module, including retrieving, setting, and clearing cookies. The primary focus is on managing an API key stored within a cookie named `sessionKey`.

## Functions Overview

| Function Name  | Description                                                   |
|----------------|---------------------------------------------------------------|
| `getCookie`    | Retrieves the value of a cookie by name.                     |
| `getApiKey`    | Retrieves the API key from the `sessionKey` cookie.          |
| `clearApiKey`  | Clears the `sessionKey` cookie, effectively logging the user out. |
| `setApiKey`    | Sets the API key in the `sessionKey` cookie.                 |

## Function Details

### **`getCookie(cname: string): string`**

This function retrieves the value of a specified cookie by name. If the cookie doesn't exist, it returns an empty string.

- **Parameters:**
  - `cname`: The name of the cookie to retrieve.

- **Returns:** The value of the cookie, or an empty string if the cookie does not exist.

#### Example Usage

```typescript
let userSessionId = getCookie("userSession");
```

### **`getApiKey(): string`**

Retrieves the API key stored within the `sessionKey` cookie.

- **Returns:** The value of the `sessionKey` cookie, or an empty string if it doesn't exist.

#### Example Usage

```typescript
let apiKey = getApiKey();
```

### **`clearApiKey(): void`**

Clears the `sessionKey` cookie by setting its expiry date to a past date, effectively logging the user out.

- **No Parameters.**

#### Example Usage

```typescript
clearApiKey();
```

### **`setApiKey(apiKey: string): void`**

Stores an API key within the `sessionKey` cookie.

- **Parameters:**
  - `apiKey`: The API key to store in the cookie.

- **No Return Value.**

#### Example Usage

```typescript
setApiKey("abc123xyz");
```

## 🚀 Quick Tips

- **Security:** When using cookies to store sensitive information like API keys, consider implementing additional security measures such as HTTPS and cookie encryption.
- **HTTP-Only Cookies:** For improved security, consider setting the `HttpOnly` flag on cookies that should not be accessible via JavaScript.

This module simplifies cookie management, specifically focusing on the use of cookies for storing and retrieving API keys. Keep in mind the security implications when working with cookies and sensitive information.

🔒 Happy Coding!