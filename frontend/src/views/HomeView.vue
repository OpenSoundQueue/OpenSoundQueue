<template>
  <main>
    <div class="queue-wrapper">
      <div class="queue-container">
        <div v-for="(songData, index) in queuePage" :key="index" class="song-container">
          <div class="queue-number">{{ songData.numberInQueue + 1 }}</div>
          <div class="song-details-container">
            <div class="song-title">
              {{ songData.song.title }}
            </div>
            <div class="song-artist">
              {{ songData.song.artist }}
            </div>
          </div>
          <div class="song-duration">
            {{ getDurationString(songData.song.duration) }}
          </div>
        </div>
      </div>
      <PageSelector @select-page="(pageNumber) => selectQueuePage(pageNumber)" :page-count="numberOfQueuePages"
                    :selectable-pages-count="5"/>
    </div>
  </main>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import PageSelector from "@/components/paging/PageSelector.vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import type {Ref} from "vue";

const httpService = new HttpService();
const queuePage: Ref<Array<{ numberInQueue: number, song: Song }>> = ref([]);
const numberOfQueuePages = ref(5);

onMounted(() => {
  selectQueuePage(0);
})

function selectQueuePage(pageNumber: number) {
  httpService.postQueuePage(pageNumber, 10)
      .then((data) => {
        queuePage.value = data.page;
        numberOfQueuePages.value = data.numberOfPages;
      });
}

function getDurationString(durationInSeconds: number): string {
  return `${Math.floor(durationInSeconds / 60) ?? 0}:${durationInSeconds % 60 < 10 ? "00" : durationInSeconds % 60}`
}
</script>

<style>
.queue-wrapper {
  width: 300px;
  margin: auto;
  font-size: 16px;
}

.queue-container {
  min-height: 440px;
  margin-bottom: 20px;
}

.song-container {
  width: 100%;
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.queue-number {
  font-size: 16px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 28px;
}

.song-details-container {
  width: 90%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.song-artist {
  width: 100%;
  margin-top: 3px;
  font-size: 11px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.song-title {
  width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.song-duration {
  margin-left: auto;
  font-size: 11px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
</style>
