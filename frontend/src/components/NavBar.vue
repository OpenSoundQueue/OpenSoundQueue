<template>
  <div class="navbar-wrapper">
    <nav class="navbar">
      <router-link to="/">
        <img class="logo" src="@/assets/menu/logo.svg">
      </router-link>
    </nav>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.navbar-wrapper {
  position: fixed;
  top: 0;
  height: 60px;
  width: 100%;
  border-bottom: 2px var(--dark-gray) solid;
  z-index: 2;
  background: var(--background-color-transparent);
  display: flex;
}

.navbar {
  width: 100%;
  padding: 0 20px 0 20px;
  margin: auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  height: 30px;
}

.menu-item > * {
  padding: 0;
  margin: 0;
}

@media screen and (min-width: 1250px) {
  .navbar {
    width: 1250px;
  }
}
</style>