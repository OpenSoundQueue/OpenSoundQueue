<template>
  <div class="volume-changer-wrapper">

    <div class="volume-changer-container">
      <div class="icon-wrapper">
        <div class="icon-container">
          <img v-if="isMuted" @click="unmute" src="@/assets/icons/volume_mute.svg" :alt="$translate('altTexts.volumeOff')"/>
          <img v-else @click="mute" src="@/assets/icons/volume_full.svg" :alt="$translate('altTexts.volumeFull')"/>
        </div>
        <div class="icon-container" @click="volumeDown" >
          <img v-if="loading || volume === 0" src="@/assets/icons/volume_down_inactive.svg" :alt="$translate('altTexts.volumeDown')"/>
          <img v-else src="@/assets/icons/volume_down_active.svg" :alt="$translate('altTexts.volumeDown')"/>
        </div>
        <div class="icon-container" @click="volumeUp">
          <img v-if="loading || volume === 100" src="@/assets/icons/volume_up_inactive.svg" :alt="$translate('altTexts.volumeUp')"/>
          <img v-else src="@/assets/icons/volume_up_active.svg" @click="volumeUp" :alt="$translate('altTexts.volumeUp')"/>
        </div>
      </div>
      <div class="volume-changer-background">
        <div class="volume-status-bar" :style="{transform: `translateX(${volume - 100}%)`}"></div>
      </div>
    </div>
    <div class="volume-value-wrapper">
      {{ volume }}%
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, onUnmounted, ref} from "vue";
import {HttpService} from "@/services/HttpService";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";

const httpService = new HttpService();

const props = defineProps<{
  stepSize: number,
  updateInterval: number
}>()

const volume = ref(0);
const isMuted = ref(false);
const loading = ref(false);

let updateIntervalTimer: ReturnType<typeof setInterval> | undefined;

onMounted(() => {
  getVolume();
  updateIntervalTimer = setInterval(getVolume, props.updateInterval);
})

onUnmounted(() => {
  clearInterval(updateIntervalTimer);
})

function getVolume() {
  httpService.getVolume()
      .then((data: { volume: number, isMuted: boolean }) => {
        volume.value = data.volume;
        isMuted.value = data.isMuted;
      })
}

async function volumeUp() {
  if (volume.value === 100) {
    return;
  }

  if (loading.value) {
    return;
  }

  loading.value = true;

  await httpService.postVolume(volume.value + props.stepSize)
      .then((data: { volume: number, isMuted: boolean }) => {
        volume.value = data.volume;
        isMuted.value = data.isMuted;
      })
      .catch(() => {
        ToastService.sendNotification(translate("volume.changeError"), "error", 3000);
      });

  loading.value = false;
}

async function volumeDown() {
  if (volume.value === 0) {
    return;
  }

  if (loading.value) {
    return;
  }

  loading.value = true;

  await httpService.postVolume(volume.value - props.stepSize)
      .then((data: { volume: number, isMuted: boolean }) => {
        volume.value = data.volume;
        isMuted.value = data.isMuted;
      })
      .catch(() => {
        ToastService.sendNotification(translate("volume.changeError"), "error", 3000);
      });

  loading.value = false;
}

async function mute() {
  if (loading.value) {
    return;
  }

  loading.value = true;

  await httpService.postMute()
      .then((data: { volume: number, isMuted: boolean }) => {
        volume.value = data.volume;
        isMuted.value = data.isMuted;
      })
      .catch(() => {
        ToastService.sendNotification(translate("volume.muteError"), "error", 3000);
      });

  loading.value = false;
}

async function unmute() {
  if (loading.value) {
    return;
  }

  loading.value = true;

  await httpService.postUnmute()
      .then((data: { volume: number, isMuted: boolean }) => {
        volume.value = data.volume;
        isMuted.value = data.isMuted;
      })
      .catch(() => {
        ToastService.sendNotification(translate("volume.unmuteError"), "error", 3000);
      });

  loading.value = false;
}

</script>

<style scoped>
.volume-changer-wrapper {
  width: 160px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.icon-wrapper {
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 7px;
  box-sizing: border-box;
}

.icon-container {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon-container img:hover {
  cursor: pointer;
}

.volume-changer-container {
  position: relative;
  width: calc(100% - 50px);
  height: 100%;
  display: flex;
  align-items: center;
}

.volume-changer-background {
  overflow: hidden;
  background: var(--background-color);
  box-sizing: border-box;
  border: 3px solid var(--secondary-color);
  height: 100%;
  width: 100%;
  border-radius: 20px;
}

.volume-status-bar {
  background: var(--secondary-color);
  height: 100%;
  top: 0;
  transition: transform 0.1s linear;
}

.volume-value-wrapper {
  width: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>