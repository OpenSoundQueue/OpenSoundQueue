<template>
  <div class="user-menu-wrapper">
    <div class="user-menu-container">
      <div class="close-container">
        <img @click="close" src="@/assets/menu/close.svg" :alt="$translate('altTexts.close')"/>
      </div>
      <div v-if="user">
        <p class="username">{{ user.username }}</p>
        <div class="button">
          <DefaultButton :is-disabled="waitingForResponse" text="Logout" @click="logout">
            <img src="@/assets/icons/logout.svg" :alt="$translate('altTexts.logout')"/>
          </DefaultButton>
        </div>
      </div>
      <div v-else>
        <p>{{ $translate('logout.info') }}</p>
        <div class="button">
          <DefaultButton text="Login" @click="router.push('/login')">
            <img src="@/assets/icons/login.svg" :alt="$translate('altTexts.login')"/>
          </DefaultButton>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import {HttpService} from "@/services/HttpService";
import type {Ref} from "vue";
import {onMounted, ref, watch} from "vue";
import type {User} from "@/models/User";
import router from "@/router";
import * as cookieService from "@/services/cookieService";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";

const httpService = new HttpService();

const user: Ref<User | null> = ref(null);

const waitingForResponse = ref(false);

const emit = defineEmits<{
  close: []
}>();

onMounted(() => {
  getSelf();
})

watch(router.currentRoute, () => {
  getSelf();
})

async function logout() {
  waitingForResponse.value = true;

  await httpService.postLogout(cookieService.getApiKey())
      .then(() => {
        ToastService.sendNotification(translate('logout.success'), "success", 3000);

        router.push("/");
      })
      .catch(() => {
        ToastService.sendNotification(translate('logout.error'), "error", 3000);
      });

  waitingForResponse.value = false;
  getSelf();
  close();
}

function getSelf() {
  httpService.getSelf()
      .then((data: User) => {
        user.value = data
      })
      .catch(() => {
        user.value = null;
      })
}

function close() {
  emit("close");
}
</script>

<style scoped>
.user-menu-wrapper {
  position: fixed;
  width: 100%;
  right: 0;
  top: 0;
  padding: 5px;
  box-sizing: border-box;
}

.user-menu-container {
  background: rgb(var(--secondary-color));
  border: 2px solid rgb(var(--tertiary-color));
  border-radius: var(--border-radius-big);
  display: flex;
  flex-direction: column;
  padding: 10px 20px 20px 20px;
  box-sizing: border-box;
}

.close-container {
  display: flex;
  justify-content: flex-end;
}

.username {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin: 5px 0;
}

.button {
  margin-top: 15px;
}

@media screen and (min-width: 400px) {
  .user-menu-wrapper {
    width: 400px;
  }
}
</style>