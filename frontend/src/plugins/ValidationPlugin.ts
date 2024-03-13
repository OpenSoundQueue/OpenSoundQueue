import type { App, Plugin } from "vue";

// Function to validate usernames
export function validateUsername(value: string): Function {
    return () => {
        if (value.length === 0) {
            return true;
        }

        const usernameRegex = /^([A-Za-z0-9]?)([._]?).{4,20}$/;
        return usernameRegex.test(value);
    }
}

// Function to validate passwords
export function validatePassword(value: string): Function {
    return () => {
        if (value.length === 0) {
            return true;
        }

        const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,}$/;
        return passwordRegex.test(value);
    }
}

// Function to validate entry codes
export function validateEntryCode(value: string): Function {
    return () => {
        if (value.length === 0) {
            return true;
        }

        const entryCodeRegex = /^[a-zA-Z0-9]{6}$/;
        return entryCodeRegex.test(value);
    }
}

// Function to validate song links (supports YouTube and SoundCloud links)
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

// Function to validate email addresses
export function validateEmail(value: string) {
    return () => {
        if (value.length === 0) {
            return true;
        }

        const emailRegex = /^[^.\s][\w\-]+(\.[\w\-]+)*@([\w-]+\.)+[\w-]{2,}$/;
        return emailRegex.test(value);
    }
}

export function isEmpty(value: string) {
    return () => {
        if (value.length === 0) {
            return false;
        }

        return true;
    }
}

// Vue plugin definition
export const ValidationPlugin: Plugin = {
    install: (app: App) => {
        // Register validation functions as global properties in Vue app
        app.config.globalProperties.$validateUsername = validateUsername;
        app.config.globalProperties.$validatePassword = validatePassword;
        app.config.globalProperties.$validateEntryCode = validateEntryCode;
        app.config.globalProperties.$validateEmail = validateEmail;
        app.config.globalProperties.$isEmpty = isEmpty;

        // Provide an empty object as a placeholder for future use
        app.provide('validationPlugin', {});
    },
};
