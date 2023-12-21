<template>
  <div class="add-song-wrapper">
    <div class="add-song-container">
      <div class="add-song-label">
        <span>{{ $translate('addSong.title') }}</span>
        <InfoButton>{{ $translate('help.addSong') }}</InfoButton>
      </div>
      <InputField @click="openOverlay"
                  v-closable="{excluded: ['add-song-overlay'], handler: closeOverlay}"
                  :placeholder="$translate('addSong.placeholder')"
                  v-model="inputString"
                  ref="inputField"
                  :custom-icon="true"
      >
        <template #icon>
          <img v-if="showSearch" src="@/assets/icons/input/search.svg" :alt="$translate('altTexts.search')"/>
          <img v-else src="@/assets/icons/music/playlist_add.svg" :alt="$translate('altTexts.playlistAdd')">
        </template>
      </InputField>
    </div>
    <div class="add-song-overlay" :class="[showOverlay ? 'visible' : 'not-visible']">
      <div v-if="showSearch">
        <SearchResults :search-term="inputString" @add-song="clearInputField"/>
      </div>
      <div v-else>
        <DefaultButton :text="$translate('addSong.action')"
                       :is-disabled="!inputIsValid"
                       :is-loading="waitingForResponse"
                       @click="addSong(inputString)"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import {computed, ref, watch} from "vue";
import SearchResults from "@/components/search/SearchResults.vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import * as cookieService from "@/services/cookieService";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";
import {validateSonglink} from "@/plugins/ValidationPlugin";
import {HttpService} from "@/services/HttpService";
import type {Song} from "@/models/Song";
import InfoButton from "@/components/InfoButton.vue";

const inputString = ref("");
const showOverlay = ref(false);
const waitingForResponse = ref(false);
const httpService = new HttpService();
const inputField = ref<InstanceType<typeof InputField>>();

const linkRegex = /^(https?|ftp):\/\/[^\s/$.?#].\S*$/;

const showSearch = computed(() => {
  if (!inputString.value) {
    return false;
  }

  return !linkRegex.test(inputString.value);
});

watch(inputString, () => {
  if (!inputString.value) {
    showOverlay.value = false;
    return;
  }

  showOverlay.value = true;
})

const inputIsValid = computed(() => {
  if (!inputString.value) {
    return false;
  }

  return validateSonglink(inputString.value)();
});

function addSong(link: string) {
  waitingForResponse.value = true;

  httpService.postQueueAdd(link, cookieService.getApiKey())
      .then((data: Song) => {
        waitingForResponse.value = false;
        clearInputField();

        ToastService.sendNotification(
            `"${data.title}" ${translate("notifications.queueAddSuccess")}`,
            "success",
            3000
        );
      })
      .catch(() => {
        waitingForResponse.value = false;
        clearInputField();

        ToastService.sendNotification(
            translate("notifications.queueAddError"),
            "error",
            3000
        );
      });
}

function openOverlay() {
  if (!inputString.value) {
    return;
  }

  showOverlay.value = true;
}

function closeOverlay() {
  showOverlay.value = false;
}

function clearInputField() {
  inputField.value?.clearInput();
}
</script>

<style scoped>
.add-song-wrapper {
  width: 100%;
  height: 100%;
  position: relative;
  background: var(--secondary-color);
  border-radius: var(--border-radius-medium);
}

.add-song-container {
  padding: 10px 10px 0 10px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.add-song-label {
  display: flex;
  align-items: center;
  gap: 5px;
}

.add-song-label span {
  font-size: var(--font-size-big);
}

.add-song-overlay {
  position: absolute;
  top: 90px;
  width: 100%;
  max-height: calc(100svh - 240px);
  background-color: var(--secondary-color);
  border-radius: var(--border-radius-medium);
  padding: 10px;
  box-sizing: border-box;
  overflow-y: scroll;
  z-index: 2;
}

.add-song-overlay.not-visible {
  visibility: hidden;
}

.add-song-overlay.visible {
  visibility: visible;
}

@media screen and (min-width: 1250px) {
  .add-song-wrapper {
    padding: 10px 0 0 0;
    box-sizing: border-box;
    background: none;
    display: flex;
    flex-direction: column;
  }

  .add-song-overlay {
    position: static;
    height: 100%;
    padding-top: 0;
  }

  .add-song-overlay.not-visible {
    visibility: visible;
  }
}
</style>