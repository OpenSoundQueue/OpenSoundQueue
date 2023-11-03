<template>
  <div class="mode-switcher">
    <router-link to="/home/basic" class="link">Basic</router-link>
    <router-link to="/home/advanced" class="link">Advanced</router-link>
  </div>
</template>

<script setup lang="ts">

</script>

<style scoped>
.mode-switcher {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 15px;
  height: 50px;
}

.mode-switcher .link {
  height: 35px;
  width: 100%;
  background: var(--background-color);
  border: 2px solid var(--secondary-color);
  color: var(--text-color);
  font-size: var(--font-size-medium);
  border-radius: var(--border-radius-medium);
  display: flex;
  justify-content: center;
  align-items: center;
  text-decoration: none;
}

.link.router-link-exact-active {
  color: var(--background-color);
  border-color: var(--tertiary-color);
  background: var(--tertiary-color);
  font-weight: bold;
}
</style>