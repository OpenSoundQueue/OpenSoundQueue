import {reactive, watch} from "vue";
import type {Settings} from "@/store/settings";
import {getSettings} from "@/store/settings";
import type {Installation} from "@/store/installation";
import {getInstallation} from "@/store/installation";

export let settings: Settings = reactive({
    language: "en"
});
export let installation: Installation = reactive({
    currentStep: "language"
});

try {
    settings = reactive(getSettings());
    installation = reactive(getInstallation());
} catch (error) {
    console.error(error);

    // using default settings
    localStorage.setItem("settings", JSON.stringify(settings));
    localStorage.setItem("installation", JSON.stringify(installation));
}

watch(settings, (newValue) => {
    localStorage.setItem("settings", JSON.stringify(newValue));
});
watch(installation, (newValue) => {
    localStorage.setItem("installation", JSON.stringify(newValue));
});
