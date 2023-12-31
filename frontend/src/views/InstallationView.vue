<template>
  <nav>
    <InstallationProgress :installation-order="installationSteps"/>
  </nav>
  <main class="scrollbar">
    <header>
      <div class="step-name">{{ translate(currentRouteName.valueOf()) }}</div>
      <img class="header-image" src="@/assets/logo/logo_white.svg" :alt="translate('altTexts.logo')">
    </header>
    <div class="install-container">
      <component :is="component"
                 @ready="readyForNextStep = true"
                 @not-ready="readyForNextStep = false"/>
    </div>
  </main>
  <div class="button-container">
    <div v-if="router.currentRoute.value.name!='language'" class="button back" @click="back()">
      <div>{{ translate(lastStep.valueOf()) }}</div>
      <img src="@/assets/icons/arrows/keyboard_arrow_left.svg" :alt="translate('altTexts.arrowLeft')">
    </div>
    <div class="button next"
         :class="[router.currentRoute.value.name=='language'? 'full-width':'',readyForNextStep?'ready':'']"
         @click="next()">
      <div>{{ translate(nextStep.valueOf()) }}</div>
      <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="translate('altTexts.arrowRight')">
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref, shallowRef, watch} from "vue";
import type {Component, Ref, ShallowRef, ComputedRef} from "vue";
import LanguageSetting from "@/components/installation/LanguageSetting.vue";
import PrivacySetting from "@/components/installation/PrivacySetting.vue";
import SourceSetting from "@/components/installation/SourceSetting.vue";
import InstallationProgress from "@/components/installation/InstallationProgress.vue";
import router from "@/router";
import type {TranslationsKey} from "@/plugins/TranslationPlugin";
import type {RouteRecordName} from "vue-router";
import {useInstallationStore} from "@/stores/Installation";
import {translate} from "@/plugins/TranslationPlugin";
import {installation} from "@/store/store";

const store = useInstallationStore();
const installationProgress = ref(installation.currentStep);

watch(installationProgress, (newValue) => {
  installation.currentStep = newValue;
})


const component: ShallowRef<Component | undefined> = shallowRef(LanguageSetting);
const installationSteps: RouteRecordName[] = ['language', 'registration', 'privacy', 'sources'];
const readyForNextStep: Ref<boolean> = ref(false);

const currentRouteName: ComputedRef<TranslationsKey> = computed(() => {
  return <TranslationsKey>`installation.progressBar.${String(router.currentRoute.value.name)}`
});
const nextStep: ComputedRef<TranslationsKey> = computed(() => {
  return <TranslationsKey>`installation.navigation.next.${String(router.currentRoute.value.name)}`
});
const lastStep: ComputedRef<TranslationsKey> = computed(() => {
  return <TranslationsKey>`installation.navigation.back.${String(router.currentRoute.value.name)}`
});

onMounted(() => {
  chooseComponent()
})

watch(router.currentRoute, () => {
  chooseComponent();
})

function chooseComponent() {
  const routeName = router.currentRoute.value.name;
  if (routeName == undefined)
    return;

  if (routeName !== installationProgress.value){
    if (installationSteps.indexOf(routeName) > installationSteps.indexOf(installationProgress.value))
      router.push({name: installationProgress.value});
    else
      installationProgress.value = routeName;
  }

  if (routeName === 'language') {
    component.value = LanguageSetting
  }
  if (routeName === 'registration') {
    component.value = LanguageSetting
  }
  if (routeName === 'privacy') {
    component.value = PrivacySetting
  }
  if (routeName === 'sources') {
    component.value = SourceSetting
  }
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

async function next() {
  const routeName = router.currentRoute.value.name;
  if (routeName == undefined || !readyForNextStep.value)
    return;

  try {
    switch (routeName) {
      case "language":
        await store.saveLanguage()
        installationProgress.value = 'registration';
        break;
      case "registration":
        installationProgress.value = 'privacy';
        break;
      case "privacy":
        await store.savePrivacy()
        installationProgress.value = 'registration';
        break;
      case "sources":
        await store.saveSources()
        installationProgress.value = 'sources';
        break;
    }
  } catch {
    return;
  }

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
  min-height: calc(100vh - 92px - 45px);
  margin: 0;
  padding: 0;
  overflow-y: auto;
}

header {
  position: sticky;
  top: 0;
  background-color: var(--grayish-blue);

  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;

  .step-name {
    width: 100%;
    padding-left: 25px;
    padding-right: 25px;
    font-size: var(--font-size-big);
    font-weight: bold;
  }
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

.ready {
  background-color: var(--primary-color);
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
    min-height: calc(100vh - 270px - 45px);
    max-height: calc(100vh - 270px - 45px);
  }

  .button-container {
    width: 600px;
    margin: 20px auto 0 auto;
  }
}
</style>