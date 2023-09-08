import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import {makeServer} from "@/mirage/server";
import {TranslationPlugin} from "@/plugins/TranslationPlugin";
import translations from "@/translations/en.json";

if (import.meta.env.DEV) {
    makeServer();
}

const app = createApp(App)

app.use(router)

app.use(TranslationPlugin, translations);

app.mount('#app')
