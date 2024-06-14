<template>
  <div class="language-container scrollbar" v-if="store.editedApplicationSettings">
    <div class="description">{{ $translate('applicationSettings.defaultLanguage.description') }}</div>
    <div v-for="(language, index) of Object.keys(translations)"
         :key="index"
         @click="store.setLanguage(language)"
         class="language-wrapper"
         :class="[store.editedApplicationSettings.language == language ? 'selected':'']">
      <div>{{ $translate(`languages.${language}`) }}</div>
      <svg v-show="store.editedApplicationSettings.language == language" xmlns="http://www.w3.org/2000/svg"
           viewBox="0 -960 960 960">
        <path d="M382-240 154-468l57-57 171 171 367-367 57 57-424 424Z"/>
      </svg>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, watchEffect} from "vue";
import {translations} from "@/plugins/TranslationPlugin";
import {useApplicationSettingsStore} from "@/stores/ApplicationSettings";

const emit = defineEmits<{
  ready: [],
  notReady: []
}>()

const store = useApplicationSettingsStore();

onMounted(() => {
  checkStatus()

  watchEffect(() => {
    checkStatus();
  });
})

function checkStatus() {
  if (store.editedApplicationSettings?.language) {
    emit("ready");
  } else {
    emit("notReady");
  }
}
</script>

<style scoped>
.language-container {
  display: flex;
  flex-direction: column;
  gap: 10px;

  overflow-y: scroll;
  padding: 25px;
}

.description {
  font-style: italic;
}

.language-wrapper {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  width: 100%;
  padding: calc(var(--font-size-medium) / 2);

  border: solid 2px var(--cool-gray);
  border-radius: var(--border-radius-medium);
  box-sizing: border-box;


  &&.selected {
    background-color: var(--cool-gray-transparent);
    border-color: var(--cool-gray);
    color: var(--dark-blue);
    font-weight: bold;
  }

  svg {
    height: var(--font-size-medium);
    fill: var(--dark-blue);
    aspect-ratio: 1;
  }
}
</style>