<template>
  <main>
    <div class="background-gradient">
      <div class="mode-switcher">
        <router-link to="/home/basic" class="link">Basic</router-link>
        <router-link to="/home/advanced" class="link">Advanced</router-link>
      </div>
    </div>
  </main>
</template>

<script setup lang="ts">

</script>

<style scoped>
main {
  height: calc(100svh - 60px);
  background-image: url("@/assets/background/grid-background_big.png");
  background-size: 300px;
}

.background-gradient {
  background: linear-gradient(180deg, rgba(0, 0, 0, 0.00) 0%, var(--background-color) 54.69%);
  height: 100%;
  width: 100%;
}

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