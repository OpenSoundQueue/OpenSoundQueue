<template>
  <div class="navbar-wrapper">
    <nav class="navbar">
      <div @click="toggleMenu" class="open-button mobile">
        <div>
          <img src="@/assets/icons/burger_menu.svg"/>
        </div>
      </div>
      <div class="logo-container">
        <router-link to="/">
          <img class="logo" src="@/assets/menu/logo.svg">
        </router-link>
      </div>
      <div class="menu" :class="{opened: menuIsOpen, closed: !menuIsOpen}">
        <div @click="toggleMenu" class="close-button mobile">
          <img src="@/assets/icons/input/delete.svg"/>
        </div>
        <div class="links">
          <div class="quick-nav-links">
            <Link path="/" label="Public" :is-outer-area="true"/>
            <Link path="/settings" label="Settings"/>
            <Link path="/home" label="Home"/>
            <Link path="/admin" label="Admin"/>
          </div>
          <div class="other-nav-links">
            <Link path="https://opensoundqueue.org/team" label="Team" :is-external="true"/>
            <Link path="https://opensoundqueue.org/team" label="About" :is-external="true"/>
          </div>
        </div>
      </div>
      <div class="user-button">
        <img src="@/assets/icons/user.svg"/>
      </div>
    </nav>
  </div>
</template>

<script setup lang="ts">
import Link from "@/components/Link.vue";
import {ref} from "vue";

const menuIsOpen = ref(false);

function toggleMenu() {
  menuIsOpen.value = !menuIsOpen.value;
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
  height: 60px;
  width: 60px;
  display: grid;
  place-items: center;
}

.close-button img {
  height: 35px;
}

.menu {
  position: fixed;
  width: 66%;
  left: 0;
  top: 0;
  height: calc(100% - 10px);
  margin-top: 5px;
  background: var(--secondary-color);
  border: 2px solid var(--tertiary-color);
  border-left: none;
  box-sizing: border-box;
  border-radius: 0 var(--border-radius-big) var(--border-radius-big) 0;
  transition: 0.1s transform ease-in;
}

.menu.closed {
  transform: translateX(-100%);
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
  }

  .menu.closed {
    transform: none;
  }

  .links {
    flex-direction: row;
    margin: 0;
  }

  .quick-nav-links, .other-nav-links {
    flex-direction: row;
  }
}
</style>