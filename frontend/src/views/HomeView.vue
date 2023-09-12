<template>
  <main>
    <OverlayCollapse label="Add Song" :icon="resolveFilePath('/icons/music/playlist_add.svg')" :is-collapsed="false">
      <Tabs :tabs="[$translate('byLink'), $translate('bySearch')]">
        <template #tab-0>
          <InputField input-id="asdf" input-type="password"></InputField>
        </template>
        <template #tab-1>
          <p>hallo2</p>
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
</script>

<style scoped></style>