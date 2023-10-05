import {reactive, watch} from "vue";
import type {Settings} from "@/store/settings";
import {getSettings} from "@/store/settings";

export let settings: Settings = reactive({
    language: "en"
});

try {
    settings = reactive(getSettings());
} catch (error) {
    console.error(error);

    // using default settings
    localStorage.setItem("settings", JSON.stringify(settings));
}

watch(settings, (newValue) => {
    localStorage.setItem("settings", JSON.stringify(newValue));
});
