<template>
  <div class="add-song-wrapper">
    <OverlayCollapse :label="$translate('addSong')"
                     :is-collapsed="true"
    >
      <template #custom-icon>
        <img src="@/assets/icons/music/playlist_add.svg">
      </template>
      <template #content>
        <Tabs :tabs="[$translate('byLink.title'), $translate('bySearch')]">
          <template #tab-0>
            <div class="tab-wrapper">
              <InputField v-model="songLink"
                          :label="$translate('byLink.label')"
                          input-type="text"
                          :validation-function="validateSonglink"
                          :validation-message="$translate('byLink.incorrectInput')"
                          :placeholder="$translate('byLink.placeholder')"
                          ref="inputField"
              />
              <DefaultButton @click="addSong(songLink)"
                             :is-disabled="!inputIsValid"
                             :is-loading="waitingForResponse"
                             :text="$translate('byLink.action')">
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
import {computed, ref} from "vue";
import {ToastService} from "@/services/ToastService";
import {translate} from "@/plugins/TranslationPlugin";
import {validateSonglink} from "@/plugins/ValidationPlugin";

const httpService = new HttpService();
const songLink = ref("");
const waitingForResponse = ref(false);
const inputField = ref<InstanceType<typeof InputField>>();

const inputIsValid = computed(() => {
  if (!songLink.value) {
    return false;
  }

  return validateSonglink(songLink.value)();
})

function addSong(link: string) {
  waitingForResponse.value = true;

  httpService.postQueueAdd(link)
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
</script>

<style scoped>
.tab-wrapper {
  margin: auto;
}
</style>