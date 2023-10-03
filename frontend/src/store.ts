import {reactive, watch} from "vue";

interface Settings {
    language: string
}

export let settings: Settings = reactive({
    language: "en"
});

// Retrieve settings from localStorage
const storedSettings = localStorage.getItem("settings");

if (storedSettings !== null) {
    try {
        const parsedSettings = JSON.parse(storedSettings);

        // Check if the parsedSettings match the Settings type
        if (isSettings(parsedSettings)) {
            settings = reactive(parsedSettings);
        } else {
            console.error("Invalid settings found in localStorage.");
            console.error("Resetting and using default settings.");
            localStorage.setItem("settings", JSON.stringify(settings));
        }
    } catch (error) {
        console.error("Error parsing settings from localStorage:", error);
    }
}

watch(settings, (newValue) => {
    localStorage.setItem("settings", JSON.stringify(newValue));
});

// Type guard function to check if an object is of type Settings
function isSettings(obj: any): obj is Settings {
    const requiredKeys = Object.keys(settings);

    return (
        typeof obj === "object" &&
        obj !== null &&
        requiredKeys.every((key) => key in obj)
    );
}