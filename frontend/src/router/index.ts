import {createRouter, createWebHistory} from 'vue-router'
import PublicView from '@/views/PublicView.vue'
import HomeView from "@/views/HomeView.vue";
import LoginView from "@/views/LoginView.vue";
import SettingsView from "@/views/SettingsView.vue";
import {HttpService} from "@/services/HttpService";
import UserManagementView from "@/views/UserManagementView.vue";
import * as cookieService from "@/services/cookieService";
import {PopUpService} from "@/services/PopUpService";
import {translate} from "@/plugins/TranslationPlugin";
import {ToastService} from "@/services/ToastService";

const httpService = new HttpService();

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'public',
            component: PublicView,
            meta: {
                requiresAuth: false
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
                    component: HomeView,
                    meta: {
                        requiresAuth: true
                    }
                }
            ],
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView,
            props: false,
            meta: {
                requiresAuth: false
            }
        },
        {
            path: '/login/:entryCode',
            name: 'login-with-entryCode',
            component: LoginView,
            props: true
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

// runs on all path requests which have the meta-tag 'requiresNoAuth' set to 'true'
// checks if there is not sessionKey or the stored sessionKey is invalid
// if so the request is permitted, else the user gets redirected to the '/map' path
router.beforeEach(async (to, from, next) => {
    if (!to.matched.some(record => record.meta.requiresAuth)) {
        if (document.cookie.indexOf('sessionKey=') > -1) {
            await httpService.getVerifyApiKey(cookieService.getApiKey())
                .then(async () => {
                    PopUpService.openPopUp(translate("logout.callToAction"), translate("logout.buttonLabel"));

                    const userAction = await PopUpService.waitForUserAction();

                    if (userAction === "accepted") {
                        await httpService.postLogout(cookieService.getApiKey())
                            .then(() => {
                                cookieService.clearApiKey();
                                next();
                            })
                            .catch(() => {
                                ToastService.sendNotification(translate('logout.error'), "error", 3000);
                                if (from.name) {
                                    next(false);
                                } else {
                                    next({path: "/home"});
                                }
                            });
                    } else {
                        if (from.name) {
                            next(false);
                        } else {
                            next({path: "/home"});
                        }
                    }
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
