<template>
  <main>
    <header>
      <router-link to="/">
        <img class="header-image" src="@/assets/icons/arrows/keyboard_arrow_left.svg">
      </router-link>
      <img class="header-image" src="@/assets/logo/logo_white.svg">
    </header>
    <Login
        :passwordRequired="true"
    ></Login>
    <div class="link-container">
      <router-link class="link" to="/forgot-password">{{ $translate("forgotPassword") }}</router-link>
      <router-link class="link" to="/register">{{ $translate("createAccount") }}</router-link>
    </div>
  </main>
</template>

<script setup lang="ts">
import Login from "@/components/Login.vue"
</script>

<style scoped>
main {
  background-color: var(--grayish-blue);
  width: 100svw;
  height: 100vh;
  margin: 0;
  padding: 0;
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

.link-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin: 20px 0 20px 0;
}

.link {
  text-decoration: none;
  color: var(--primary-color);
  text-align: center;
}

.link:hover {
  text-decoration: underline;
}
</style>