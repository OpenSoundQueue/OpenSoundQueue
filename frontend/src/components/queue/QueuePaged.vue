<template>
  <div class="queue-wrapper">
    <div class="queue-container" :class="!isLoading && !queuePage.length?'empty-queue':''">
      <div v-if="isLoading" class="skeleton-container">
        <EntrySkeleton v-for="(index) in pageSize" :key="index"/>
      </div>
      <div v-else-if="!queuePage.length" class="empty-queue">{{ $translate('queueEmpty') }}</div>
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
    <div v-if="numberOfQueuePages > 1 && (!isLoading && queuePage.length)">
      <PageSelector @select-page="(pageNumber) => selectNewPage(pageNumber)" :page-count="numberOfQueuePages"
                    :selectable-pages-count="numberOfQueuePages > 5 ? 5 : numberOfQueuePages"/>
    </div>
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

const props = defineProps<{
  pageUpdateInterval: number
}>();

const httpService = new HttpService();
const queuePage: Ref<Array<{ numberInQueue: number, song: Song }>> = ref([]);
const numberOfQueuePages = ref(5);
const queuePageIsLoading = ref(false);
const currentPage = ref(0);
const pageSize = 10;
const isLoading = ref(true);

onMounted(() => {
  selectNewPage(currentPage.value);

  setInterval(() => reloadPage(), props.pageUpdateInterval);
})

function selectNewPage(pageNumber: number) {
  queuePageIsLoading.value = true;
  currentPage.value = pageNumber;

  requestPage(pageNumber).then(() => {
    queuePageIsLoading.value = false;
  });
}

function reloadPage() {
  requestPage(currentPage.value);
}

async function requestPage(pageNumber: number) {
  return httpService.getQueuePage(pageNumber, pageSize)
      .then((data) => {
        isLoading.value = false;
        console.log(isLoading.value)
        queuePage.value = data.page;
        numberOfQueuePages.value = data.numberOfPages;
      });
}
</script>

<style scoped>
.queue-wrapper {
  width: 100%;
  font-size: var(--font-size-medium);
}

.queue-container {
  min-height: 480px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 10px;
}

.skeleton-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.empty-queue{
  margin: 0 auto 0 auto;
  color: var(--tertiary-color);
}
</style>