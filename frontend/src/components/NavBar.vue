<template>
  <nav class="navbar">
    <router-link to="/">
      <img class="logo" src="@/assets/menu/logo.svg">
    </router-link>
    <TranslateButton class="translate-button"></TranslateButton>
  </nav>
</template>

<script setup lang="ts">
import TranslateButton from "@/components/buttons/TranslateButton.vue";
</script>


<style scoped>
.navbar {
  position: fixed;
  top: 0;
  height: 88px;
  width: 100%;
  border-bottom: 2px var(--dark-gray) solid;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--background-color);
  z-index: 999;
}

.translate-button {
  margin-right: 20px;
}

.logo {
  height: 40px;
  margin: 0 32px 0 32px;
}

.menu-item > * {
  padding: 0;
  margin: 0;
}
</style>