<template>
  <div class="queue-wrapper">
    <div class="queue-container">
      <div v-if="!queuePage.length">
        <EntrySkeleton v-for="(index) in pageSize" :key="index"/>
      </div>
      <div v-for="(songData, index) in queuePage" :key="index">
        <Entry v-if="!queuePageIsLoading"
               :number-in-queue="songData.numberInQueue"
               :title="songData.song.title"
               :artist="songData.song.artist"
               :duration="songData.song.duration"
        />
        <EntrySkeleton v-else/>
      </div>
    </div>
    <PageSelector @select-page="(pageNumber) => selectQueuePage(pageNumber)" :page-count="numberOfQueuePages"
                  :selectable-pages-count="numberOfQueuePages > 5 ? 5 : numberOfQueuePages"/>
  </div>
</template>

<script setup lang="ts">
import {HttpService} from "@/services/HttpService";
import {onMounted, ref} from "vue";
import {Song} from "@/models/Song";
import type {Ref} from "vue";
import Entry from "@/components/queue/Entry.vue";
import PageSelector from "@/components/paging/PageSelector.vue";
import EntrySkeleton from "@/components/queue/EntrySkeleton.vue";

const httpService = new HttpService();
const queuePage: Ref<Array<{ numberInQueue: number, song: Song }>> = ref([]);
const numberOfQueuePages = ref(5);
const queuePageIsLoading = ref(false);
const pageSize = 10;

onMounted(() => {
  selectQueuePage(0);
})

function selectQueuePage(pageNumber: number) {
  queuePageIsLoading.value = true;

  httpService.postQueuePage(pageNumber, pageSize)
      .then((data) => {
        queuePage.value = data.page;
        numberOfQueuePages.value = data.numberOfPages;
        queuePageIsLoading.value = false;
      });
}
</script>

<style scoped>
.queue-wrapper {
  width: 300px;
  font-size: 16px;
}

.queue-container {
  min-height: 445px;
  margin-bottom: 20px;
}
</style>