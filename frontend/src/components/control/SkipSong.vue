<template>
  <div class="skip-song" @click="isLoading?{}:skipSong()" :class="isClicked?'animate':''">
    <img src="@/assets/icons/music/skip.svg" alt="skip icon">
  </div>
</template>

<script setup lang="ts">
import * as cookieService from "@/services/cookieService";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";
import {HttpService} from "@/services/HttpService";
import {ref} from "vue";

const httpService = new HttpService();
const isClicked = ref(false);
const isLoading = ref(false);

async function skipSong() {
  isClicked.value = true;
  isLoading.value = true;
  await httpService.postSkip(cookieService.getApiKey())
      .then(() => {
        ToastService.sendNotification(translate("notifications.skipSongSuccess"), "success", 3000);
      })
      .catch(() => {
        ToastService.sendNotification(translate("notifications.skipSongError"), "error", 3000);
      });
  await new Promise((resolve) => setTimeout(resolve, 200));
  isClicked.value = false;
  isLoading.value = false;
}
</script>

<style scoped>

.skip-song {
  user-select: none;
  display: flex;
  align-items: center;
}

img {
  width: 15px;
}

.skip-song:hover {
  cursor: pointer;
}

.animate {
  animation: shrink 0.2s ease-in-out;
}

@keyframes shrink {
  0% {
    transform: scale(1);
  }

  25% {
    transform: scale(0.75);
  }

  100% {
    transform: scale(1);
  }
}
</style>