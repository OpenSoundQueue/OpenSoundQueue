<template>
  <main>
    <div class="add-song-container">
      <AddSong/>
    </div>
    <div class="queue-scroll-container">
      <QueueScroll :update-interval="4000"/>
    </div>
    <div class="control-panel-wrapper">
      <div class="control-panel-container">
        <div class="now-playing-container">
          <NowPlaying :update-interval="1000"/>
        </div>
        <div class="vote-skip-container">
          <VoteSkip :update-interval="4000"/>
        </div>
      </div>
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
  width: calc(100% - 30px);
  height: calc(100vh - 60px);
  margin: auto;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-sizing: border-box;
  padding-top: 20px;
}

.add-song-container {
  height: 70px;
}

.queue-scroll-container {
  border-bottom: var(--secondary-color) dashed 2px;
  height: calc(100% - 70px - 230px);
}

.control-panel-wrapper {
  width: 100%;
  height: 230px;
  bottom: 0;
  left: 0;
}

.control-panel-container {
  margin: auto;
  background: var(--background-color);
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 20px;
  box-sizing: border-box;
}

.now-playing-container {
  margin-top: 20px;
}

.vote-skip-container {
  margin: 20px 0;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

@media screen and (min-width: 800px){
  main {
    width: 750px;
  }
  .control-panel-container {
    width: 750px;
  }
}
</style>