import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import {makeServer} from "@/mirage/server";

if (import.meta.env.DEV) {
    makeServer();
}

const app = createApp(App)

app.use(router)

app.mount('#app')
