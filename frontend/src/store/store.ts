import {reactive, watch} from "vue";
import type {Settings} from "@/store/settings";
import {getSettings} from "@/store/settings";
import type {Installation} from "@/store/installation";
import {getInstallation} from "@/store/installation";
import type {Registration} from "@/store/registration";
import {getRegistration} from "@/store/registration";

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
    settings = reactive(getSettings());
    installation = reactive(getInstallation());
    registration = reactive(getRegistration());
} catch (error) {
    console.error(error);

    // using default settings
    localStorage.setItem("settings", JSON.stringify(settings));
    localStorage.setItem("installation", JSON.stringify(installation));
    localStorage.setItem("registration", JSON.stringify(registration));
}

watch(settings, (newValue) => {
    localStorage.setItem("settings", JSON.stringify(newValue));
});
watch(installation, (newValue) => {
    localStorage.setItem("installation", JSON.stringify(newValue));
});
watch(registration, (newValue) => {
    localStorage.setItem("registration", JSON.stringify(newValue));
});
