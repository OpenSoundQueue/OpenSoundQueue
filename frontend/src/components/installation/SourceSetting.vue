<template>
  <div class="source-container scrollbar" v-if="store.editedApplicationSettings">
    <div class="description">{{ $translate('applicationSettings.sources.description') }}</div>
    <div v-for="(source, index) of store.editedApplicationSettings.supportedSources"
         :key="index"
         @click="toggleSource(source)"
         class="source-wrapper">
      <div>{{ source }}</div>
      <Checkbox :checked="store.editedApplicationSettings.enabledSources.includes(source)"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, watchEffect} from "vue";
import Checkbox from "@/components/buttons/Checkbox.vue";
import {useApplicationSettingsStore} from "@/stores/ApplicationSettings";

const emit = defineEmits<{
  ready: [],
  notReady: []
}>()

const store = useApplicationSettingsStore();

onMounted(async () => {
  checkStatus()

  watchEffect(() => {
    checkStatus();
  });
})

function checkStatus() {
  if (!store.editedApplicationSettings?.enabledSources) {
    emit("notReady");
    return;
  }

  if (store.editedApplicationSettings?.enabledSources.length === 0) {
    emit("notReady");
    return;
  }

  emit("ready");
}

function toggleSource(source: string) {
  if (!store.editedApplicationSettings) {
    return;
  }

  store.toggleSource(source);
}
</script>

<style scoped>
.source-container {
  display: flex;
  flex-direction: column;
  gap: 10px;

  overflow-y: scroll;
  padding: 25px;
}

.description {
  font-style: italic;
}

.source-wrapper {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  width: 100%;
  padding: calc(var(--font-size-medium) / 2 + 2px);

  border-radius: var(--border-radius-medium);
  box-sizing: border-box;

  &&:hover {
    background-color: rgb(var(--tertiary-color));
    color: rgb(var(--background-color));
    font-weight: bold;

    && > .checkbox {
      border-color: rgb(var(--background-color)) !important;
    }
  }

  &&.selected {
    background-color: rgb(var(--tertiary-color));
    border-color: rgb(var(--tertiary-color));
  }
}
</style>