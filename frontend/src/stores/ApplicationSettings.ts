import { defineStore } from "pinia";
import { ref } from "vue";
import { ApplicationSettings } from "@/models/ApplicationSettings";
import type { Ref } from "vue";
import { ToastService } from "@/services/ToastService";
import { translate } from "@/plugins/TranslationPlugin";
import { HttpService } from "@/services/HttpService";
import { computed } from "vue";

// Initialize HttpService
const httpService = new HttpService();

// Define application settings store
export const useApplicationSettingsStore = defineStore('applicationSettings', () => {
    // Reactive references for persisted and edited application settings
    const persistedApplicationSettings: Ref<ApplicationSettings | undefined> = ref();
    const editedApplicationSettings: Ref<ApplicationSettings | undefined> = ref();

    // Computed property to check if application settings are edited
    const areApplicationSettingsEdited = computed(() => {
        if (persistedApplicationSettings.value === undefined || editedApplicationSettings.value === undefined) {
            return false;
        }

        return !ApplicationSettings.areEqual(persistedApplicationSettings.value, editedApplicationSettings.value);
    });

    // Fetch application settings from the server
    async function fetchApplicationSettings() {
        try {
            httpService.getApplicationSettings()
                .then((data) => {
                    // Set persisted and edited application settings
                    persistedApplicationSettings.value = new ApplicationSettings(
                        data.language,
                        data.emailAuth,
                        data.fromEmail,
                        data.smtpPassword,
                        data.smtpHostString,
                        data.isPrivate,
                        data.entryCode,
                        data.enabledSources,
                        data.supportedSources
                    );
                    editedApplicationSettings.value = ApplicationSettings.clone(persistedApplicationSettings.value);
                });
        } catch (error) {
            // Notify user if fetching application settings fails
            ToastService.sendNotification(translate("applicationSettings.fetchError"), "error", 3000);
        }
    }

    // Rollback changes to application settings
    function rollback() {
        if (persistedApplicationSettings.value === undefined) {
            editedApplicationSettings.value = undefined;
        } else {
            editedApplicationSettings.value = ApplicationSettings.clone(persistedApplicationSettings.value);
        }
    }

    // Save changes to application settings
    async function save() {
        if (editedApplicationSettings.value === undefined) {
            // Notify user if edited application settings are undefined
            ToastService.sendNotification(translate("applicationSettings.saveError"), "error", 3000);
            return;
        }

        // Save edited application settings to the server
        await httpService.patchSetApplicationSettings(editedApplicationSettings.value.toDto())
            .catch(() => {
                // Notify user if saving application settings fails
                ToastService.sendNotification(translate("applicationSettings.saveError"), "error", 3000);
            });

        // Fetch updated application settings from the server
        await fetchApplicationSettings();
    }

    // Set privacy status in edited application settings
    function setIsPrivate(value: boolean) {
        if (!editedApplicationSettings.value) {
            return;
        }
        editedApplicationSettings.value.isPrivate = value;
    }

    // Set email authentication status in edited application settings
    function setRequireEmailAuth(value: boolean) {
        if (!editedApplicationSettings.value) {
            return;
        }
        editedApplicationSettings.value.emailAuth = value;
    }

    // Toggle enabled sources in edited application settings
    function toggleSource(source: string) {
        if (!editedApplicationSettings.value) {
            return;
        }
        const index = editedApplicationSettings.value.enabledSources.indexOf(source);
        if (index !== -1) {
            editedApplicationSettings.value.enabledSources.splice(index, 1);
        } else {
            editedApplicationSettings.value.enabledSources.push(source);
        }
    }

    // Set language in edited application settings
    function setLanguage(language: string) {
        if (!editedApplicationSettings.value) {
            return;
        }
        editedApplicationSettings.value.language = language;
    }

    // Return methods and reactive properties
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
    };
});