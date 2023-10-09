import type {App, Plugin} from "vue";

function validate(type: string, value: string): boolean {
    if (value.length === 0) return true;
    switch (type) {
        case "password":
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
            return passwordRegex.test(value);
        case "entryCode":
            const entryCodeRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
            return entryCodeRegex.test(value);
        case "username":
            const usernameRegex = /^[a-zA-Z0-9_-]{3,20}$/;
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