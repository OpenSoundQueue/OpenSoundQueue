<template>
  <main>
    <div class="settings-wrapper">
      <div class="settings-nav">
        <img class="back" src="@/assets/icons/arrows/arrow_back.svg">
        <img class="logo" src="@/assets/logo.svg">
      </div>
      <div class="settings-header">
        <img src="@/assets/icons/settings.svg">
        <h1>{{ $translate('settings')}}</h1>
      </div>
      <TranslateButton/>
    </div>
  </main>
</template>

<script setup lang="ts">

import TranslateButton from "@/components/settings/TranslateButton.vue";
</script>


<style scoped>
.settings-wrapper {
  background: var(--secondary-color);
  padding: 30px;
}

.settings-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.settings-nav .logo {
  height: 32px;
}

.settings-nav .back {
  height: 32px;
}

.settings-header {
  display: flex;
  align-items: center;
  gap: 20px;
}

.settings-header img {
  height: 40px;
}

h1 {
  font-weight: bold;
}

@media screen and (min-width: 800px) {
  .settings-wrapper {
    border-radius: var(--border-radius-big);
    width: 750px;
    height: 80vh;
  }
}
</style>