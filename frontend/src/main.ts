import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import {makeServer} from "@/mirage/server";
import {TranslationPlugin} from "@/plugins/TranslationPlugin";
import translations from "@/translations/en.json";
import {ValidationPlugin} from "@/plugins/ValidationPlugin";
import Closable from "@/directives/ClosableDirective";

if (import.meta.env.DEV) {
    makeServer();
}

const app = createApp(App)

app.directive('closable', Closable())
app.directive('closableonmount', Closable(true))

app.use(router)

app.use(TranslationPlugin, translations);

app.use(ValidationPlugin)

app.mount('#app')
