import type {App, Plugin} from "vue";
import {ref} from "vue";
import enTranslations from "@/translations/en.json";
import deTranslations from "@/translations/de.json";
import {settings} from "@/store/store";

type FlattenTranslations<T, K extends string | number = ''> = {
    [P in keyof T & string]: T[P] extends Record<string, any>
        ? FlattenTranslations<T[P], `${K}${"" extends K ? "" : "."}${P}`>
        : `${K}${"" extends K ? "" : "."}${P}`;
}[keyof T & string];

export type TranslationsKey = FlattenTranslations<typeof enTranslations>;

const currentLanguage = ref('en');
const translations: Record<string, Record<string, any>> = {
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
    const keys = key.split('.');
    let current: string | Record<string, any> = translations[currentLanguage.value];

    for (const k of keys) {
        if (current && typeof current === 'object' && k in current) {
            current = current[k];
        } else {
            // Return an error message or a default value if the key is not found
            return `Translation for key '${key}' not found`;
        }
    }

    return current as string;
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

        setLanguage(settings.language);
    },
};