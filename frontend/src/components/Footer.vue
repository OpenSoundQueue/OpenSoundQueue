<template>
  <div class="footer-container">
    <img class="logo" src="@/assets/logo/logo_and_text.svg">
    <div class="body-container">
      <div class="link-container">
        <router-link to="/imprint">GitHub</router-link>
        <router-link to="/imprint">{{ $translate("documentation") }}</router-link>
        <router-link to="/imprint">{{ $translate("team") }}</router-link>
        <router-link to="/imprint">{{ $translate("imprint") }}</router-link>
        <router-link to="/imprint">{{ $translate("about") }}</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {getCurrentLanguage} from "@/plugins/TranslationPlugin";

function linktToEasyname(): void {
  window.open('https://www.easyname.at/' + getCurrentLanguage().value, "_blank");
}
</script>

<style scoped>
.footer-container {
  font-size: var(--font-size-small);
  background-color: var(--dark-blue);
  padding: 20px;
  border-top: solid 1px var(--dark-gray);
}

.body-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding-top: 20px;
  padding-bottom: 20px;
  color: var(--text-color);
}

.logo {
  height: calc(var(--font-size-big) * 1.25);
}

.link-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  gap: 20px;

  margin: auto 0 auto 0;
}

.link-container > * {
  flex-basis: calc(50% - 10px);
  margin-right: 20px;
  margin-bottom: 20px;
  box-sizing: border-box;
}

.link-container > *:hover {
  cursor: pointer;
  text-decoration: underline;
}

.link-container > *, .sponsor-container > * {
  padding: 0;
  margin: 0;
  text-decoration: none;
  color: var(--text-color);
}
</style>