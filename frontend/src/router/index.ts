import {createRouter, createWebHistory, RouterView} from 'vue-router'
import PublicView from '@/views/PublicView.vue'
import HomeView from "@/views/HomeView.vue";
import LoginView from "@/views/LoginView.vue";
import SettingsView from "@/views/SettingsView.vue";
import {HttpService} from "@/services/HttpService";
import UserManagementView from "@/views/UserManagementView.vue";
import * as cookieService from "@/services/cookieService";
import HomeViewModerator from "@/views/HomeViewAdvanced.vue";

const httpService = new HttpService();

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'public',
            component: PublicView,
            meta: {
                requiresNoCookie: true
            }
        },
        {
            path: '/home',
            name: 'home',
            children: [
                {
                    path: '',
                    name: 'default',
                    component: HomeView,
                    meta: {
                        requiresAuth: true
                    }
                },
                {
                    path: 'basic',
                    name: 'basic',
                    component: HomeView,
                    meta: {
                     requiresAuth: true
                    }
                },
                {
                    path: 'advanced',
                    name: 'advanced',
                    component: HomeViewModerator,
                    meta: {
                        requiresAuth: true
                    }
                }
            ],
        },
        {
            path: '/login/:entryCode',
            name: 'login-with-entryCode',
            component: LoginView,
            props: true
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView,
            meta: {
                requiresNoCookie: true
            }
        },
        {
            path: "/settings",
            name: "settings",
            component: SettingsView,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: "/admin/user-management",
            name: "user-management",
            component: UserManagementView,
            meta: {
                requiresAuth: true
            }
        }
    ]
})

// runs on all path requests which have the meta-tag 'requiresAuth' set to 'true'
// checks if the stored sessionKey is valid
// if so the request is permitted, else the user gets redirected to the '/login' path

router.beforeEach(async (to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
        await httpService.getVerifyApiKey(cookieService.getApiKey())
            .then(() => {
                next()
            })
            .catch(() => {
                cookieService.clearApiKey();
                next({
                    path: '/login'
                })
            })
    } else {
        next()
    }
})

// runs on all path requests which have the meta-tag 'requiresNoCookie' set to 'true'
// checks if there is not sessionKey or the stored sessionKey is invalid
// if so the request is permitted, else the user gets redirected to the '/map' path
router.beforeEach(async (to, from, next) => {
    if (to.matched.some(record => record.meta.requiresNoCookie)) {
        if (document.cookie.indexOf('sessionKey=') > -1) {
            await httpService.getVerifyApiKey(cookieService.getApiKey())
                .then(() => {
                    next({
                        path: '/home'
                    })
                })
                .catch(() => {
                    cookieService.clearApiKey();
                    next()
                })
        } else {
            next()
        }
    } else {
        next()
    }
})


export default router
