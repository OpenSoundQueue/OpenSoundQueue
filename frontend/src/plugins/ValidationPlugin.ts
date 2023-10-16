import type {App, Plugin} from "vue";

function validate(type: string, value: string): boolean {
    if (value.length === 0) return true;
    switch (type) {
        case "password":
            const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,}$/;
            return passwordRegex.test(value);
        case "entryCode":
            const entryCodeRegex = /^[a-zA-Z0-9]{6}$/;
            return entryCodeRegex.test(value);
        case "username":
            const usernameRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[._]).{4,20}$/;
            return usernameRegex.test(value);
        default:
            return false
    }
}

export function validateUsername(value: string): Function {
    return () => validate("username", value)
}

export function validatePassword(value: string): Function {
    return () => validate("password", value)
}

export function validateEntryCode(value: string): Function {
    return () => validate("entryCode", value)
}

export const ValidationPlugin: Plugin = {
    install: (app: App) => {
        app.config.globalProperties.$validateUsername = validateUsername;
        app.config.globalProperties.$validatePassword = validatePassword;
        app.config.globalProperties.$validateEntryCode = validateEntryCode;
        app.provide('validationPlugin', {});
    },
};