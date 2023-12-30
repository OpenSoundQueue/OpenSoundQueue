import {defineStore} from "pinia";
import {ref} from "vue";
import type {Ref} from "vue";
import {HttpService} from "@/services/HttpService";

const httpService = new HttpService();

export const useInstallationStore = defineStore('installation', () => {
    const language: Ref<string> = ref("en");
    const requireEmailAuth: Ref<boolean> = ref(true);
    const isPrivate: Ref<boolean> = ref(true);
    const entryCode: Ref<string> = ref("");
    const sources: Ref<string[]> = ref([]);

    function setLanguage(newLanguage: string) {
        language.value = newLanguage;
    }

    function toggleEmailAuth() {
        requireEmailAuth.value = !requireEmailAuth.value;
    }

    function toggleIsPrivate() {
        isPrivate.value = !isPrivate.value;
    }

    function setEntryCode(newEntryCode: string) {
        entryCode.value = newEntryCode;
    }

    function setSources(sourceSelection: string[]) {
        sources.value = sourceSelection;
    }

    async function savePrivacy() {
        await httpService.setPrivacy({
            "isPrivate": isPrivate.value,
            "entryCode": entryCode.value
        })
            .then(data => {
                isPrivate.value = data.isPrivate;
                entryCode.value = data.entryCode;
            })
    }

    return {
        language,
        requireEmailAuth,
        isPrivate,
        entryCode,
        sources,
        setLanguage,
        toggleEmailAuth,
        toggleIsPrivate,
        setEntryCode,
        setSources,
        savePrivacy
    }

})