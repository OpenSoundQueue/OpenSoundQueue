<template>
  <div class="replay" @click="isLoading?{}:replaySong()" :class="isClicked?'rotate':''">
      <img src="@/assets/icons/arrows/replay.svg" :alt="$translate('altTexts.replay')">
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

async function replaySong() {
  isClicked.value = true;
  isLoading.value = true;
  await httpService.postReplay(cookieService.getApiKey())
      .catch(() => {
        ToastService.sendNotification(translate("notifications.replaySongError"), "error", 3000);
      });
  await new Promise((resolve) => setTimeout(resolve, 350));
  isClicked.value = false;
  isLoading.value = false;
}
</script>

<style scoped>

.replay {
  user-select: none;
  display: flex;
  align-items: center;
}

img {
  width: 27px;
  transform: scale(-1, 1);
}

.replay:hover  {
  cursor: pointer;
}

.rotate {
  animation: rotate 0.35s ease-in-out;
}

@keyframes rotate {
  0% {
    transform: rotate(0);
  }

  50% {
    transform: rotate(-90deg);
  }

  100% {
    transform: rotate(0);
  }
}
</style>