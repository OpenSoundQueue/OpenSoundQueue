/// <reference types="vite/client" />
import type {TranslationsKey} from "@/plugins/TranslationPlugin";

declare module "@vue/runtime-core" {
    interface ComponentCustomProperties {
        $translate: (translationKey: TranslationsKey) => string;
        $validateUsername: (value: string) => Function;
        $validateEmail: (value: string) => Function;
        $validatePassword: (value: string) => Function;
        $validateEntryCode: (value: string) => Function;
    }
}
