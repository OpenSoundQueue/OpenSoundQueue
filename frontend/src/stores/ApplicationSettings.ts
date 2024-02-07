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
            httpService.getApplicationSettings()
                .then((data) => {
                    persistedApplicationSettings.value = new ApplicationSettings(
                        data.language,
                        data.emailAuth,
                        data.isPrivate,
                        data.entryCode,
                        data.enabledSources,
                        data.supportedSources
                    );

                    editedApplicationSettings.value = ApplicationSettings.clone(persistedApplicationSettings.value);
                });
        } catch (error) {
            ToastService.sendNotification(translate("applicationSettings.fetchError"), "error", 3000);
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
            ToastService.sendNotification(translate("applicationSettings.saveError"), "error", 3000);
            return;
        }

        await httpService.patchSetApplicationSettings(editedApplicationSettings.value.toDto())
            .catch(() => {
                ToastService.sendNotification(translate("applicationSettings.saveError"), "error", 3000);
            });

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

        editedApplicationSettings.value.emailAuth = value;
    }

    function toggleSource(source: string) {
        if (!editedApplicationSettings.value) {
            return
        }

        if (editedApplicationSettings.value.enabledSources.includes(source)) {
            editedApplicationSettings.value.enabledSources.splice(editedApplicationSettings.value.enabledSources.indexOf(source), 1)
        } else {
            editedApplicationSettings.value.enabledSources.push(source)
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