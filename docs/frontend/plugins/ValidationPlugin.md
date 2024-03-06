# ValidationPlugin.ts Documentation 📄

The `ValidationPlugin.ts` file defines a Vue plugin that provides a suite of validation functions for various types of inputs, such as usernames, passwords, entry codes, song links, and email addresses. This plugin can be easily integrated into Vue applications to facilitate input validation across the application.

## Overview 🌟

The plugin is designed to be registered with a Vue application, making its validation functions globally accessible. This allows for a streamlined and centralized approach to input validation.

## Functions 🛠️

Below is a detailed description of each validation function provided by this plugin:

| Function Name        | Purpose                                              | Validation Performed                                                                                      |
|----------------------|------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| `validateUsername`   | Validates usernames                                  | - Checks for 4-20 characters<br>- Allows letters, numbers, periods, and underscores                       |
| `validatePassword`   | Validates passwords                                  | - Must be at least 8 characters<br>- Requires uppercase, lowercase, number, and special character         |
| `validateEntryCode`  | Validates entry codes                                | - Exactly 6 alphanumeric characters                                                                       |
| `validateSongLink`   | Validates song links (YouTube and SoundCloud)        | - Supports YouTube and SoundCloud links                                                                   |
| `validateEmail`      | Validates email addresses                            | - Standard email format validation                                                                        |

### Example Usage

To use these validation functions in your Vue components, you can simply call them like so:

```javascript
this.$validateUsername('someUsername').then(isValid => {
    if (isValid) {
        // Username is valid
    } else {
        // Username is invalid
    }
});
```

## Vue Plugin Definition 📦

The plugin is defined in the following way:

```javascript
export const ValidationPlugin: Plugin = {
  install: (app: App) => {
    app.config.globalProperties.$validateUsername = validateUsername;
    app.config.globalProperties.$validatePassword = validatePassword;
    app.config.globalProperties.$validateEntryCode = validateEntryCode;
    app.config.globalProperties.$validateEmail = validateEmail;
    app.provide('validationPlugin', {});
  },
};
```

### Installation

To integrate the `ValidationPlugin` into your Vue application, you will need to install it during the Vue application initialization phase:

```javascript
import { createApp } from 'vue';
import App from './App.vue';
import { ValidationPlugin } from './ValidationPlugin';

const app = createApp(App);

app.use(ValidationPlugin);

app.mount('#app');
```

## Conclusion 🎉

The `ValidationPlugin.ts` file equips Vue developers with a powerful and easy-to-use set of validation functions. By leveraging this plugin, developers can ensure that user inputs across their applications meet predefined criteria, enhancing both security and user experience.