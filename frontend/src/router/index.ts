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
import ApplicationSettingsView from "@/views/ApplicationSettingsView.vue";

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
            redirect: "/installation/language",
            children: [
                {
                    path: "language",
                    name: "language",
                    component: InstallationView,
                },
                {
                    path: "register",
                    name: "registration",
                    component: InstallationView,
                },
                {
                    path: "privacy",
                    name: "privacy",
                    component: InstallationView,
                },
                {
                    path: "sources",
                    name: "sources",
                    component: InstallationView,
                }
            ]
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
                        requiresPermission: ["MANAGE_ROLES", "MANAGE_USER"]
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
                },
                {
                    path: 'application-settings',
                    name: 'application-settings',
                    component: ApplicationSettingsView,
                    meta: {
                        requiresAuth: true,
                        requiresPermission: ["MANAGE_SYSTEM_SETTINGS"]
                    }
                }
            ]
        },
    ]
})

// This router guard executes before each navigation, ensuring proper installation state handling
router.beforeEach(async (to, from, next) => {
    // Fetches the installation state from the server
    await httpService.getInstallationState()
        .then(data => {
            // Extracts the 'finished' property from the response data and checks if it's true or false
            const finished: boolean = data.finished == "true";
            // Checks if the current route is an installation path
            const isInstallationPath = to.matched.some(record => record.path.includes("installation"));

            // If the installation is not finished
            if (!finished) {
                // If the current path is the installation path, continue to the next navigation step
                if (isInstallationPath) {
                    next();
                    return;
                } else {
                    // Redirects to the installation path if not already there
                    next({
                        path: '/installation'
                    })
                    return;
                }
            } else {
                // If the installation is finished
                if (isInstallationPath) {
                    // Redirects to the home path if currently on the installation path
                    next({
                        path: '/home'
                    })
                    return;
                } else {
                    // Proceeds with the navigation if not on the installation path
                    next();
                    return;
                }
            }

        })
        // Handles errors that occur during the installation state fetching process
        .catch(() => {
            // Clears the API key cookie
            cookieService.clearApiKey();
            // Redirects to the installation path
            next({
                path: '/installation'
            })
        });
})

// This router guard runs on all path requests that require authentication ('requiresAuth' meta-tag set to 'true').
// It checks if the stored sessionKey (API key) is valid by calling the 'getVerifyApiKey' method from 'httpService'.
// If the sessionKey is valid, the request is permitted. Otherwise, the user gets redirected to the '/login' path.

router.beforeEach(async (to, from, next) => {
    // If the route does not require authentication, proceed with the navigation
    if (!to.matched.some(record => record.meta.requiresAuth)) {
        next();
        return;
    }

    // Verifies the API key stored in the cookie
    await httpService.getVerifyApiKey(cookieService.getApiKey())
        .then(async () => {
            // Checks if any specific permissions are required for the route
            for (const record of to.matched) {
                if (record.meta.requiresPermission) {
                    // Retrieves and verifies the user's permissions
                    await PermissionService.getPermissions();
                    const permissions = <PermissionType[]>record.meta.requiresPermission;

                    // If the user has any of the required permissions, permit the request
                    if (PermissionService.hasAnyPermission(permissions)) {
                        next();
                    } else {
                        // Redirects to the home path if the user lacks required permissions
                        next({
                            path: '/home'
                        })
                    }
                    return;
                }
            }
            // Proceeds with the navigation if no permissions are required
            next();
            return;
        })
        // Handles errors that occur during the API key verification process
        .catch(() => {
            // Clears the API key cookie
            cookieService.clearApiKey();
            // Redirects to the login path
            next({
                path: '/login'
            })
        });
})

// This router guard runs on all path requests which have the meta-tag 'requiresAuth' set to 'true'.
// It checks if there is no sessionKey or the stored sessionKey is invalid by calling the 'getVerifyApiKey' method from 'httpService'.
// If there is no sessionKey or it is invalid, the request is permitted. Otherwise, the user gets redirected to the '/map' path.

router.beforeEach(async (to, from, next) => {
    // If the route meta-tag is empty, proceed with the navigation
    if (Object.keys(to.meta).length == 0) {
        next();
        return;
    }

    // If the route requires authentication, proceed with the navigation
    if (to.matched.some(record => record.meta.requiresAuth)) {
        next();
        return;
    }

    // If there is no stored sessionKey, proceed with the navigation
    if (!cookieService.getApiKey()) {
        next();
        return;
    }

    // Verifies the API key stored in the cookie
    await httpService.getVerifyApiKey(cookieService.getApiKey())
        .then(async () => {
            // Opens a pop-up dialog prompting the user to confirm logout
            PopUpService.openPopUp(translate("logout.callToAction"), translate("logout.buttonLabel"));

            // Waits for the user's action on the pop-up dialog
            const userAction = await PopUpService.waitForUserAction();

            if (userAction === "accepted") {
                // Logs out the user by sending a POST request to the logout endpoint
                await httpService.postLogout(cookieService.getApiKey())
                    .then(() => {
                        // Sends a notification of successful logout
                        ToastService.sendNotification(translate('logout.success'), "success", 3000);

                        next();
                    })
                    .catch(() => {
                        // Sends a notification of logout error
                        ToastService.sendNotification(translate('logout.error'), "error", 3000);
                        if (from.name) {
                            next(false);
                        } else {
                            next({ path: "/home" });
                        }
                    });
            } else {
                if (from.name) {
                    next(false);
                } else {
                    next({ path: "/home" });
                }
            }
        })
        // Handles errors that occur during the API key verification process
        .catch(() => {
            // Clears the API key cookie
            cookieService.clearApiKey();
            // Proceeds with the navigation
            next()
        });
})

export default router
