<template>
  <div class="queue-scroll-wrapper">
    <div v-for="(songData, index) in queue" :key="index">
      <Entry :number-in-queue="songData.numberInQueue"
             :title="songData.song.title"
             :artist="songData.song.artist"
             :duration="songData.song.duration"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted} from "vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import type {Ref} from "vue";
import Entry from "@/components/queue/Entry.vue";

const httpService = new HttpService();
const queue: Ref<Array<{ numberInQueue: number, song: Song }>> = ref([]);

onMounted(() => {
  httpService.getQueueAll()
      .then((data: Array<{ numberInQueue: number, song: Song }>) => {
        console.log(data)
        queue.value = data;
      })
})
</script>

<style scoped>
.queue-scroll-wrapper {
  margin: 30px 0 20px 0;
  padding: 0 20px 0 10px;
  height: 50vh;
  overflow: scroll;
}
</style>