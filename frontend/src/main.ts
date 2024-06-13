import './assets/main.css';

import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { makeServer } from "@/mirage/server";
import { TranslationPlugin } from "@/plugins/TranslationPlugin";
import translations from "@/translations/en.json";
import { ValidationPlugin } from "@/plugins/ValidationPlugin";
import Closable from "@/directives/ClosableDirective";
import { createPinia } from 'pinia';

// Check if in development mode and create Mirage server if true
if (import.meta.env.DEV) {
    // makeServer();
}

// Create Vue app instance
const app = createApp(App);

// Create Pinia store
const pinia = createPinia();

// Register custom directives
app.directive('closable', Closable());
app.directive('closableonmount', Closable(true));

// Use Pinia store
app.use(pinia);

// Use Vue Router
app.use(router);

// Use translation plugin and provide translations
app.use(TranslationPlugin, translations);

// Use validation plugin
app.use(ValidationPlugin);

// Mount the app to the DOM
app.mount('#app');