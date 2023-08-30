<template>
  <main>
    <p>{{ currentSong?.title }}</p>
    <p>{{ currentSong?.artist }}</p>
    <input type="range" :value="progress" min="0" max="100" step="1">
    <Collapse label="In Queue" :is-collapsed="false">
      <Queue></Queue>
    </Collapse>
  </main>
</template>

<script setup lang="ts">
import Queue from "@/components/queue/Queue.vue";
import Collapse from "@/components/Collapse.vue";
import {HttpService} from "@/services/HttpService";
import {ref} from "vue";
import {Song} from "@/models/Song";

import type {Ref} from "vue";

const progress = ref(0);
const currentSong: Ref<Song | undefined> = ref();
const httpService = new HttpService();

setInterval(getTime, 1000);


function getTime() {
  httpService.getQueueCurrent().then(data => {
    currentSong.value = data.song;

    progress.value = (data.time + Date.now() - data.stamp) / 10 / data.song.duration;
  })
}

</script>

<style></style>
