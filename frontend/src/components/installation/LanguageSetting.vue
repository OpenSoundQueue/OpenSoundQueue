<template>
  <div class="language-container scrollbar">
    <div class="description">{{ translate('installation.language')}} </div>
    <div v-for="(language,index) of Object.keys(translations)"
         :key="index"
         @click="store.setLanguage(language)"
         class="language-wrapper"
    :class="[store.language==language?'selected':'']">
      <div>{{ translate(`languages.${language}`) }}</div>
      <img v-show="store.language==language"
           src="@/assets/icons/check.svg"
           :alt="translate('altTexts.check')">
    </div>
  </div>
</template>

<script setup lang="ts">
import {useInstallationStore} from "@/stores/Installation";
import {onMounted, watchEffect} from "vue";
import {translate, translations} from "@/plugins/TranslationPlugin";

const store = useInstallationStore();

onMounted(() => {
  checkStatus()

  watchEffect(() => {
    checkStatus();
  });
})


function checkStatus() {
  if (store.language != null) {
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
.language-container{
  display: flex;
  flex-direction: column;
  gap: 10px;

  overflow-y: scroll;
  padding: 25px;
}

.description{
  font-style: italic;
}

.language-wrapper{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  width: 100%;
  padding: calc(var(--font-size-medium) / 2);

  border: solid 2px var(--cool-gray);
  border-radius: var(--border-radius-medium);
  box-sizing: border-box;


  &&.selected{
    background-color: rgba(151, 71, 255, 0.5);
    border-color: var(--primary-color);
  }

  img{
    height: var(--font-size-medium);
  }
}


</style>