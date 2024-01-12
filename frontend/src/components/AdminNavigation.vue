<template>
  <div class="admin-navigation-wrapper">
    <nav>
      <div class="mode-switcher">
        <router-link to="/admin/roles" class="link">{{ $translate('adminPage.nav.roles') }}</router-link>
        <router-link to="/admin/users" class="link">{{ $translate('adminPage.nav.users') }}</router-link>
      </div>
    </nav>
  </div>
</template>

<script setup lang="ts">

</script>

<style scoped>

.mode-switcher {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 15px;
  height: 35px;
}

.link {
  height: 35px;
  width: 100%;
  background: var(--background-color);
  border: 3px solid var(--secondary-color);
  box-sizing: border-box;
  color: var(--text-color);
  font-size: var(--font-size-medium);
  border-radius: var(--border-radius-medium);
  display: flex;
  justify-content: center;
  align-items: center;
  text-decoration: none;
}

.link.router-link-active {
  color: var(--background-color);
  border-color: var(--tertiary-color);
  background: var(--tertiary-color);
  font-weight: bold;
}

</style>