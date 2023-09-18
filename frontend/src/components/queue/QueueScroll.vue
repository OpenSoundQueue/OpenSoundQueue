<template>
  <div class="queue-scroll-wrapper scrollbar">
    <div v-if="!queue.length">
      <div v-for="(index) in 15" :key="index">
        <EntrySkeleton/>
      </div>
    </div>
    <div v-else>
      <div v-for="(songData, index) in queue" :key="index">
        <Entry :number-in-queue="songData.numberInQueue"
               :title="songData.song.title"
               :artist="songData.song.artist"
               :duration="songData.song.duration"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted} from "vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import type {Ref} from "vue";
import Entry from "@/components/queue/Entry.vue";
import EntrySkeleton from "@/components/queue/EntrySkeleton.vue";

const props = defineProps<{
  updateInterval: number
}>();

const httpService = new HttpService();
const queue: Ref<Array<{ numberInQueue: number, song: Song }>> = ref([]);

onMounted(() => {
  requestQueue();

  setInterval(requestQueue, props.updateInterval);
})

function requestQueue() {
  httpService.getQueueAll()
      .then((data: Array<{ numberInQueue: number, song: Song }>) => {
        queue.value = data;
      })
}
</script>

<style scoped>
.queue-scroll-wrapper {
  padding: 0 20px 0 10px;
  overflow-y: scroll;
  height: 100%;
}
</style>