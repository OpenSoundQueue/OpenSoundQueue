// Generates a UUID (Universally Unique Identifier) using a combination of current time, performance, and randomization.
export function generateUUID() {
    // Gets the current time in milliseconds since the Unix epoch.
    let d = new Date().getTime();
    // Checks if the 'performance' object is available and uses it to get high-resolution time.
    let d2 = ((typeof performance !== 'undefined') && performance.now && (performance.now() * 1000)) || 0;
    // Replaces the 'x' and 'y' placeholders in the UUID template with random hexadecimal digits.
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        let r = Math.random() * 16; // Generates a random number between 0 and 16.
        if (d > 0) {
            r = (d + r) % 16 | 0; // Applies bitwise OR operation to ensure the number is an integer.
            d = Math.floor(d / 16); // Divides and rounds down the current time.
        } else {
            r = (d2 + r) % 16 | 0; // Applies the same process using the high-resolution time if current time is not available.
            d2 = Math.floor(d2 / 16); // Divides and rounds down the high-resolution time.
        }
        // Converts the random number to a hexadecimal string and replaces 'x' or 'y' with it.
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
}
