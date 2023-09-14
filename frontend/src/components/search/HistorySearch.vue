<template>
  <div class="history-search-wrapper">
    <InputField :icon-path="resolveFilePath('/icons/input/search.svg')"
                input-type="text"
                v-model="searchTerm"
                @user-input="data => processChange(data)"
                placeholder="Search in History"
    />
    <div class="result-container">
      <div v-for="(result, index) in searchResults" :key="index" class="search-result">
        <img :src="resolveFilePath('/icons/input/search.svg')"/>
        <div class="song-data">
          <Entry :title="result.title" :artist="result.artist" :duration="result.duration"></Entry>
        </div>
        <div class="add-to-queue-button">
          <DefaultButton text="" :icon-path="resolveFilePath('/icons/music/playlist_add.svg')" icon-alt=""/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {resolveFilePath} from "@/services/urlService";
import InputField from "@/components/inputs/InputField.vue";
import type {Ref} from "vue";
import {ref} from "vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import Entry from "@/components/queue/Entry.vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";

const maxSearchResults = 20;

const httpService = new HttpService();
const searchTerm = ref("");

const searchResults: Ref<Song[]> = ref([]);

const processChange = debounce(() => {
  if (!searchTerm.value) {
    searchResults.value = [];

    return;
  }

  searchHistory(searchTerm.value);
});

function searchHistory(searchTerm: string) {
  httpService.getSearchHistory(searchTerm, maxSearchResults)
      .then((data: Song[]) => searchResults.value = data);
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
.history-search-wrapper {
  height: 250px;
}

.result-container {
  overflow-y: scroll;
  height: 100%;
  margin-top: 10px;
  padding-left: 5px;
}

.search-result {
  display: flex;
  align-items: center;
  gap: 10px;
  height: 50px;
}

.search-result img {
  height: 20px;
}

.search-result .add-to-queue-button {
  padding-right: 10px;
  margin-left: auto;
}

.search-result .song-data {
  width: 100%;
  overflow: hidden;
}
</style>