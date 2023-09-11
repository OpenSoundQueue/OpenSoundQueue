<template>
  <main>
    <OverlayCollapse label="Add Song" :icon="resolveFilePath('/icons/music/playlist_add.svg')" :is-collapsed="false">
      <div class="tab-switcher-wrapper">
        <div class="tabs-container">
          <div class="tab">tab1</div>
          <div class="tab">tab2</div>
          <div class="spacer"></div>
        </div>
      </div>
    </OverlayCollapse>
    <VoteSkip :update-interval="4000"/>
  </main>
</template>

<script setup lang="ts">
import VoteSkip from "@/components/control/VoteSkip.vue";
import OverlayCollapse from "@/components/collapse/OverlayCollapse.vue";
import {resolveFilePath} from "@/services/urlService";
</script>

<style scoped>
.tab-switcher-wrapper {
  width: calc(100% - 40px);
  height: 100px;
  background: red;
}

.tabs-container {
  display: flex;
  justify-content: space-around;
  width: 100%;
}

.tab {
  background: blue;
  width: 40%;
}

@media screen and (min-width: 420px) {
  .tab {
    background: blue;
    width: 210px;
  }
}
</style>