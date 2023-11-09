<template>
  <div class="skip-song" @click="skipSong">
    <img src="@/assets/icons/music/skip.svg" alt="skip icon">
  </div>
</template>

<script setup lang="ts">
import * as cookieService from "@/services/cookieService";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";
import {HttpService} from "@/services/HttpService";

const httpService = new HttpService();

async function skipSong() {
  await httpService.postSkip(cookieService.getApiKey())
      .then((data: []) => {
        ToastService.sendNotification(translate("notifications.skipSongSuccess"), "success", 3000);
      })
      .catch(() => {
        ToastService.sendNotification(translate("notifications.skipSongError"), "error", 3000);
      });
}
</script>

<style scoped>
.skip-song, img {
  width: 15px;
}

.skip-song:hover {
  cursor: pointer;
}
</style>