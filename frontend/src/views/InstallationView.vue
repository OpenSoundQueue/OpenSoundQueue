<template>
  <nav>
    <InstallationProgress :installation-order="installationSteps"/>
  </nav>
  <main>
    <header>
      <img class="header-image" src="@/assets/logo/logo_white.svg" :alt="$translate('altTexts.logo')">
    </header>
    <div class="install-container">
      <component :is="component"/>
    </div>
  </main>
  <div class="button-container">
    <div v-if="router.currentRoute.value.name!='language'" class="button back" @click="back()">
      <div>{{ translate(lastStep.valueOf()) }}</div>
      <img src="@/assets/icons/arrows/keyboard_arrow_left.svg" :alt="$translate('altTexts.arrowLeft')">
    </div>
    <div class="button next" :class="[router.currentRoute.value.name=='language'? 'full-width':'']" @click="next()">
      <div>{{ translate(nextStep.valueOf()) }}</div>
      <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')">
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, shallowRef, watch} from "vue";
import type {Component, ShallowRef} from "vue";
import {HttpService} from "@/services/HttpService";
import LanguageSetting from "@/components/installation/LanguageSetting.vue";
import PrivacySetting from "@/components/installation/PrivacySetting.vue";
import SourceSetting from "@/components/installation/SourceSetting.vue";
import InstallationProgress from "@/components/installation/InstallationProgress.vue";
import router from "@/router";
import {translate} from "@/plugins/TranslationPlugin";
import type {TranslationsKey} from "@/plugins/TranslationPlugin";
import type {RouteRecordName} from "vue-router";

const httpService = new HttpService()
const component: ShallowRef<Component | undefined> = shallowRef(LanguageSetting);
const installationSteps: RouteRecordName[] = ['language', 'registration', 'privacy', 'sources'];

const nextStep: TranslationsKey = computed(() => {
  return <TranslationsKey>`installation.navigation.next.${router.currentRoute.value.name}`
});
const lastStep: TranslationsKey = computed(() => {
  return <TranslationsKey>`installation.navigation.back.${router.currentRoute.value.name}`
});

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

function back() {
  const routeName = router.currentRoute.value.name;
  if (routeName == undefined)
    return;

  if (installationSteps.indexOf(routeName) == 0) {
    return;
  }

  router.push({name: installationSteps[installationSteps.indexOf(routeName) - 1]});
}

function next() {
  const routeName = router.currentRoute.value.name;
  if (routeName == undefined)
    return;

  if (installationSteps.indexOf(routeName) == installationSteps.length - 1) {
    router.push("/home")
    return;
  }

  router.push({name: installationSteps[installationSteps.indexOf(routeName) + 1]});
}
</script>

<style scoped>
nav {
  background-color: var(--grayish-blue);
  margin: 0;
  padding-top: 10px;
  padding-bottom: 10px;
}

main {
  background-color: var(--grayish-blue);
  width: 100svw;
  min-height: calc(100vh - 92px);
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

.button-container {
  display: grid;
  margin-top: 20px;
  grid-template-columns: minmax(0, 1fr) 20px minmax(0, 1fr);
  width: 100%;
}

.button {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  background-color: var(--grayish-blue);
  border-radius: var(--border-radius-medium);
  padding: 5px 0 5px 0;

  user-select: none;

  div {
    width: 100%;
    text-align: center;

    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    padding: 0 10px 0 10px;
  }

  img {
    height: 35px;
    aspect-ratio: 1;
  }
}

.button:hover {
  cursor: pointer;
}

.back {
  grid-column: 1;
  flex-direction: row-reverse;
}

.next {
  grid-column: 3;
}

.full-width {
  grid-column: 1 / 4;
}

@media screen and (min-width: 600px) {
  nav {
    background-color: transparent;
    width: 600px;
    margin: 50px auto 10px auto;
  }

  main {
    width: 600px;
    border-radius: var(--border-radius-big);
    margin: 0 auto;
    min-height: calc(100vh - 270px);
    max-height: calc(100vh - 270px);
  }

  .button-container {
    width: 600px;
    margin: 20px auto 0 auto;
  }
}
</style>