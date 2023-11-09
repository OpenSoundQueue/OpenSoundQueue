<template>
  <div class="control-panel">
    <ReplaySong v-show="replay"/>
    <div class="skip-container">
          <div v-show="voteSkip" class="vote-skip">
      <VoteSkip :update-interval="4000"/>
      <InfoButton>{{ $translate('help.voteSkip') }}</InfoButton>
    </div>
    <StartStopSong v-show="skip"/>
    </div>
    <SkipSong v-show="skip"/>
  </div>
</template>

<script setup lang="ts">
import VoteSkip from "@/components/control/VoteSkip.vue";
import InfoButton from "@/components/InfoButton.vue";
import SkipSong from "@/components/control/SkipSong.vue";
import ReplaySong from "@/components/control/ReplaySong.vue";
import StartStopSong from "@/components/control/StartStopSong.vue";

const props = defineProps<{
  voteSkip:boolean,
  startStop:boolean,
  skip: boolean,
  replay:boolean
}>();
</script>

<style scoped>
.control-panel,.vote-skip{
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.control-panel{
  gap: 30px;
  width: 100%;
}

.skip-container{
  display: flex;
  flex-direction: column;
  gap: 10px;
  justify-content: center;
  align-items: center;
}

.vote-skip{
  gap: 10px;
}
</style>