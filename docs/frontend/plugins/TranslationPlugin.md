# TranslationPlugin Documentation 🌍

`TranslationPlugin.ts` is a Vue plugin designed to add internationalization (i18n) capabilities to a Vue application. It provides a simple way of translating text within the application based on the user's language preference. This document aims to explain the functionality and usage of the plugin in detail.

## Overview

The plugin handles the translation of text strings in a Vue application by utilizing a set of translation files (JSON) for different languages. It allows changing the application's current language and fetching translations dynamically.

## Features

- **Dynamic Language Switching**: Change the application's language on the fly.
- **Translation Key Flattening**: Access nested translation strings using dot notation keys.
- **Reactive Language State**: The current language state is reactive, updating the UI automatically upon changes.
- **Easy Integration with Vue**: Designed as a Vue plugin for straightforward integration into Vue applications.

## Key Components

### Import Statements

```typescript
import type { App, Plugin } from "vue";
import { ref } from "vue";

// Import translation files
import enTranslations from "@/translations/en.json";
import deTranslations from "@/translations/de.json";

// Import settings from the store
import { settings } from "@/store/store";
```

### Utility Type for Flattening Translation Keys

```typescript
type FlattenTranslations<T, K extends string | number = ''> = {
  [P in keyof T & string]: T[P] extends Record<string, any>
    ? FlattenTranslations<T[P], `${K}${"" extends K ? "" : "."}${P}`>
    : `${K}${"" extends K ? "" : "."}${P}`;
}[keyof T & string];
```

### Translation Keys Type

```typescript
export type TranslationsKey = FlattenTranslations<typeof enTranslations>;
```

### Reactive Current Language Reference

```typescript
const currentLanguage = ref('en');
```

### Translations Object

```typescript
export const translations: Record<string, Record<string, any>> = {
  en: enTranslations,
  de: deTranslations
};
```

### Functions

- **setLanguage**: Sets the current language.
  
  ```typescript
  export const setLanguage = (language: string) => {/* implementation */};
  ```

- **translate**: Translates a key into the current language.
  
  ```typescript
  export const translate = (key: string) => {/* implementation */};
  ```

- **getCurrentLanguage**: Returns the current language.
  
  ```typescript
  export const getCurrentLanguage = () => {/* implementation */};
  ```

### Vue Plugin Definition

```typescript
export const TranslationPlugin: Plugin = {
  install: (app: App) => {/* implementation */}
};
```

## Usage

1. **Install the Plugin**: Import and use `TranslationPlugin` in your main Vue app file.

    ```typescript
    import { createApp } from 'vue';
    import App from './App.vue';
    import { TranslationPlugin } from './path/to/TranslationPlugin';

    const app = createApp(App);

    app.use(TranslationPlugin);

    app.mount('#app');
    ```

2. **Translate Text in Components**: Use the `$translate` method available globally on the Vue instance.

    ```html
    <template>
      <p>{{ $translate('path.to.translation.key') }}</p>
    </template>
    ```

3. **Change Language**: Use the `$setLanguage` method to change the application's current language.

    ```javascript
    this.$setLanguage('de');
    ```

## Concluding Remarks

The `TranslationPlugin.ts` provides a seamless way to add multi-language support to Vue applications, promoting global accessibility and enhancing user experiences across different locales. 🌐✨