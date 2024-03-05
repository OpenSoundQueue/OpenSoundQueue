import { defineStore } from "pinia";
import { ref, watchEffect } from "vue";
import type { Ref } from "vue";
import { HttpService } from "@/services/HttpService";
import { ToastService } from "@/services/ToastService";
import { translate } from "@/plugins/TranslationPlugin";

// Initialize HttpService
const httpService = new HttpService();

// Define installation store
export const useInstallationStore = defineStore('installation', () => {
    // Reactive references for installation settings
    const fetchedSetting = ref(false);
    const language = ref("en");
    const requireEmailAuth = ref(true);
    const isPrivate = ref(true);
    const entryCode = ref("");
    const sources: Ref<string[]> = ref([]);

    // Fetch installation settings from the server
    watchEffect(async () => {
        if (!fetchedSetting.value) {
            try {
                const [languageData, authData, privacyData, sourcesData] = await Promise.all([
                    httpService.getLanguage(),
                    httpService.getAuthentication(),
                    httpService.getPrivacy(),
                    httpService.getSources()
                ]);

                // Update installation settings
                language.value = languageData.language;
                requireEmailAuth.value = authData.emailAuth == "true";
                isPrivate.value = privacyData.isPrivate == "true";
                entryCode.value = privacyData.entryCode;
                sources.value = sourcesData;

                fetchedSetting.value = true;
            } catch (error) {
                // Notify user if fetching installation settings fails
                ToastService.sendNotification(translate(('notifications.installation.fetchError')), "error", 3000);
            }
        }
    });

    // Set language
    function setLanguage(newLanguage: string) {
        language.value = newLanguage;
    }

    // Toggle email authentication
    function toggleEmailAuth() {
        requireEmailAuth.value = !requireEmailAuth.value;
    }

    // Toggle privacy
    function toggleIsPrivate() {
        isPrivate.value = !isPrivate.value;
    }

    // Set entry code
    function setEntryCode(newEntryCode: string) {
        entryCode.value = newEntryCode;
    }

    // Toggle source
    function toggleSource(source: string) {
        if (sources.value.includes(source)) {
            sources.value.splice(sources.value.indexOf(source), 1);
        } else {
            sources.value.push(source);
        }
    }

    // Save language settings
    async function saveLanguage() {
        await httpService.setLanguage(language.value)
            .then(data => {
                language.value = data.language;
                return Promise.resolve();
            })
            .catch(() => {
                // Notify user if saving language settings fails
                ToastService.sendNotification(translate(('notifications.installation.saveLanguageError')), "error", 3000);
                return Promise.reject();
            });
    }

    // Save privacy settings
    async function savePrivacy() {
        await httpService.setPrivacy({
            "isPrivate": isPrivate.value,
            "entryCode": entryCode.value
        })
            .then(data => {
                isPrivate.value = data.isPrivate;
                entryCode.value = data.entryCode;
                return Promise.resolve();
            })
            .catch(() => {
                // Notify user if saving privacy settings fails
                ToastService.sendNotification(translate(('notifications.installation.savePrivacyError')), "error", 3000);
                return Promise.reject();
            });

        // Save email authentication settings
        await httpService.setAuthentication(requireEmailAuth.value)
            .then(data => {
                requireEmailAuth.value = data.emailAuth == "true";
                return Promise.resolve();
            })
            .catch(() => {
                // Notify user if saving privacy settings fails
                ToastService.sendNotification(translate(('notifications.installation.savePrivacyError')), "error", 3000);
                return Promise.reject();
            });
    }

    // Save source settings
    async function saveSources() {
        await httpService.setSources(sources.value)
            .then(data => {
                sources.value = data;
                return Promise.resolve();
            })
            .catch(() => {
                // Notify user if saving source settings fails
                ToastService.sendNotification(translate(('notifications.installation.saveSourcesError')), "error", 3000);
                return Promise.reject();
            });
    }

    // Return methods and reactive properties
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
        toggleSource,
        saveLanguage,
        savePrivacy,
        saveSources
    };
});