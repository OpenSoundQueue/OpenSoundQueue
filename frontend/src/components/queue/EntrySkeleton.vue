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
  height: 35px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

.queue-number {
  height: 16px;
  width: 16px;
}

.song-details-container {
  width: 90%;
}

.song-artist {
  width: 100%;
  margin-top: 3px;
  height: 11px;
}

.song-title {
  height: 16px;
  width: 100%;
}

.song-duration {
  margin-left: auto;
  width: 25px;
  height: 11px;
}

</style>