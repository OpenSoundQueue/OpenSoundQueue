<template>
  <main>
    <OverlayCollapse label="Add Song" :icon="resolveFilePath('/icons/music/playlist_add.svg')" :is-collapsed="false">
      <p>test</p>
    </OverlayCollapse>
    <VoteSkip :update-interval="4000"/>
  </main>
</template>

<script setup lang="ts">
import VoteSkip from "@/components/control/VoteSkip.vue";
import OverlayCollapse from "@/components/collapse/OverlayCollapse.vue";
import {resolveFilePath} from "@/services/urlService";
</script>

<style scoped></style>