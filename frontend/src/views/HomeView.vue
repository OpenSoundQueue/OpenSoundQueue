<template>
  <main>
    <OverlayCollapse label="Add Song"
                     :icon="resolveFilePath('/icons/music/playlist_add.svg')"
                     :is-collapsed="false"
    >
      <Tabs :tabs="[$translate('byLink'), $translate('bySearch')]">
        <template #tab-0>
          <div class="tab-wrapper">
            <InputField label="Song Link" input-type="text"/>
            <Button text="Add to queue" icon-path="" icon-alt=""/>
          </div>
        </template>
        <template #tab-1>
          <div class="tab-wrapper">
            <InputField :icon-path="resolveFilePath('/icons/input/search.svg')"
                        input-type="text"
                        v-model="searchTerm"
                        @user-input="data => processChange(data)"
            />
            <div v-for="(result, index) in searchResults" :key="index">{{ result.title }}</div>
          </div>
        </template>
      </Tabs>
    </OverlayCollapse>
    <VoteSkip :update-interval="4000"/>
  </main>
</template>

<script setup lang="ts">
import VoteSkip from "@/components/control/VoteSkip.vue";
import OverlayCollapse from "@/components/collapse/OverlayCollapse.vue";
import {resolveFilePath} from "@/services/urlService";
import Tabs from "@/components/Tabs.vue";
import InputField from "@/components/inputs/InputField.vue";
import Button from "@/components/buttons/Button.vue";
import {computed, onMounted, ref} from "vue";
import {HttpService} from "@/services/HttpService";
import {Song} from "@/models/Song";
import type {Ref} from "vue";

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
      .then((data) => searchResults.value = data);
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
.tab-wrapper {
  margin: auto;
}
</style>