import {reactive, watch} from "vue";
import type {Settings} from "@/store/settings";
import {getSettings} from "@/store/settings";
import type {Installation} from "@/store/installation";
import {getInstallation} from "@/store/installation";
import type {Registration} from "@/store/registration";
import {getRegistration} from "@/store/registration";

// Define reactive variables for settings, installation, and registration.
export let settings: Settings = reactive({
    language: "en"
});
export let installation: Installation = reactive({
    currentStep: "language"
});
export let registration: Registration = reactive({
    username: "",
    email: "",
    password: "",
    timestamp: 0,
    state: "input"
});

try {
    // Attempt to retrieve settings, installation, and registration from localStorage.
    settings = reactive(getSettings());
    installation = reactive(getInstallation());
    registration = reactive(getRegistration());
} catch (error) {
    console.error(error);

    // If there is an error and fallback to default values.
    // Save default values to localStorage.
    localStorage.setItem("settings", JSON.stringify(settings));
    localStorage.setItem("installation", JSON.stringify(installation));
    localStorage.setItem("registration", JSON.stringify(registration));
}

// Watch for changes in settings, installation, and registration, and update localStorage accordingly.
watch(settings, (newValue) => {
    localStorage.setItem("settings", JSON.stringify(newValue));
});
watch(installation, (newValue) => {
    localStorage.setItem("installation", JSON.stringify(newValue));
});
watch(registration, (newValue) => {
    localStorage.setItem("registration", JSON.stringify(newValue));
});
