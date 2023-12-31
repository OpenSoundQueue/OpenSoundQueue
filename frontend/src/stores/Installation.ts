import {defineStore} from "pinia";
import {ref, watchEffect} from "vue";
import type {Ref} from "vue";
import {HttpService} from "@/services/HttpService";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";

const httpService = new HttpService();

export const useInstallationStore = defineStore('installation', () => {
        const fetchedSetting = ref(false);
        const language = ref("en");
        const requireEmailAuth = ref(true);
        const isPrivate = ref(true);
        const entryCode = ref("");
        const sources: Ref<string[]> = ref([]);

        watchEffect(async () => {
            if (!fetchedSetting.value) {
                try {
                    const [languageData, authData, privacyData, sourcesData] = await Promise.all([
                        httpService.getLanguage(),
                        httpService.getAuthentication(),
                        httpService.getPrivacy(),
                        httpService.getSources()
                    ]);

                    language.value = languageData.language;
                    requireEmailAuth.value = authData.emailAuth;
                    isPrivate.value = privacyData.isPrivate;
                    entryCode.value = privacyData.entryCode;
                    sources.value = sourcesData;

                    fetchedSetting.value = true;
                } catch (error) {
                    ToastService.sendNotification(translate(('notifications.installation.fetchError')), "error", 3000);
                }
            }
        });

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

        async function saveLanguage() {
            await httpService.setLanguage(language.value)
                .then(data => {
                    language.value = data.language;
                    return Promise.resolve();
                })
                .catch(() => {
                    ToastService.sendNotification(translate(('notifications.installation.saveLanguageError')), "error", 3000);
                    return Promise.reject();
                })
        }

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
                    ToastService.sendNotification(translate(('notifications.installation.savePrivacyError')), "error", 3000);
                    return Promise.reject();
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
            saveLanguage,
            savePrivacy
        }

    }
)