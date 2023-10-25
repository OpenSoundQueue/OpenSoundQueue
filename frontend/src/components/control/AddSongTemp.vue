<template>
  <div class="add-song-wrapper">
    <div class="mobile">
      <OverlayCollapse :label="$translate('addSong')"
                       :is-collapsed="true"
      >
        <template #custom-icon>
          <img src="@/assets/icons/music/playlist_add.svg">
        </template>
        <template #content>
          <div class="add-song-slot">
            <AddSong/>
          </div>
        </template>
      </OverlayCollapse>
    </div>
    <div class="desktop">
      <AddSong/>
    </div>
  </div>
</template>

<script setup lang="ts">
import OverlayCollapse from "@/components/collapse/OverlayCollapse.vue";
import AddSong from "@/components/control/AddSong.vue";
</script>

<style scoped>
.add-song-wrapper {
  height: 100%;
}

.desktop {
  display: none;
}

.add-song-slot {
  width: 100%;
  height: 100%;
}

@media screen and (min-width: 1100px) {
  .mobile {
    display: none;
  }

  .desktop {
    display: block;
    height: 100%;
  }
}
</style>