import {defineStore} from "pinia";
import {ref} from "vue";
import {ApplicationSettings} from "@/models/ApplicationSettings";
import type {Ref} from "vue";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";
import {HttpService} from "@/services/HttpService";
import {computed} from "vue";

const httpService = new HttpService();

export const useApplicationSettingsStore = defineStore('applicationSettings', () => {
    const persistedApplicationSettings: Ref<ApplicationSettings | undefined> = ref();
    const editedApplicationSettings: Ref<ApplicationSettings | undefined> = ref();

    const areApplicationSettingsEdited = computed(() => {
        if (persistedApplicationSettings.value === undefined || editedApplicationSettings.value === undefined) {
            return false;
        }

        return !ApplicationSettings.areEqual(persistedApplicationSettings.value, editedApplicationSettings.value);
    });

    async function fetchApplicationSettings() {
        try {
            const [languageData, authData, privacyData, sourcesData, supportedSourcesData] = await Promise.all([
                httpService.getLanguage(),
                httpService.getAuthentication(),
                httpService.getPrivacy(),
                httpService.getSources(),
                httpService.getSupportedSources()
            ]);

            persistedApplicationSettings.value = new ApplicationSettings(
                languageData.language,
                authData.emailAuth == "true",
                privacyData.isPrivate == "true",
                privacyData.entryCode,
                sourcesData,
                supportedSourcesData
            );

            editedApplicationSettings.value = ApplicationSettings.clone(persistedApplicationSettings.value);
        } catch (error) {
            ToastService.sendNotification(translate(('notifications.installation.fetchError')), "error", 3000);
        }
    }

    function rollback() {
        if (persistedApplicationSettings.value === undefined) {
            editedApplicationSettings.value = undefined;
        } else {
            editedApplicationSettings.value = ApplicationSettings.clone(persistedApplicationSettings.value);
        }
    }

    async function save() {
        if (editedApplicationSettings.value === undefined) {
            // TODO: write error message
            ToastService.sendNotification("", "error", 3000);
            return;
        }

        try {
            await Promise.all([httpService.setPrivacy({
                    isPrivate: editedApplicationSettings.value.isPrivate,
                    entryCode: editedApplicationSettings.value.entryCode
                }),
                httpService.setAuthentication(editedApplicationSettings.value.requireEmailAuth),
                httpService.setSources(editedApplicationSettings.value.sources),
                httpService.setLanguage(editedApplicationSettings.value.language),
            ]);
        } catch (error) {
            // TODO: write error message
            ToastService.sendNotification("", "error", 3000);
        }

        await fetchApplicationSettings();
    }

    function setIsPrivate(value: boolean) {
        if (!editedApplicationSettings.value) {
            return
        }

        editedApplicationSettings.value.isPrivate = value;
    }

    function setRequireEmailAuth(value: boolean) {
        if (!editedApplicationSettings.value) {
            return
        }

        editedApplicationSettings.value.requireEmailAuth = value;
    }

    function toggleSource(source: string) {
        if (!editedApplicationSettings.value) {
            return
        }

        if (editedApplicationSettings.value.sources.includes(source)) {
            editedApplicationSettings.value.sources.splice(editedApplicationSettings.value.sources.indexOf(source), 1)
        } else {
            editedApplicationSettings.value.sources.push(source)
        }
    }

    function setLanguage(language: string) {
        if (!editedApplicationSettings.value) {
            return
        }

        editedApplicationSettings.value.language = language;
    }

    return {
        fetchApplicationSettings,
        rollback,
        save,
        persistedApplicationSettings,
        editedApplicationSettings,
        areApplicationSettingsEdited,
        setIsPrivate,
        setRequireEmailAuth,
        toggleSource,
        setLanguage,
    }
})