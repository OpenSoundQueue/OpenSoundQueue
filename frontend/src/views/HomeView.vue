<template>
  <main>
    <p>{{ currentSong?.title }}</p>
    <p>{{ currentSong?.artist }}</p>
    <p>{{ getCurrentTime }}</p>
    <input type="range" :value="progress" min="0" max="100" step="1">
    <p>{{ getDuration }}</p>
    <Collapse label="In Queue" :is-collapsed="false">
      <Queue></Queue>
    </Collapse>
  </main>
</template>

<script setup lang="ts">
import Queue from "@/components/queue/Queue.vue";
import Collapse from "@/components/Collapse.vue";
import {HttpService} from "@/services/HttpService";
import {computed, ref} from "vue";
import {Song} from "@/models/Song";

import type {Ref} from "vue";

const progress = ref(0);
const currentTime = ref(0);
const currentSong: Ref<Song | undefined> = ref();
const httpService = new HttpService();

setInterval(getTime, 1000);

const getCurrentTime = computed(() => {
  return secondsToTimeString(currentTime.value);
})

const getDuration = computed(() => {
  if (currentSong.value) {
    return secondsToTimeString(currentSong.value?.duration);
  } else {
    return "0:00";
  }
})

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

<style></style>
