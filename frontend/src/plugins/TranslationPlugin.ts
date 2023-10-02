import type {App, Plugin} from "vue";
import enTranslations from "@/translations/en.json";
import deTranslations from "@/translations/de.json";
import {ref} from "vue";

export type TranslationsKey = keyof typeof enTranslations;

const currentLanguage = ref('en');
const translations: Record<string, Record<string, string>> = {
    en: enTranslations,
    de: deTranslations
};

export const setLanguage = (language: string) => {
    if (Object.keys(translations).includes(language)) {
        currentLanguage.value = language;
    } else {
        console.warn(`Language '${language}' is not available.`);
    }
};

export const translate = (key: string) => {
    return translations[currentLanguage.value][key];
};

export const getCurrentLanguage = () => {
    return currentLanguage;
};

export const TranslationPlugin: Plugin = {
    install: (app: App) => {
        app.config.globalProperties.$translate = translate;
        app.config.globalProperties.$setLanguage = setLanguage;
        app.config.globalProperties.$getCurrentLanguage = getCurrentLanguage;
        app.provide('translatePlugin', translations);
    },
};