<template>
  <div class="add-song-wrapper" @click="openOverlay" v-closable="{excluded: [], handler: closeOverlay}">
    <InputField placeholder="Suchen oder Link"
                v-model="addSongInput"
                ref="inputField"
    />
    <div class="add-song-overlay" :class="[showOverlay ? 'visible' : 'not-visible']">
      <div v-if="showSearch">
        <SearchResults :search-term="addSongInput"/>
      </div>
      <div v-else>
        <DefaultButton :text="$translate('byLink.action')"
                       :is-disabled="!inputIsValid"
                       :is-loading="waitingForResponse"
                       @click="addSong(addSongInput)"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import {computed, ref, watch} from "vue";
import SearchResults from "@/components/control/SearchResults.vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import * as cookieService from "@/services/cookieService";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";
import {validateSonglink} from "@/plugins/ValidationPlugin";
import {HttpService} from "@/services/HttpService";

const addSongInput = ref("");
const showOverlay = ref(false);
const waitingForResponse = ref(false);
const httpService = new HttpService();
const inputField = ref<InstanceType<typeof InputField>>();

const linkRegex = /^(https?|ftp):\/\/[^\s/$.?#].\S*$/;

const showSearch = computed(() => {
  if (!addSongInput.value) {
    return false;
  }

  return !linkRegex.test(addSongInput.value);
});

watch(addSongInput, () => {
  if (!addSongInput.value) {
    showOverlay.value = false;
    return;
  }

  showOverlay.value = true;
})

const inputIsValid = computed(() => {
  if (!addSongInput.value) {
    return false;
  }

  return validateSonglink(addSongInput.value)();
});

function addSong(link: string) {
  waitingForResponse.value = true;

  httpService.postQueueAdd(link, cookieService.getApiKey())
      .then((data) => {
        waitingForResponse.value = false;
        inputField.value?.clearInput();

        ToastService.sendNotification(
            `"${data.title}" ${translate("notifications.queueAddSuccess")}`,
            "success",
            3000
        );
      })
      .catch(() => {
        waitingForResponse.value = false;
        inputField.value?.clearInput();

        ToastService.sendNotification(
            translate("notifications.queueAddError"),
            "error",
            3000
        );
      });
}

function openOverlay() {
  if (!addSongInput.value) {
    return;
  }

  showOverlay.value = true;
}

function closeOverlay() {
  showOverlay.value = false;
}

</script>

<style scoped>
.add-song-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 2;
}

.add-song-overlay {
  position: absolute;
  top: 55px;
  width: 100%;
  max-height: calc(100svh - 150px);
  background-color: var(--secondary-color);
  border-radius: var(--border-radius-medium);
  padding: 10px;
  box-sizing: border-box;
  overflow-y: scroll;
}

.add-song-overlay.not-visible {
  visibility: hidden;
}

.add-song-overlay.visible {
  visibility: visible;
}

@media screen and (min-width: 1250px) {
  .add-song-wrapper {
    padding: 10px 10px 10px 10px;
    box-sizing: border-box;
  }

  .add-song-overlay {
    position: static;
    height: 100%;
    padding: 0;
  }

  .add-song-overlay.not-visible {
    visibility: visible;
  }
}
</style>