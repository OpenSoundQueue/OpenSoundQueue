<template>
  <div class="control-panel">
    <VoteSkip :update-interval="4000"/>
    <InfoButton>{{ $translate('help.voteSkip') }}</InfoButton>
  </div>
</template>

<script setup lang="ts">
import VoteSkip from "@/components/control/VoteSkip.vue";
import InfoButton from "@/components/InfoButton.vue";
const props = defineProps<{
  voteSkip:boolean,
  startStop:boolean,
  skip: boolean,
  restart:boolean
}>();
</script>

<style scoped>
.control-panel{
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 15px;
}
</style>