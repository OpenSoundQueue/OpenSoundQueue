<template>
  <div class="now-playing-wrapper">
    <div class="title-artist-wrapper">
      <div class="title-container">
        <div v-if="currentSong" class="title">{{ currentSong?.title }}</div>
        <div v-else class="skeleton"></div>
      </div>
      <div class="artist-container">
        <div v-if="currentSong" class="artist">{{ currentSong?.artist }}</div>
        <div v-else class="skeleton"></div>
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

const progress = ref(0);
const currentTime = ref(0);
const currentSong: Ref<Song | undefined> = ref();
const httpService = new HttpService();

onMounted(() => {
  setInterval(getTime, 1000);
});

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

function getTime() {
  httpService.getQueueCurrent().then(data => {
    currentSong.value = data.song;

    currentTime.value = (data.time + Date.now() - data.stamp) / 1000;
    progress.value = (data.time + Date.now() - data.stamp) / 10 / data.song.duration;
  })
}

function secondsToTimeString(time: number): string {
  const minutes = Math.floor(time / 60);
  const seconds = Math.floor(time % 60);
  return `${minutes}:${seconds < 10 ? "0" + seconds : seconds}`;
}
</script>

<style scoped>
.title-artist-wrapper {
  margin: 10px 0 10px 0;
}

.title-container .title {
  height: 30px;
  font-size: var(--font-size-big);
  margin-bottom: 5px;
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
}

.artist-container .skeleton {
  height: 20px;
  width: 50%;
  margin-bottom: 20px;
}
</style>