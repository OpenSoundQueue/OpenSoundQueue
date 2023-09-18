<template>
  <main>
    <div class="add-song-container">
      <AddSong/>
    </div>
    <div class="queue-scroll-container">
      <QueueScroll :update-interval="4000"/>
    </div>
    <div class="now-playing-container">
      <NowPlaying :update-interval="1000"/>
    </div>
    <div class="control-panel-container">
      <VoteSkip :update-interval="4000"/>
    </div>
  </main>
</template>

<script setup lang="ts">
import VoteSkip from "@/components/control/VoteSkip.vue";
import QueueScroll from "@/components/queue/QueueScroll.vue";
import NowPlaying from "@/components/NowPlaying.vue";
import AddSong from "@/components/control/AddSong.vue";
</script>

<style scoped>
main {
  height: 90vh;
  display: flex;
  flex-direction: column;
}

.add-song-container {
  height: 10%;
}

.queue-scroll-container {
  height: 60%;
}

.now-playing-container {
  margin-top: 20px;
  height: 15%;
}

.control-panel-container {
  height: 15%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
</style>