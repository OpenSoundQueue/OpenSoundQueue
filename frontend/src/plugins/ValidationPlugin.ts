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

export function validateUsername(value: string): Function {
    return () => {
        if (value.length === 0) {
            return true;
        }

        const usernameRegex = /^([A-Za-z0-9]?)([._]?).{4,20}$/;
        return usernameRegex.test(value);
    }
}

export function validatePassword(value: string): Function {
    return () => {
        if (value.length === 0) {
            return true;
        }

        const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,}$/;
        return passwordRegex.test(value);
    }
}

export function validateEntryCode(value: string): Function {
    return () => {
        if (value.length === 0) {
            return true;
        }

        const entryCodeRegex = /^[a-zA-Z0-9]{6}$/;
        return entryCodeRegex.test(value);
    }
}

export function validateSonglink(value: string): Function {
    return () => {
        if (value.length === 0) {
            return true;
        }

        const youtubeLinkRegex = /^((?:https?:)?\/\/)?((?:www|m)\.)?(?:youtube(-nocookie)?\.com|youtu.be)\/.*$/;
        const soundcloudLinkRegex = /^https?:\/\/(www\.|m\.|on\.)?soundcloud\.com\/.*$/;

        return youtubeLinkRegex.test(value) || soundcloudLinkRegex.test(value);
    }
}

export function validateEmail(value: string) {
    return () => {
        if (value.length === 0) {
            return true;
        }

        const emailRegex = /^[^.\s][\w\-]+(\.[\w\-]+)*@([\w-]+\.)+[\w-]{2,}$/;
        return emailRegex.test(value);
    }
}

export const ValidationPlugin: Plugin = {
    install: (app: App) => {
        app.config.globalProperties.$validateUsername = validateUsername;
        app.config.globalProperties.$validatePassword = validatePassword;
        app.config.globalProperties.$validateEntryCode = validateEntryCode;
        app.config.globalProperties.$validateEmail = validateEmail;
        app.provide('validationPlugin', {});
    },
};