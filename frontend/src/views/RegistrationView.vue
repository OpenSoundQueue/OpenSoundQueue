<template>
  <main>
    <header>
      <div class="link-back" @click="router.back()">
        <img class="header-image" src="@/assets/icons/arrows/keyboard_arrow_left.svg"
             :alt="$translate('altTexts.arrowBack')">
      </div>
      <img class="header-image" src="@/assets/logo/logo_white.svg" :alt="$translate('altTexts.logo')">
    </header>
    <component :is="component"
               @continue="() => {
                 component = RegistrationVerification
                 localStorageService.setRegisterPosition(1)
               }"
               @change="() => {
                 component = RegistrationForm
                 localStorageService.setRegisterPosition(0)
               }"
               @validated="()=>{
                removeRegistration()
                router.push('/home')
              }"
    />
  </main>

</template>

<script setup lang="ts">
import RegistrationForm from "@/components/registration/RegistrationForm.vue";
import RegistrationVerification from "@/components/registration/RegistrationVerification.vue";
import {onMounted, shallowRef} from "vue";
import type {ShallowRef, Component} from "vue";
import router from "@/router";
import * as localStorageService from "@/services/localStorageService"
import {removeRegistration} from "@/store/registration";

const component: ShallowRef<Component> = shallowRef(RegistrationForm);

onMounted(() => {
  if (localStorageService.getRegisterPosition() > 0) {
    component.value = RegistrationVerification;
  }
})

function linkBack() {
  if (component.value == RegistrationForm) {
    router.back()
  } else {
    localStorageService.setRegisterPosition(0)
    component.value = RegistrationForm
  }
}

//TODO: Add possibility to register a user without email
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
  justify-content: space-between;
}

.header-image {
  height: calc(var(--font-size-big) * 1.5);
  padding: 25px;
}

.link-back:hover {
  cursor: pointer;
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