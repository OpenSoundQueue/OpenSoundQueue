import type {App, Plugin} from "vue";
import translations from "@/translations/en.json";

export type TranslationsKey = keyof typeof translations;

export const TranslationPlugin: Plugin = {
    install: (app: App, translation: Record<string, string>) => {
        app.config.globalProperties.$translate = (key: string) => {
            return translation[key];
        }

        app.provide('translatePlugin', translation);
    }
}