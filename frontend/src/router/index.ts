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
import RegistrationView from "@/views/RegistrationView.vue";
import RoleManagementView from "@/views/RoleManagementView.vue";
import {PermissionService} from "@/services/PermissionService";
import type {PermissionType} from "@/services/PermissionService";
import InstallationView from "@/views/InstallationView.vue";

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
                        requiresAuth: true,
                        requiresPermission: ["PAUSE_PLAY", "CHANGE_VOLUME", "CHANGE_ORDER", "DELETE_SONGS", "SKIP"]
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
            path: '/register',
            name: 'register',
            component: RegistrationView,
            meta: {
                requiresAuth: false
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
            path: "/installation",
            name: "installation",
            component: InstallationView,
            meta: {
                requiresAuth: false
            }
        },
        {
            path: '/admin',
            name: 'admin',
            children: [
                {
                    path: '',
                    name: 'redirect',
                    component: RoleManagementView,
                    meta: {
                        requiresAuth: true,
                        requiresPermission: ["MANAGE_ROLES","MANAGE_USER"]
                    }
                },
                {
                    path: 'roles',
                    name: 'roles',
                    component: RoleManagementView,
                    meta: {
                        requiresAuth: true,
                        requiresPermission: ["MANAGE_ROLES"]
                    }
                },
                {
                    path: 'roles/display/:roleId',
                    name: 'roles-display',
                    component: RoleManagementView,
                    props: true,
                    meta: {
                        requiresAuth: true,
                        requiresPermission: ["MANAGE_ROLES"]
                    }
                },
                {
                    path: 'roles/members/:roleId',
                    name: 'roles-members',
                    component: RoleManagementView,
                    props: true,
                    meta: {
                        requiresAuth: true,
                        requiresPermission: ["MANAGE_ROLES"]
                    }
                },
                {
                    path: 'roles/permissions/:roleId',
                    name: 'roles-permissions',
                    component: RoleManagementView,
                    props: true,
                    meta: {
                        requiresAuth: true,
                        requiresPermission: ["MANAGE_ROLES"]
                    }
                },
                {
                    path: 'users',
                    name: 'users',
                    component: UserManagementView,
                    meta: {
                        requiresAuth: true,
                        requiresPermission: ["MANAGE_USER"]
                    }
                }

            ]
        },
    ]
})


// runs on all path requests which have the meta-tag 'requiresAuth' set to 'true'
// checks if the stored sessionKey is valid
// if so the request is permitted, else the user gets redirected to the '/login' path

router.beforeEach(async (to, from, next) => {
    if (!to.matched.some(record => record.meta.requiresAuth)) {
        next();
        return;
    }

    await httpService.getVerifyApiKey(cookieService.getApiKey())
        .then(async () => {
            for (const record of to.matched) {

                if (record.meta.requiresPermission) {

                    await PermissionService.getPermissions();
                    const permissions = <PermissionType[]>record.meta.requiresPermission;

                    if (PermissionService.hasAnyPermission(permissions)) {
                        next();
                    } else {
                        next({
                            path: '/home'
                        })
                    }
                    return;
                }
            }
            next();
            return;
        })
        .catch(() => {
            cookieService.clearApiKey();
            next({
                path: '/login'
            })
        });
})

// runs on all path requests which have the meta-tag 'requiresAuth' set to 'true'
// checks if there is not sessionKey or the stored sessionKey is invalid
// if so the request is permitted, else the user gets redirected to the '/map' path
router.beforeEach(async (to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth)) {
        next();
        return;
    }

    if (!cookieService.getApiKey()) {
        next();
        return;
    }

    await httpService.getVerifyApiKey(cookieService.getApiKey())
        .then(async () => {
            PopUpService.openPopUp(translate("logout.callToAction"), translate("logout.buttonLabel"));

            const userAction = await PopUpService.waitForUserAction();

            if (userAction === "accepted") {
                await httpService.postLogout(cookieService.getApiKey())
                    .then(() => {
                        ToastService.sendNotification(translate('logout.success'), "success", 3000);

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
        });
})

export default router
