import {settings} from "@/store/store";

export type Settings = {
    language: string
}

export function getSettings(): Settings {
// Retrieve settings from localStorage
    const storedSettings = localStorage.getItem("settings");

    if (storedSettings !== null) {
        const parsedSettings = JSON.parse(storedSettings);

        // Check if the parsedSettings match the Settings type
        if (isSettings(parsedSettings)) {
            return parsedSettings;
        } else {
            throw new Error("Invalid settings found in localStorage.")
            // localStorage.setItem("settings", JSON.stringify(settings));
        }
        // console.error("Error parsing settings from localStorage:", error);
    } else {
        throw new Error("Invalid settings found in localStorage.");
    }
}

// Type guard function to check if an object is of type Settings
function isSettings(obj: any): obj is Settings {
    const requiredKeys = Object.keys(settings);

    return (
        typeof obj === "object" &&
        obj !== null &&
        requiredKeys.every((key) => key in obj)
    );
}
