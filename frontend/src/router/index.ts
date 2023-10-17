import { createRouter, createWebHistory } from 'vue-router'
import PublicView from '@/views/PublicView.vue'
import HomeView from "@/views/HomeView.vue";
import LoginView from "@/views/LoginView.vue";
import SettingsView from "@/views/SettingsView.vue";
import {HttpService} from "@/services/HttpService";

const httpService = new HttpService();

// gets the value of a cookie by name
// if the cookie doesn't exist, the function returns 'false'
function getCookie(cname:string):string {
  let name = cname + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(';');
  for (let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) === ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) === 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

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
      component: HomeView,
      meta: {
        requiresAuth: true
      }
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
    }
  ]
})

// runs on all path requests which have the meta-tag 'requiresAuth' set to 'true'
// checks if the stored sessionKey is valid
// if so the request is permitted, else the user gets redirected to the '/login' path

router.beforeEach(async (to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    await httpService.getVerifyApiKey(getCookie("sessionKey"))
        .then(() => {
          next()
        })
        .catch(() => {
          document.cookie = "sessionKey= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
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
        await httpService.getVerifyApiKey(getCookie("sessionKey"))
            .then(() => {
                next({
                    path: '/home'
                })
            })
            .catch(() => {
                document.cookie = "sessionKey= ; expires = Thu, 01 Jan 1970 00:00:00 GMT"
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
