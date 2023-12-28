<template>
  <main>
    <header>
      <img class="header-image" src="@/assets/logo/logo_white.svg" :alt="$translate('altTexts.logo')">
    </header>
    <div class="install-container">
      <InstallationProgress
          :installation-order="['language', 'registration','privacy', 'sources']"/>
      <br><br>
      <component :is="component"/>
    </div>
  </main>
</template>

<script setup lang="ts">
import {onMounted, shallowRef, watch} from "vue";
import type {Component, ShallowRef} from "vue";
import {HttpService} from "@/services/HttpService";
import LanguageSetting from "@/components/installation/LanguageSetting.vue";
import PrivacySetting from "@/components/installation/PrivacySetting.vue";
import SourceSetting from "@/components/installation/SourceSetting.vue";
import InstallationProgress from "@/components/installation/InstallationProgress.vue";
import router from "@/router";

const httpService = new HttpService()
const component: ShallowRef<Component | undefined> = shallowRef(LanguageSetting);

onMounted(() => {
  chooseComponent()
})

watch(router.currentRoute, () => {
  chooseComponent();
})

function chooseComponent() {
  const routeName = router.currentRoute.value.name;

  if (routeName === 'language') component.value = LanguageSetting
  if (routeName === 'register') component.value = LanguageSetting
  if (routeName === 'privacy') component.value = PrivacySetting
  if (routeName === 'sources') component.value = SourceSetting
}
</script>

<style scoped>

main {
  background-color: var(--grayish-blue);
  width: 100svw;
  min-height: 100vh;
  margin: 0;
  padding: 0;
  overflow-y: auto;
}

header {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
}

.header-image {
  height: calc(var(--font-size-big) * 1.5);
  padding: 25px;
}

@media screen and (min-width: 600px) {
  main {
    width: 600px;
    border-radius: var(--border-radius-big);
    margin: 50px auto 0 auto;
    min-height: calc(100vh - 100px);
    max-height: calc(100vh - 100px);
  }
}
</style>