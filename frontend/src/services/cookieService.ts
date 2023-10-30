// gets the value of a cookie by name
// if the cookie doesn't exist, the function returns 'false'
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

export function getApiKey() {
   return getCookie("sessionKey");
}

export function clearApiKey() {
    document.cookie = "sessionKey= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
}

export function setApiKey(apiKey: string) {
    document.cookie = "sessionKey=" + apiKey + "; path=/";
}
