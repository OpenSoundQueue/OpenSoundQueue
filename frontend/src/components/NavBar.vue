<template>
  <nav class="navbar">
    <img class="menu-icon" :src="menuIcon" @click="toggleMenu">
    <router-link to="/">
      <img class="logo" src="@/assets/menu/logo.svg">
    </router-link>
    <div class="menu-wrapper" :class="[menuOpen ? 'menu-opened' : 'menu-closed']">
      <div class="menu-items-container">
        <div class="menu-icon-container">
          <img class="menu-icon" :src="menuIcon" @click="toggleMenu">
        </div>
        <div class="current-page-container">
          <p class="menu-title">{{ $translate("menuCurrentPage") }}</p>
          <router-link class="menu-item" :to="currentPage.link">
            <p>{{ $translate(currentPage.translationKey) }}</p>
          </router-link>
        </div>
        <div class="next-pages-container">
          <p class="menu-title">{{ $translate("menuNextPages") }}</p>
          <router-link v-for="(page, index) in nextPages" class="menu-item" :to="page.link" :key="index">
            <p>{{ $translate(page.translationKey) }}</p>
          </router-link>
        </div>
        <div class="desktop-menu">
          <router-link v-for="(page, index) in pages" class="menu-item" :to="page.link" :key="index">
            <p>{{ $translate(page.translationKey) }}</p>
          </router-link>
        </div>
      </div>
      <div class="close-box" @click="toggleMenu"></div>
    </div>
    <TranslateButton class="translate-button"></TranslateButton>
  </nav>
</template>

<script setup lang="ts">
import TranslateButton from "@/components/buttons/TranslateButton.vue";
import {ref} from "vue";
import type {TranslationsKey} from "@/plugins/TranslationPlugin";


const menuOpen = ref(false);
const menuIcon = ref("src/assets/menu/menu.svg");

type Page = {
  translationKey: TranslationsKey,
  link: string
}
const pages: Array<Page> = [
  {translationKey: "menuTeam", link: "/team"},
  {translationKey: "menuIdea", link: "/idea"},
  {translationKey: "menuFeatures", link: "/features"},
];

const nextPages: Array<Page> = Object.assign([], pages).splice(0, 1);
let currentPage: Page = pages[0];

function toggleMenu(): void {
  menuOpen.value = !menuOpen.value;
  if (menuOpen.value) {
    menuIcon.value = "src/assets/menu/close.svg";
  } else {
    menuIcon.value = "src/assets/menu/menu.svg";
  }
}
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

.next-pages-container {
  display: flex;
  flex-direction: column;
}

.desktop-menu {
  display: none;
}

.translate-button {
  margin-right: 20px;
}

.logo, .menu-icon {
  height: 40px;
  margin: 0 32px 0 32px;
}

.menu-wrapper {
  position: fixed;
  top: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  transition: .2s;
  z-index: 99999;
}

.menu-wrapper.menu-closed {
  left: -100vw;
}

.menu-wrapper.menu-opened {
  left: 0;
}

.menu-items-container {
  height: 100%;
  background: var(--magenta);
  opacity: 0.75;
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-right: 40px;
}

.menu-icon-container {
  height: 88px;
  display: flex;
  align-items: center;
}

.close-box {
  width: 30%;
}

.menu-title {
  color: var(--text-color);
  font-size: var(--font-size-medium);
}

.menu-title, .menu-item {
  padding-left: 40px;
}

.menu-item {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 10px;

  margin-bottom: 20px;
  color: white;
  text-decoration: none;
  font-size: var(--font-size-big);
}

.menu-item > * {
  padding: 0;
  margin: 0;
}

.router-link-active {
  font-weight: bold;
  letter-spacing: 1px;
}


.album-cover {
  height: 30px;
  aspect-ratio: 1/1;
}


@media screen and (min-width: 740px) {
  .menu-icon {
    display: none;
  }

  .menu-wrapper {
    position: static;
    height: 100%;
  }

  .menu-items-container {
    flex-direction: row;
    align-items: center;
    justify-content: space-evenly;
    padding: 0;
    background: none;
    width: 100%;
  }

  .current-page-container, .next-pages-container {
    display: none;
  }

  .desktop-menu {
    display: flex;
    flex-direction: row;
    justify-content: space-evenly;
    width: 100%;
  }

  .menu-item {
    padding: 0;
    margin: auto 0 auto 0;
  }

  .close-box {
    display: none;
  }
}

@media screen and (min-width: 1200px) {
  .menu-wrapper {
    position: static;
    height: 100%;
    width: 700px;
  }
}
</style>