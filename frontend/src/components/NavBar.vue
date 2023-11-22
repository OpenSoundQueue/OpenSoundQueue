<template>
  <div class="navbar-wrapper">
    <nav class="navbar">
      <div @click="navIsOpen = true" class="open-button mobile">
        <div>
          <img src="@/assets/menu/menu.svg"/>
        </div>
      </div>
      <div class="logo-container">
        <router-link to="/">
          <img class="logo" src="@/assets/menu/logo.svg">
        </router-link>
      </div>
      <div class="menu" :class="{opened: navIsOpen, closed: !navIsOpen}" v-closable="{excluded: [], handler: () => navIsOpen = false}">
        <div @click="navIsOpen = false" class="close-button mobile">
          <img src="@/assets/menu/close.svg"/>
        </div>
        <div class="links">
          <div class="quick-nav-links">
            <Link path="/" :label="$translate('navBar.public')" :is-outer-area="!userIsInPublicArea"/>
            <Link path="/home" :label="$translate('navBar.home')" :is-outer-area="userIsInPublicArea"/>
            <Link path="/settings" :label="$translate('navBar.settings')" :is-outer-area="userIsInPublicArea"/>
          </div>
          <div class="other-nav-links">
            <Link path="https://opensoundqueue.org/" :label="$translate('navBar.project')" :is-external="true"/>
            <Link path="https://opensoundqueue.org/team" :label="$translate('navBar.team')" :is-external="true"/>
          </div>
        </div>
      </div>
      <div class="user-button" @click="userMenuIsOpen = true">
        <img src="@/assets/icons/user.svg"/>
      </div>
    </nav>
    <div v-show="userMenuIsOpen" v-closable="{exclude: [], handler: () => userMenuIsOpen = false}">
      <UserMenu @close="() => userMenuIsOpen = false"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import Link from "@/components/Link.vue";
import {onMounted, ref, watch} from "vue";
import router from "@/router";
import UserMenu from "@/components/UserMenu.vue";

const navIsOpen = ref(false);
const userMenuIsOpen = ref(false);
const userIsInPublicArea = ref(true);

watch(router.currentRoute, () => {
  determineArea();

  navIsOpen.value = false;
})

onMounted(() => {
  determineArea();
})

function determineArea() {
  userIsInPublicArea.value = router.currentRoute.value.name === "public";
}
</script>

<style scoped>
.navbar-wrapper {
  position: fixed;
  top: 0;
  height: 60px;
  width: 100%;
  border-bottom: 2px var(--dark-gray) solid;
  z-index: 2;
  background: var(--background-color);
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

.open-button img {
  height: 15px;
}

.user-button img {
  height: 30px;
}

.close-button {
  height: calc(60px - 5px - 2px);
  width: 60px;
  display: grid;
  place-items: center;
}

.close-button img {
  height: 20px;
}

.menu {
  position: fixed;
  width: 66%;
  left: 0;
  top: 0;
  height: calc(100% - 10px);
  margin: 5px 0 0 5px;
  background: var(--secondary-color);
  border: 2px solid var(--tertiary-color);
  border-radius: var(--border-radius-big);
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  padding-bottom: 30px;
  transition: 0.1s transform ease-in;
}

.menu.closed {
  transform: translateX(calc(-100% - 5px));
}

.links {
  height: 100%;
  margin-left: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.quick-nav-links, .other-nav-links {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.logo {
  height: 30px;
}

.menu-item > * {
  padding: 0;
  margin: 0;
}

@media screen and (min-width: 800px) {
  .mobile {
    display: none;
  }

  .navbar {
    gap: 30px;
  }

  .menu {
    position: static;
    background: none;
    border: none;
    height: fit-content;
    width: 100%;
    flex-direction: row;
    padding: 0;
  }

  .menu.closed {
    transform: none;
  }

  .links {
    flex-direction: row;
    margin: 0;
    width: 100%;
  }

  .quick-nav-links, .other-nav-links {
    flex-direction: row;
  }
}
</style>