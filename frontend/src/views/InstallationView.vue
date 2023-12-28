<template>
  <router-link to="/installation/language">Language</router-link>
  <br>
  <router-link to="/installation/privacy">Privacy</router-link>
  <br>
  <router-link to="/installation/register">Register</router-link>
  <br>
  <router-link to="/installation/sources">Sources</router-link>
  <br>
  <component :is="component"/>
</template>

<script setup lang="ts">
import {onMounted, shallowRef, watch} from "vue";
import type {Component, ShallowRef} from "vue";
import {HttpService} from "@/services/HttpService";
import LanguageSetting from "@/components/installation/LanguageSetting.vue";
import PrivacySetting from "@/components/installation/PrivacySetting.vue";
import SourceSetting from "@/components/installation/SourceSetting.vue";
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

  if (routeName === 'redirect') router.push("/installation/language")

  if (routeName === 'language') component.value = LanguageSetting
  if (routeName === 'register') component.value = LanguageSetting
  if (routeName === 'privacy') component.value = PrivacySetting
  if (routeName === 'sources') component.value = SourceSetting
}
</script>

<style scoped>

</style>