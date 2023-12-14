<template>
  <div class="result-wrapper scrollbar">
    <div v-if="isLoading" class="loading-container">
      <LoadingAnimation :duration="1000" :dot-count="4" size="medium"/>
    </div>
    <div v-else-if="Object.keys(searchResults).length > 0" class="result-container">
      <div v-for="(result, index) in searchResults" :key="index" class="search-result">
        <img src="../../assets/icons/input/search.svg" :alt="$translate('altTexts.search')"/>
        <div class="song-data">
          <Entry :title="result.title" :artist="result.artist" :duration="result.duration"></Entry>
        </div>
        <div class="add-to-queue-button">
          <DefaultButton @click="addSong(result.link)" :is-disabled="queueAddIsDisabled" text="">
            <img src="../../assets/icons/music/playlist_add.svg" :alt="$translate('altTexts.playlistAdd')">
          </DefaultButton>
        </div>
      </div>
    </div>
    <div v-else class="error-container">
      <p>
        {{ searchTerm ? `${translate("historySearch.noSongFound")} "${searchTerm}"` : translate("historySearch.enterSearchTerm") }}
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import type {Ref} from "vue";
import {ref, watch} from "vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import Entry from "@/components/queue/Entry.vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";
import LoadingAnimation from "@/components/LoadingAnimation.vue";
import * as cookieService from "@/services/cookieService";

const props = defineProps<{
  searchTerm: string
}>();

const emit = defineEmits<{
    addSong: []
}>();

const maxSearchResults = 20;

const httpService = new HttpService();

const searchResults: Ref<Song[]> = ref([]);

const queueAddIsDisabled = ref(false);

const isLoading = ref(false);

watch(() => props.searchTerm, (newValue) => {
  processChange();

  if (newValue) {
    isLoading.value = true;
    return;
  }

  isLoading.value = false;
});

const processChange = debounce(() => {
  if (!props.searchTerm) {
    searchResults.value = [];

    return;
  }

  searchHistory(props.searchTerm);
});

function searchHistory(searchTerm: string) {
  httpService.getSearchHistory(searchTerm, maxSearchResults, cookieService.getApiKey())
      .then((data: Song[]) => {
        searchResults.value = data
        isLoading.value = false;
      })
      .catch(() => {
        ToastService.sendNotification("Could not search for songs.", "error", 3000);
        isLoading.value = false;
      });
}

function addSong(link?: string) {
  if (!link) {
    return;
  }

  queueAddIsDisabled.value = true;

  httpService.postQueueAdd(link, cookieService.getApiKey())
      .then((data: Song) => {
        queueAddIsDisabled.value = false;
        ToastService.sendNotification(
            `"${data.title}" ${translate("notifications.queueAddSuccess")}`,
            "success",
            3000
        );
        emit("addSong");
      })
      .catch(() => {
        queueAddIsDisabled.value = false
        ToastService.sendNotification(
            translate("notifications.queueAddError"),
            "error",
            3000
        );
      });
}

function debounce<T extends Function>(func: T, timeout: number = 300): (...args: unknown[]) => void {
  let timer: number | null = null;

  return function (this: ThisParameterType<T>, ...args: unknown[]): void {
    if (timer !== null) {
      clearTimeout(timer);
    }

    timer = window.setTimeout(() => {
      func.apply(this, args);
    }, timeout);
  };
}
</script>

<style scoped>
.result-wrapper {
  overflow-y: scroll;
  height: 100%;
  padding-left: 5px;
  display: flex;
  flex-direction: column;
}

.result-container {
  width: 100%;
}

.loading-container {
  margin: auto;
  width: 100px;
  height: 50px;
}

.error-container {
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.error-container p {
  text-align: center;
  font-size: var(--font-size-medium);
  color: var(--text-color);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.search-result {
  display: flex;
  align-items: center;
  gap: 10px;
  height: 50px;
  width: 100%;
}

.search-result img {
  height: 20px;
}

.search-result .add-to-queue-button {
  padding-right: 10px;
  margin-left: auto;
  width: 100px;
}

.search-result .song-data {
  width: 100%;
  overflow: hidden;
}
</style>