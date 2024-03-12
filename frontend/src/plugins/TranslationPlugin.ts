import type {App, Plugin} from "vue";
import {ref} from "vue";

// Import translation files
import enTranslations from "@/translations/en.json";
import deTranslations from "@/translations/de.json";
import esTranslations from "@/translations/es.json";

// Import settings from the store
import {settings} from "@/store/store";

// Define a utility type to flatten translation keys
type FlattenTranslations<T, K extends string | number = ''> = {
    [P in keyof T & string]: T[P] extends Record<string, any>
        ? FlattenTranslations<T[P], `${K}${"" extends K ? "" : "."}${P}`>
        : `${K}${"" extends K ? "" : "."}${P}`;
}[keyof T & string];

// Define the type for translation keys
export type TranslationsKey = FlattenTranslations<typeof enTranslations>;

// Define a reactive reference to store the current language
const currentLanguage = ref('en');

// Define an object to store translations for different languages
export const translations: Record<string, Record<string, any>> = {
    en: enTranslations,
    de: deTranslations,
    es: esTranslations
};

// Function to set the current language
export const setLanguage = (language: string) => {
    if (Object.keys(translations).includes(language)) {
        currentLanguage.value = language;
    } else {
        console.warn(`Language '${language}' is not available.`);
    }
};

// Function to translate a key
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

// Function to get the current language
export const getCurrentLanguage = () => {
    return currentLanguage;
};

// Vue plugin definition
export const TranslationPlugin: Plugin = {
    install: (app: App) => {
        // Add translation-related properties and methods to the app instance
        app.config.globalProperties.$translate = translate;
        app.config.globalProperties.$setLanguage = setLanguage;
        app.config.globalProperties.$getCurrentLanguage = getCurrentLanguage;

        // Provide translations to components using the 'translatePlugin' key
        app.provide('translatePlugin', translations);

        // Set the initial language based on settings
        setLanguage(settings.language);
    },
};