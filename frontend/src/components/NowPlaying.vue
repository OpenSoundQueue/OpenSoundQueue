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
  currentSong?: Song
  currentTime: number,
  progress: number
}>();

const isLoading = computed(() =>  {
  return !props.currentSong;
});

const getCurrentTime = computed(() => {
  return secondsToTimeString(props.currentTime);
});

const getDuration = computed(() => {
  if (props.currentSong) {
    return secondsToTimeString(props.currentSong.duration);
  } else {
    return "0:00";
  }
});

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