<template>
  <div class="footer-wrapper">
    <div class="footer-container">
      <img class="logo" src="@/assets/logo/logo_and_text.svg"><!-- Alt text -->
      <div class="body-container">
        <div class="link-container">
          <router-link to="/imprint">GitHub</router-link>
          <router-link to="/imprint">{{ $translate("documentation") }}</router-link>
          <router-link to="/imprint">{{ $translate("team") }}</router-link>
          <a href="https://opensoundqueue.org/imprint" target="_blank">{{ $translate("imprint") }}</a>
          <router-link to="/imprint">{{ $translate("about") }}</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.footer-wrapper {
  position: relative;
  bottom: 0;
  font-size: var(--font-size-small);
  background-color: var(--dark-blue);
  border-top: solid 1px var(--dark-gray);
  width: 100%;
}

.footer-container {
  margin: 20px auto 0 auto;
  padding: 0 20px 0 20px;
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

@media screen and (min-width: 1250px) {
  .footer-container {
    width: 1250px;
  }
}
</style>