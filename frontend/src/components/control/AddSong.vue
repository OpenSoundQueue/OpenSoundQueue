<template>
  <div class="add-song-wrapper">
    <OverlayCollapse label="Add Song"
                     :is-collapsed="true"
    >
      <template #custom-icon>
        <img src="@/assets/icons/music/playlist_add.svg">
      </template>
      <template #content>
        <Tabs :tabs="[$translate('byLink'), $translate('bySearch')]">
          <template #tab-0>
            <div class="tab-wrapper">
              <InputField v-model="songLink" label="Song Link" input-type="text"/>
              <DefaultButton @click="addSong(songLink)" :is-disabled="waitingForResponse" text="Add to queue">
                <img src="@/assets/icons/music/playlist_add.svg">
              </DefaultButton>
            </div>
          </template>
          <template #tab-1>
            <div class="tab-wrapper">
              <HistorySearch/>
            </div>
          </template>
        </Tabs>
      </template>
    </OverlayCollapse>
  </div>
</template>

<script setup lang="ts">
import OverlayCollapse from "@/components/collapse/OverlayCollapse.vue";
import InputField from "@/components/inputs/InputField.vue";
import HistorySearch from "@/components/search/HistorySearch.vue";
import Tabs from "@/components/Tabs.vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import {HttpService} from "@/services/HttpService";
import {ref} from "vue";

const httpService = new HttpService();
const songLink = ref("");
const waitingForResponse = ref(false);

function addSong(link: string) {
  waitingForResponse.value = true;

  httpService.postQueueAdd(link)
      .then((data: { link: string }) => {
        waitingForResponse.value = false;
        console.log(data)
      })
      .catch(() => {
        waitingForResponse.value = false;
      });
}
</script>

<style scoped>
.tab-wrapper {
  margin: auto;
}
</style>