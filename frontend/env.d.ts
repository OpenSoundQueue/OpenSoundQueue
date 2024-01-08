/// <reference types="vite/client" />

declare module "@vue/runtime-core" {
    interface ComponentCustomProperties {
        $translate: (translationKey: string) => string;
        $validateUsername: (value: string) => Function;
        $validateEmail: (value: string) => Function;
        $validatePassword: (value: string) => Function;
        $validateEntryCode: (value: string) => Function;
    }
}
