<template>
  <div class="queue-scroll-wrapper scrollbar">
    <div v-if="queueIsLoading" class="entry-container">
      <div v-for="(index) in 10" :key="index">
        <EntrySkeleton/>
      </div>
    </div>
    <div v-else-if="queue.length===0" class="empty-queue">
      <div>{{ $translate('queueEmpty') }}</div>
    </div>
    <div v-else-if="hasReorder">
      <draggable
          tag="ul"
          :list="queue"
          class="queue-reorder-container"
          handle=".handle"
          item-key="name"
          v-bind="dragOptions"
          @end="endDrag"
          :component-data="{
          tag: 'ul',
          type: 'transition-group',
          name: !drag ? 'flip-list' : null
        }"
      >
        <template #item="{ element }">
          <li @mousedown="startDrag(element.numberInQueue)" class="queue-reorder-item">
            <span class="entry">
              <Entry :number-in-queue="element.numberInQueue"
                     :title="element.song.title"
                     :artist="element.song.artist"
                     :duration="element.song.duration"
              />
            </span>
            <span class="fa fa-align-justify handle">
              <img src="@/assets/icons/drag_handle.svg" :alt="$translate('altTexts.dragHandle')"/>
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
import {computed, onMounted, ref} from "vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import Entry from "@/components/queue/Entry.vue";
import EntrySkeleton from "@/components/queue/EntrySkeleton.vue";
import draggable from "vuedraggable";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";
import type {Ref} from "vue";

const props = defineProps<{
  updateInterval: number,
  hasReorder?: boolean
}>();

const httpService = new HttpService();

const drag = ref(false);

const queueIsLoading = ref(true);

const queue: Ref<Array<{
  numberInQueue: number,
  song: { title: string, artist: string, duration: number, link?: string }
}>> = ref([]);

const draggedElement = ref(0);

const dragOptions = computed(() => {
  return {
    animation: 200,
    disabled: false,
    ghostClass: "ghost"
  }
})

onMounted(() => {
  requestQueue();

  setInterval(requestQueue, props.updateInterval);
})

function requestQueue() {
  httpService.getQueueAll()
      .then((data: Array<{ numberInQueue: number, song: Song }>) => {
        queue.value = data;
        queueIsLoading.value = false;
      })
}

function startDrag(numberInQueue: number) {
  draggedElement.value = numberInQueue;
}

function endDrag() {
  for (let [index, entry] of queue.value.entries()) {
    if (entry.numberInQueue === draggedElement.value) {
      httpService.queueChangeOrder(entry.numberInQueue, index)
          .then(() => requestQueue())
          .catch(() => {
            ToastService.sendNotification(translate("notifications.queueChangeOrderError"), "error", 3000);
          });
    }
  }
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

  .flip-list-move {
    transition: transform 0.2s;
  }

  .no-move {
    transition: transform 0s;
  }
}

.empty-queue {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: var(--tertiary-color);
  font-weight: bold;
  font-size: var(--font-size-medium);
}
</style>