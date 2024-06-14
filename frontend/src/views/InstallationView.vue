<template>
  <nav>
    <InstallationProgress :installation-order="installationSteps"/>
  </nav>
  <main class="scrollbar">
    <header>
      <div class="step-name">
        <div class="darken">{{ $translate('installation.installation') }}&nbsp;|&nbsp;</div>
        <div>{{ $translate(currentRouteName.valueOf()) }}</div>
      </div>
      <img class="header-image" src="@/assets/logo/logo_white.svg" :alt="$translate('altTexts.logo')">
    </header>
    <div class="install-container">
      <component :is="component"
                 @ready="readyForNextStep = true"
                 @not-ready="readyForNextStep = false"
                 @continue="registration.state = 'validation'"
                 @change="registration.state = 'input'"
                 @validated="registration.state = 'finished'"
                 :mode="'installation'"/>
    </div>
  </main>
  <div class="button-container">
    <div v-if="router.currentRoute.value.name!='language'" class="button back" @click="back()">
      <div>{{ $translate(lastStep.valueOf()) }}</div>
      <img src="@/assets/icons/arrows/keyboard_arrow_left.svg" :alt="$translate('altTexts.arrowLeft')">
    </div>
    <div class="button next"
         :class="[router.currentRoute.value.name=='language'? 'full-width':'',readyForNextStep?'ready':'']"
         @click="next()">
      <div>{{ $translate(nextStep.valueOf()) }}</div>
      <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')">
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
import RegistrationForm from "@/components/registration/RegistrationForm.vue";
import RegistrationVerification from "@/components/registration/RegistrationVerification.vue";
import router from "@/router";
import type {TranslationsKey} from "@/plugins/TranslationPlugin";
import type {RouteRecordName} from "vue-router";
import {useInstallationStore} from "@/stores/Installation";
import {installation, registration} from "@/store/store";
import {removeRegistration} from "@/store/registration";
import RegistrationFinished from "@/components/registration/RegistrationFinished.vue";
import {HttpService} from "@/services/HttpService";
import {ToastService} from "@/services/ToastService";

const httpService = new HttpService();
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
watch(() => registration.state, () => {
  chooseComponent();
});

function chooseComponent() {
  const routeName = router.currentRoute.value.name;
  if (routeName == undefined)
    return;

  if (routeName !== installationProgress.value) {
    if (installationSteps.indexOf(routeName) > installationSteps.indexOf(installationProgress.value))
      router.push({name: installationProgress.value});
    else
      installationProgress.value = routeName;
  }

  if (routeName === 'language') {
    component.value = LanguageSetting
  }
  if (routeName === 'registration') {
    readyForNextStep.value = registration.state == "finished";
    switch (registration.state) {
      case "input":
        component.value = RegistrationForm;
        break;
      case "validation":
        component.value = RegistrationVerification;
        break;
      case "finished":
        component.value = RegistrationFinished;
        break;
    }
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
        installationProgress.value = 'sources';
        break;
      case "sources":
        await store.saveSources();
        await httpService.setInstallationStateComplete()
            .then(()=>{
              ToastService.sendNotification("Save Success","success",3000);
            })
            .catch(()=>{
              ToastService.sendNotification("Save Error","error",3000);
            });
        break;
    }
  } catch {
    return;
  }

  if (installationSteps.indexOf(routeName) == installationSteps.length - 1) {
    removeRegistration();
    await router.push("/home")
    return;
  }

  await router.push({name: installationSteps[installationSteps.indexOf(routeName) + 1]});

}
</script>

<style scoped>
nav {
  margin: 0;
  padding: 14px 10px 10px 10px;
}

main {
  background-color: rgb(var(--secondary-color));
  border-radius: var(--border-radius-medium);
  width: calc(100svw - 20px);
  height: calc(100vh - 100px - 75px);
  padding: 0;
  margin: 10px;
  overflow-y: auto;
}

header {
  position: sticky;
  top: 0;
  background-color: rgb(var(--secondary-color));

  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
  width: 100%;

  .step-name {
    width: calc(100% - 166px);
    display: flex;
    flex-direction: row;
    justify-content: left;

    padding-left: 25px;
    padding-right: 25px;
    font-size: var(--font-size-big);
    font-weight: bold;

    .darken {
      color: rgb(var(--tertiary-color));
      min-width: 0;
      width: fit-content;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
}

.header-image {
  height: calc(var(--font-size-big) * 1.5);
  aspect-ratio: 2/1;
  padding: 25px;
}

.button-container {
  display: grid;
  margin: 20px 10px 10px 10px;
  grid-template-columns: minmax(0, 1fr) 20px minmax(0, 1fr);
  width: calc(100% - 20px);
}

.button {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  background-color: rgb(var(--secondary-color));
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
  background-color: rgb(var(--primary-color));
}

@media screen and (min-width: 600px) {
  nav {
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