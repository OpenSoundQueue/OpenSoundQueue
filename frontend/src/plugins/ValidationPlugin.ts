import type {App, Plugin} from "vue";

/*
Password rule: The password must be at least 8 characters long and meet the following requirements:
    - At least one uppercase letter (A-Z)
    - At least one lowercase letter (a-z)
    - At least one digit (0-9)
    - At least one special character from [@#$%^&+=!]

Input code rule: The input code must be exactly 6 characters long and may only consist of letters (upper and lower case) and digits.

Username rule: The username must be between 4 and 20 characters long and meet the following requirements:
    - May contain an uppercase letter (A-Z)
    - May contain a lowercase letter (a-z)
    - May contain a digit (0-9)
    - May contain a period (.) or underscore (_)
 */

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
            const usernameRegex = /^([A-Za-z0-9]?)([._]?).{4,20}$/;
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