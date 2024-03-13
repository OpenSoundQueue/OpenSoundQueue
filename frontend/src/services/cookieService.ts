// This function retrieves the value of a cookie by name.
// If the cookie doesn't exist, the function returns an empty string.
function getCookie(cname: string): string {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

// This function retrieves the API key from the sessionKey cookie.
export function getApiKey() {
    return getCookie("sessionKey");
}

// This function clears the sessionKey cookie, effectively logging the user out.
export function clearApiKey() {
    document.cookie = "sessionKey= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
}

// This function sets the API key in the sessionKey cookie.
export function setApiKey(apiKey: string) {
    document.cookie = "sessionKey=" + apiKey + "; path=/";
}