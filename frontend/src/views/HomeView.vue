<template>
  <main>
    <div class="queue-wrapper">
      <div v-for="(songData, index) in queuePage" :key="index">
        {{ songData.numberInQueue + 1 }}, {{ songData.song.title }}, {{ songData.song.artist }}, {{ songData.song.duration }}
      </div>
    </div>
    <PageSelector @select-page="(pageNumber) => selectQueuePage(pageNumber)" :page-count="numberOfQueuePages" :selectable-pages-count="5"/>
  </main>
</template>

<script setup lang="ts">
import {ref} from "vue";
import PageSelector from "@/components/paging/PageSelector.vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import type {Ref} from "vue";

const httpService = new HttpService();
const queuePage: Ref<Array<{numberInQueue: number, song: Song}>> = ref([]);
const numberOfQueuePages = ref(5);

function selectQueuePage(pageNumber: number) {
  httpService.postQueuePage(pageNumber, 10)
      .then((data) => {
        queuePage.value = data.page;
        numberOfQueuePages.value = data.numberOfPages;
      });
}
</script>

<style>
</style>
