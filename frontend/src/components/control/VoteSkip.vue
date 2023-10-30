<template>
  <div @click="activate" class="wrapper">
    <div class="slider" :class="[voteSkipData.isActive ? 'active' : 'inactive']">
      <div class="button-label-container">
        <div class="button unselectable" :class="[voteSkipData.isActive ? 'active' : 'inactive']">
          <img src="@/assets/icons/music/skip.svg" alt="skip icon">
        </div>
        <div class="label unselectable" :class="[voteSkipData.isActive ? 'active' : 'inactive']">
          {{ voteSkipData.received }} / {{ voteSkipData.required }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {HttpService} from "@/services/HttpService";
import {ToastService} from "@/services/ToastService";
import * as cookieService from "@/services/cookieService";
import {translate} from "@/plugins/TranslationPlugin";

type VoteSkipDto = {
  isActive: boolean,
  required: number,
  received: number
}

const props = defineProps<{
  updateInterval: number
}>();

const httpService = new HttpService();

const voteSkipData = ref({
  isActive: false,
  required: 0,
  received: 0
});

let interval: any;

onMounted(() => {
  requestStatus();
})

function requestStatus() {
  httpService.getVoteSkipStatus(cookieService.getApiKey())
      .then((data: VoteSkipDto) => {
        voteSkipData.value = data;
      })
}

function requestVote() {
  httpService.getVoteSkipVote(cookieService.getApiKey())
      .then((data: VoteSkipDto) => {
        voteSkipData.value = data;
        voteSkipData.value.received ++;
      })
      .catch(() => {
        ToastService.sendNotification(translate("notifications.voteSkipRequestError"), "error", 3000);
        voteSkipData.value.isActive = false
      });
}

function withdrawVote() {
  httpService.getVoteSkipWithdraw(cookieService.getApiKey())
      .then((data: VoteSkipDto) => {
        voteSkipData.value = data;
        voteSkipData.value.received --;
      })
      .catch(() => {
        ToastService.sendNotification(translate("notifications.voteSkipWithdrawError"), "error", 3000);
        voteSkipData.value.isActive = false
      });
}

function activate() {
  resetInterval();

  if (voteSkipData.value.isActive) {
    withdrawVote();
    voteSkipData.value.isActive = false;
    return;
  }

  requestVote();
  voteSkipData.value.isActive = true;
}

function resetInterval() {
  clearInterval(interval);

  interval = setInterval(() => {
    requestStatus()
  }, props.updateInterval);
}
</script>


<style scoped>

.wrapper {
  height: 70px;
  width: 100px;
  display: flex;
  align-items: center;
}

.slider {
  width: 100%;
  height: 30px;
  border-radius: 15px;
  display: flex;
  align-items: center;
}

.slider.inactive {
  background: var(--secondary-color);
}

.slider.active {
  background: var(--tertiary-color);
}

.button-label-container {
  width: 100%;
  display: flex;
  align-items: center;
}

.button {
  z-index: 1;
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

.button img {
  height: 15px;
}

.button.active {
  left: calc(100% - 40px);
}

.button.inactive {
  left: 0;
}

.label {
  width: calc(100% - 50px);
  text-align: center;
  transition-property: transform;
  transition-duration: .2s;
}

.label.active {
  transform: translateX(-30px);
  color: var(--background-color);
  font-weight: bold;
}


</style>