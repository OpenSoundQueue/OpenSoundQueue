<template>
  <div class="source-container scrollbar">
    <div class="description">{{ $translate('applicationSettings.sources.description') }}</div>
    <div v-for="(source,index) of supportedSources"
         :key="index"
         @click="store.toggleSource(source)"
         class="source-wrapper">
      <div>{{ source }}</div>
      <Checkbox :checked="store.sources.includes(source)"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useInstallationStore} from "@/stores/Installation";
import {onMounted, ref, watchEffect} from "vue";
import type {Ref} from "vue";
import {HttpService} from "@/services/HttpService";
import Checkbox from "@/components/buttons/Checkbox.vue";

const store = useInstallationStore();
const httpService = new HttpService();

const supportedSources:Ref<string[]> = ref([]);

onMounted(async () => {
  await httpService.getSupportedSources()
      .then(data => {
        supportedSources.value = data
      })
  checkStatus()

  watchEffect(() => {
    checkStatus();
  });
})


function checkStatus() {
  if (store.sources.length > 0) {
    emit("ready");
  } else {
    emit("notReady");
  }
}

const emit = defineEmits<{
  ready: [],
  notReady: []
}>()
</script>

<style scoped>
.source-container{
  display: flex;
  flex-direction: column;
  gap: 10px;

  overflow-y: scroll;
  padding: 25px;
}

.description{
  font-style: italic;
}

.source-wrapper{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  width: 100%;
  padding: calc(var(--font-size-medium) / 2 + 2px);

  border-radius: var(--border-radius-medium);
  box-sizing: border-box;

  &&:hover{
    background-color: rgb(var(--tertiary-color));
    color: rgb(var(--background-color));
    font-weight: bold;

     && > .checkbox {
      border-color: rgb(var(--background-color)) !important;
    }
  }

  &&.selected{
    background-color: rgb(var(--tertiary-color));
    border-color: rgb(var(--tertiary-color));
  }
}
</style>