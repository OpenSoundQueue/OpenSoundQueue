<template>
  <div class="entry-wrapper">
    <div v-if="typeof numberInQueue !== 'undefined'" class="queue-number">
      {{ numberInQueue + 1 }}
    </div>

    <div class="song-details-container">
      <div class="song-title">
        {{ title }}
      </div>
      <div class="song-artist">
        {{ artist }}
      </div>
    </div>
    <div class="song-duration">
      {{ getDurationString(duration) }}
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  numberInQueue?: number,
  title: string,
  artist: string,
  duration: number
}>();

function getDurationString(durationInSeconds: number): string {
  return `${Math.floor(durationInSeconds / 60) ?? 0}:${durationInSeconds % 60 < 10 ? "00" : durationInSeconds % 60}`
}
</script>

<style scoped>
.entry-wrapper {
  width: 100%;
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.queue-number {
  font-size: var(--font-size-medium);
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 28px;
}

.song-details-container {
  width: 90%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.song-artist {
  width: 100%;
  margin-top: 3px;
  font-size: var(--font-size-small);
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.song-title {
  width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.song-duration {
  margin-left: auto;
  font-size: var(--font-size-small);
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
</style>