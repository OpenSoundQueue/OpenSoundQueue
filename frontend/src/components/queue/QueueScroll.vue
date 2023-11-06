<template>
  <div class="queue-scroll-wrapper scrollbar">
    <div v-if="!queue.length" class="entry-container">
      <div v-for="(index) in 10" :key="index">
        <EntrySkeleton/>
      </div>
    </div>
    <div v-else-if="hasReorder">
      <draggable
          tag="ul"
          :list="queue"
          class="queue-reorder-container"
          handle=".handle"
          item-key="name"
          ghost-class="ghost"
      >
        <template #item="{ element }">
          <li class="queue-reorder-item">
            <span class="entry">
              <Entry :number-in-queue="element.numberInQueue"
                     :title="element.song.title"
                     :artist="element.song.artist"
                     :duration="element.song.duration"
              />
            </span>
            <span class="fa fa-align-justify handle">
              <img src="@/assets/icons/drag_handle.svg"/>
            </span>
          </li>
        </template>
      </draggable>

    </div>
    <div v-else class="entry-container">
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
import {ref, onMounted, watch} from "vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import type {Ref} from "vue";
import Entry from "@/components/queue/Entry.vue";
import EntrySkeleton from "@/components/queue/EntrySkeleton.vue";
import draggable from "vuedraggable";

const props = defineProps<{
  updateInterval: number,
  hasReorder?: boolean
}>();

const httpService = new HttpService();
const queue: Ref<Array<{
  numberInQueue: number,
  song: { title: string, artist: string, duration: number, link?: string }
}>> = ref([]);

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
  padding: 0 20px 10px 20px;
  overflow-y: scroll;
  height: 100%;
  box-sizing: border-box;
}

.entry-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.queue-reorder-container {
  list-style-type: none;
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin: 0;
  padding: 0;

  .queue-reorder-item {
    display: flex;
    width: 100%;
    gap: 30px;

    .entry {
      width: 100%;
      overflow: hidden;
    }

    .handle {
      height: 39px;
      display: flex;
      justify-content: center;
      align-items: center;

      img {
        width: 40px;
      }
    }
  }

  .ghost {
    background: var(--primary-color);
    color: white;
    border-radius: var(--border-radius-small);
  }
}
</style>