<script setup lang="ts">
import NavBar from "@/components/NavBar.vue";
import {computed} from "vue";
import Toast from "@/components/toast/Toast.vue";
import {ToastService} from "@/services/ToastService";
import {PopUpService} from "@/services/PopUpService";
import FullScreenPopUp from "@/components/PopUp/FullScreenPopUp.vue";
import router from "@/router";

const routesWithoutNavBar = ["login", "register", "installation"];
const displayNavBar = computed(() => {
  const pathSegments = router.currentRoute.value.matched;

  for (const pathSegment of pathSegments) {
    if (routesWithoutNavBar.includes(<string>pathSegment.name)) return false;
  }

  return true;
})
</script>

<template>
  <Toast @close="() => ToastService.close()"
         :message="ToastService.message.value"
         :is-visible="ToastService.isVisible.value"
         :message-level="ToastService.messageLevel.value"
  />
  <FullScreenPopUp v-show="PopUpService.isVisible.value"
                   :message="PopUpService.message.value"
                   :button-text="PopUpService.buttonText.value"
                   @Update:PopUpStatus="PopUpService.updatePopUp($event ||'')"/>
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
