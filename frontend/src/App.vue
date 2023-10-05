<script setup lang="ts">
import NavBar from "@/components/NavBar.vue";
</script>

<template>
  <div class="navbar-container">
    <NavBar></NavBar>
  </div>
  <RouterView/>
</template>

<style scoped>
.navbar-container {
  margin-bottom: 60px;
}
</style>
