<template>
  <main>
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
              <InputField label="Song Link" input-type="text"/>
              <DefaultButton :is-disabled="false" text="Add to queue">
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
    <VoteSkip :update-interval="4000"/>
  </main>
</template>

<script setup lang="ts">
import VoteSkip from "@/components/control/VoteSkip.vue";
import OverlayCollapse from "@/components/collapse/OverlayCollapse.vue";
import Tabs from "@/components/Tabs.vue";
import InputField from "@/components/inputs/InputField.vue";
import HistorySearch from "@/components/search/HistorySearch.vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
</script>

<style scoped>
.tab-wrapper {
  margin: auto;
}
</style>