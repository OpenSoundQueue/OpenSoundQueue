import {createRouter, createWebHistory} from "vue-router"
import PublicView from "@/views/PublicView.vue"
import HomeView from "@/views/HomeView.vue";
import SettingsView from "@/views/SettingsView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "public",
            component: PublicView
        },
        {
            path: "/home",
            name: "home",
            component: HomeView
        },
        {
            path: "/settings",
            name: "settings",
            component: SettingsView
        }
    ]
})

export default router
