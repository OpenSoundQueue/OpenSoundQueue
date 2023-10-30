<script setup lang="ts">
import NavBar from "@/components/NavBar.vue";
import {computed} from "vue";
import Toast from "@/components/toast/Toast.vue";
import {ToastService} from "@/services/ToastService";
import {PopUpService} from "@/services/PopUpService";
import {useRoute} from "vue-router";
import FullScreenPopUp from "@/components/PopUp/FullScreenPopUp.vue";

const route = useRoute();
const routesWithoutNavBar = ["login"];
const routeName = computed(() => route.name == undefined ? "" : route.name.toString())
const displayNavBar = computed(() => !routesWithoutNavBar.includes(routeName.value) && routeName.value !== "")
</script>

<template>
  <Toast @close="() => ToastService.close()"
         :message="ToastService.message.value"
         :is-visible="ToastService.isVisible.value"
         :message-level="ToastService.messageLevel.value"
  />
  <FullScreenPopUp v-show="PopUpService.isVisible.value"
                   :message="PopUpService.message.value"
                   @Update:PopUpStatus="PopUpService.updatePopUp($event)"/>
  <div class="navbar-container" v-if="displayNavBar">
    <NavBar></NavBar>
  </div>
  <RouterView/>
</template>

<style scoped>
.navbar-container {
  margin-bottom: 60px;
}
</style>
