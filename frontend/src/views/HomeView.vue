<template>
  <main>
    <div v-for="song in queuePage">
      {{ song.title }}, {{ song.artist }}, {{ song.duration }}
    </div>
    <PageSelector @select-page="(pageNumber) => selectQueuePage(pageNumber)" :page-count="12" :selectable-pages-count="5"/>
  </main>
</template>

<script setup lang="ts">
import {ref} from "vue";
import PageSelector from "@/components/paging/PageSelector.vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import type {Ref} from "vue";

const httpService = new HttpService();
const queuePage: Ref<Song[]> = ref([]);

function selectQueuePage(pageNumber: number) {
  httpService.postQueue(pageNumber, 10)
      .then((data) => {
        queuePage.value = data
      });
}
</script>

<style>
</style>
