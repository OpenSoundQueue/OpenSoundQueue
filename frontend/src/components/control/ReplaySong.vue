<template>
<div class="replay" @click="replaySong">
  <img src="@/assets/icons/arrows/replay.svg" alt="skip icon">
</div>
</template>

<script setup lang="ts">
import * as cookieService from "@/services/cookieService";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";
import {HttpService} from "@/services/HttpService";

const httpService = new HttpService();
async function replaySong() {
  await httpService.postReplay(cookieService.getApiKey())
      .catch(() => {
        ToastService.sendNotification(translate("notifications.replaySongError"), "error", 3000);
      });
}
</script>

<style scoped>

.replay, img {
  width: 27px;
}

.replay:hover {
  cursor: pointer;
}
</style>