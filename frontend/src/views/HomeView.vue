<template>
  <InfoButton>Hallo Luki wie geht es dir heute abend hier is noch eine dritte zeile</InfoButton>
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
          <InfoButton>Markus hat eine neue Brille gekauft</InfoButton>
          <VoteSkip :update-interval="4000"/>
          <InfoButton>Mein Name ist Daniel PIllwein und ich kümmere mich um diese infobuttons</InfoButton>
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
import InfoButton from "@/components/InfoButton.vue";
</script>

<style scoped>
main {
  width: calc(100% - 30px);
  margin: auto;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  padding-top: 20px;
}

.add-song-container {
  height: 70px;
}

.queue-scroll-container {
  border-bottom: var(--secondary-color) dashed 2px;
  height: calc(100svh - 230px - 70px - 60px - 20px);
}

.control-panel-wrapper {
  width: 100%;
  height: fit-content;
  position: fixed;
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