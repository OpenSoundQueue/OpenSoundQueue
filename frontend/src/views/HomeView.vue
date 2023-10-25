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
  height: calc(100% - 70px - 190px);
}

.control-panel-wrapper {
  width: 100%;
  bottom: 0;
  left: 0;
}

.control-panel-container {
  margin: auto;
  background: var(--background-color);
  height: 190px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  box-sizing: border-box;
}

.vote-skip-container {
  margin: 10px 0;
  display: flex;
  justify-content: center;
}

@media screen and (min-width: 800px) {
  main {
    width: 750px;
  }
}

@media screen and (min-width: 1250px){
  main {
    width: 1250px;
    display: grid;
    grid-template-columns: 66% 33%;
    grid-template-rows: calc(100% - 230px) 230px;
  }

  .add-song-container {
    grid-column: 2;
    grid-row: 1;
    height: 100%;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
  }

  .queue-scroll-container {
    grid-column: 1;
    grid-row: 1;
    height: 100%;
    border-bottom: none;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
  }

  .control-panel-wrapper {
    grid-column: 1 / span 2;
    grid-row: 2;
    height: 100%;
    display: flex;
    align-items: center;
  }

  .control-panel-container {
    width: 100%;
    border: var(--secondary-color) 3px solid;
    border-radius: var(--border-radius-big);
    display: grid;
    place-items: center;
    grid-template-columns: 66% 33%;
  }

  .now-playing-container {
    width: 100%;
    margin-bottom: 20px;
    grid-row: 1;
    grid-column: 2;
    padding: 20px;
    box-sizing: border-box;
  }

  .vote-skip-container {
    width: 100%;
    border-right: var(--secondary-color) 3px solid;
    height: 85%;
    box-sizing: border-box;
    grid-row: 1;
    grid-column: 1;
    margin: 0;
    display: flex;
    justify-content: center;
    align-items: center;
  }

}
</style>