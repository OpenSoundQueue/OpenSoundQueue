<template>
  <div class="entry-wrapper">
    <div class="queue-number skeleton"></div>
    <div class="song-details-container">
      <div class="song-title skeleton"></div>
      <div class="song-artist skeleton"></div>
    </div>
    <div class="song-duration skeleton"></div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>

.entry-wrapper {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

.queue-number {
  height: var(--font-size-big);
  width: 16px;
}

.song-details-container {
  width: 90%;
}

.song-artist {
  width: 100%;
  margin-top: 3px;
  height: calc(var(--font-size-small) + 2px);
}

.song-title {
  height: var(--font-size-big);
  width: 100%;
}

.song-duration {
  margin-left: auto;
  width: 25px;
  height: var(--font-size-small);
}

</style>