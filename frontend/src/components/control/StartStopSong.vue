<template>
  <div class="skip-song" @click="isLoading?{}:startStop()" :class="isClicked?'animate':''">
    <img class="play-icon" v-show="!isPlaying" src="@/assets/icons/music/play.svg" :alt="$translate('altTexts.play')">
    <img class="stop-icon" v-show="isPlaying" src="@/assets/icons/music/pause.svg" :alt="$translate('altTexts.pause')">
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
const isPlaying = ref(true);

async function startStop() {
  isClicked.value = true;
  isLoading.value = true;
  if (isPlaying.value){
    await httpService.postStop(cookieService.getApiKey())
        .then(() => {
          isPlaying.value = false;
        })
        .catch(() => {
          ToastService.sendNotification(translate("notifications.stopError"), "error", 3000);
        });
  }else {
    await httpService.postStart(cookieService.getApiKey())
        .then(() => {
          isPlaying.value = true;
        })
        .catch(() => {
          ToastService.sendNotification(translate("notifications.startError"), "error", 3000);
        });
  }

  await new Promise((resolve) => setTimeout(resolve, 200));
  isClicked.value = false;
  isLoading.value = false;
}
</script>

<style scoped>

.skip-song {
  user-select: none;
  position: relative;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--primary-color);
  display: flex;
  justify-content: center;
  align-items: center;
  transition-property: left;
  transition-duration: .2s;
}

img {
  width: 15px;
}

.play-icon{
  padding-left: 3px;
}

.stop-icon{
  width: 12px;
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