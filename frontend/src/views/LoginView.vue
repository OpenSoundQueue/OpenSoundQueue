<template>
  <main class="scrollbar">
    <header>
      <router-link to="/">
        <img class="header-image" src="@/assets/icons/arrows/keyboard_arrow_left.svg"
             :alt="$translate('altTexts.arrowBack')">
      </router-link>
      <img class="header-image" src="@/assets/logo/logo_white.svg" :alt="$translate('altTexts.logo')">
    </header>
    <Login
        :isPrivate="isPrivate"
        :requireAuth="requireAuth"
    ></Login>
    <div class="link-container">
      <router-link class="link" to="/forgot-password">{{ $translate("forgotPassword") }}</router-link>
      <router-link class="link" to="/register">{{ $translate("createAccount") }}</router-link>
    </div>
  </main>
</template>

<script setup lang="ts">
import Login from "@/components/Login.vue"
import {onMounted, ref} from "vue";
import {HttpService} from "@/services/HttpService";

const httpService = new HttpService();
const isPrivate = ref(true);
const requireAuth = ref(true);

onMounted(() => {
  httpService.getLoginState()
      .then((data) => {
        isPrivate.value = data.isPrivate == 'true';
        requireAuth.value = data.requireAuth == 'true';
        console.log(isPrivate.value);
        console.log(requireAuth.value);
      })
})
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

.link-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 40px 0 40px 0;
}

.link {
  text-decoration: none;
  color: var(--pink);
  text-align: center;
}

.link:hover {
  text-decoration: underline;
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