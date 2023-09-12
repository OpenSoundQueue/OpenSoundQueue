<template>
  <InputField :icon-path="resolveFilePath('/icons/input/search.svg')"
              input-type="text"
              v-model="searchTerm"
              @user-input="data => processChange(data)"
  />
  <div v-for="(result, index) in searchResults" :key="index">
    <Entry :title="result.title" :artist="result.artist" :duration="result.duration"></Entry>
  </div>
</template>

<script setup lang="ts">
import {resolveFilePath} from "@/services/urlService";
import InputField from "@/components/inputs/InputField.vue";
import {ref} from "vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import type {Ref} from "vue";
import Entry from "@/components/queue/Entry.vue";

const maxSearchResults = 10;

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

  return function(this: ThisParameterType<T>, ...args: unknown[]): void {
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

</style>