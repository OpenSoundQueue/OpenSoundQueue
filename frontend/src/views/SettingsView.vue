<template>
  <main>
    <div class="settings-wrapper">
      <div class="settings-nav">
        <img @click="goToHome" class="back" src="@/assets/icons/arrows/arrow_back.svg" :alt="$translate('altTexts.arrowBack')">
      </div>
      <div class="settings-header">
        <img src="@/assets/icons/settings.svg" :alt="$translate('altTexts.settings')">
        <h1>{{ $translate('settings')}}</h1>
      </div>
      <Select :label="$translate('language')"
              v-model="language"
              :options="languages"
      />
    </div>
  </main>
</template>

<script setup lang="ts">
import Select from "@/components/inputs/Select.vue";
import {ref, watch} from "vue";
import {setLanguage} from "@/plugins/TranslationPlugin";
import {settings} from "@/store/store";
import router from "@/router/index";

const language = ref(settings.language);

watch(language, (newValue) => {
  settings.language = newValue;
  setLanguage(language.value);
})

const languages = ref([
  {value: "de", text: "Deutsch"},
  {value: "en", text: "English"},
  {value: "es", text: "Español"}
]);

function goToHome() {
  router.push("/home");
}
</script>


<style scoped>
.settings-wrapper {
  background: var(--secondary-color);
  height: calc(100vh - 120px);
  padding: 30px;
  margin: 0 auto 0 auto;
  overflow-y: auto;
}

.settings-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.settings-nav {
  height: 32px;
}

.settings-nav .back {
  height: 32px;
  cursor: pointer;
}

.settings-header {
  display: flex;
  align-items: center;
  gap: 20px;
}

.settings-header img {
  height: 40px;
}

h1 {
  font-weight: bold;
}

@media screen and (min-width: 800px) {
  .settings-wrapper {
    border-radius: var(--border-radius-big);
    width: 750px;
    height: 75vh;
    margin-top: 110px;
  }
}
</style>