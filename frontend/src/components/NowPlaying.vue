<template>
  <div class="now-playing-wrapper">
    <div class="title-artist-wrapper">
      <div class="title-container">
        <div v-if="isLoading" class="skeleton"></div>
        <div v-else-if="!currentSong">{{ $translate('currentSongEmpty') }}</div>
        <div v-else class="title">{{ currentSong?.title }}</div>
      </div>
      <div class="artist-container">
        <div v-if="isLoading" class="skeleton"></div>
        <div v-else-if="!currentSong" class="artist"/>
        <div v-else class="artist">{{ currentSong?.artist }}</div>
      </div>
    </div>
    <ProgressBar :label-left="getCurrentTime"
                 :label-right="getDuration"
                 :value="progress"
                 :min="0"
                 :max="100"
    />
  </div>
</template>

<script setup lang="ts">
import ProgressBar from "@/components/ProgressBar.vue";
import {HttpService} from "@/services/HttpService";
import {computed, onMounted, ref} from "vue";
import {Song} from "@/models/Song";

import type {Ref} from "vue";

const props = defineProps<{
  updateInterval: number
}>();

const progress = ref(0);
const currentTime = ref(0);
const currentSong: Ref<Song | undefined> = ref();
const isPlaying = ref(false);
const httpService = new HttpService();
const isLoading = ref(true);

const countdownDate = ref(0);

onMounted(() => {
  setInterval(getTime, props.updateInterval);
  setInterval(calculateProgress, 50);
  getTime();
});

function calculateProgress() {
  if (!currentSong.value) {
    return;
  }

  if (!isPlaying.value && progress.value && currentTime.value) {
    return;
  }

  progress.value = (1 - ((countdownDate.value - Date.now()) / 1000) / currentSong.value?.duration) * 100;
  currentTime.value = currentSong.value.duration - (countdownDate.value - Date.now()) / 1000;
}

const getCurrentTime = computed(() => {
  return secondsToTimeString(currentTime.value);
});

const getDuration = computed(() => {
  if (currentSong.value) {
    return secondsToTimeString(currentSong.value?.duration);
  } else {
    return "0:00";
  }
});

async function getTime() {
  await httpService.getNowPlaying().then(data => {
    isLoading.value = false;
    if (data.song) {
      currentSong.value = data.song;
      isPlaying.value = data.isPlaying;

      currentTime.value = (data.time + Date.now() - data.stamp) / 1000;

      countdownDate.value = Date.now() + (data.song.duration * 1000) - addTransmissionTime(data.time, data.stamp);
    }
  })
}

function addTransmissionTime(value: number, stampSender: number) {
  return value + Date.now() - stampSender;
}

function secondsToTimeString(time: number): string {
  const minutes = Math.floor(time / 60);
  const seconds = Math.floor(time % 60);
  return `${minutes}:${seconds < 10 ? "0" + seconds : seconds}`;
}
</script>

<style scoped>
.now-playing-wrapper {
  width: 100%;
}

.title-artist-wrapper {
  margin: 10px 0 10px 0;
}

.title-container .title {
  height: 30px;
  font-size: var(--font-size-big);
  margin-bottom: 5px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.title-container .skeleton {
  height: 30px;
  width: 30%;
  margin-bottom: 5px;
}

.artist-container .artist {
  height: 20px;
  font-size: var(--font-size-medium);
  margin-bottom: 20px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.artist-container .skeleton {
  height: 20px;
  width: 50%;
  margin-bottom: 20px;
}
</style>