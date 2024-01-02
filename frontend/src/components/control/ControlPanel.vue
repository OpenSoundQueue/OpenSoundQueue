<template>
  <div class="control-panel">
    <ReplaySong v-show="replay" @update="() => emit('update')"/>
    <div class="skip-container">
      <StartStopSong v-show="startStop" :is-playing="isPlaying" @update="() => emit('update')"/>
      <div v-show="voteSkip && !startStop" class="vote-skip">
        <VoteSkip :update-interval="4000"/>
        <InfoButton>{{ $translate('help.voteSkip') }}</InfoButton>
      </div>
    </div>
    <SkipSong v-show="skip" @update="() => emit('update')"/>
    <div v-show="voteSkip && startStop" class="vote-skip">
      <VoteSkip :update-interval="4000"/>
      <InfoButton>{{ $translate('help.voteSkip') }}</InfoButton>
    </div>
    <div>
      <VolumeChanger/>
    </div>
  </div>
</template>

<script setup lang="ts">
import VoteSkip from "@/components/control/VoteSkip.vue";
import InfoButton from "@/components/InfoButton.vue";
import SkipSong from "@/components/control/SkipSong.vue";
import ReplaySong from "@/components/control/ReplaySong.vue";
import StartStopSong from "@/components/control/StartStopSong.vue";
import VolumeChanger from "@/components/control/VolumeChanger.vue";

defineProps<{
  voteSkip: boolean,
  startStop: boolean,
  skip: boolean,
  replay: boolean,
  isPlaying: boolean
}>();

const emit = defineEmits<{
  update: [];
}>()
</script>

<style scoped>
.control-panel, .vote-skip {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.control-panel {
  gap: 30px;
  width: 100%;
}

.skip-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  justify-content: center;
  align-items: center;
}

.vote-skip {
  gap: 10px;
}
</style>