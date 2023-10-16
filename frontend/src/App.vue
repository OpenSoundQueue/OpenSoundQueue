<script setup lang="ts">
import NavBar from "@/components/NavBar.vue";
import {computed, ref} from "vue";
import type {Ref} from "vue";
import {useRoute} from "vue-router";

const route = useRoute();
const shouldShowNavBar = ref(false);
const routeName: Ref<string> = computed(() => route.name == undefined ? "" : route.name.toString())
const routesWithoutNavBar = ["login"];

setInterval(checkRoute, 1000);

function checkRoute() {
  if (!routesWithoutNavBar.includes(routeName.value) && routeName.value !== "") {
    shouldShowNavBar.value = true;
  }
}

</script>

<template>
  <div class="navbar-container" v-if="shouldShowNavBar">
    <NavBar></NavBar>
  </div>
  <RouterView/>
</template>

<style scoped>
.navbar-container {
  margin-bottom: 60px;
}
</style>
