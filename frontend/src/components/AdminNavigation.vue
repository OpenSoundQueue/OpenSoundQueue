<template>
  <div>
    <nav>
      <div class="mode-switcher">
        <router-link to="/admin/roles" class="link">{{ $translate('adminPage.nav.roles') }}</router-link>
        <router-link to="/admin/users" class="link">{{ $translate('adminPage.nav.users') }}</router-link>
        <router-link to="/admin/application-settings" class="link">{{ $translate('applicationSettings.title') }}</router-link>
      </div>
    </nav>
  </div>
</template>

<style scoped>
nav {
  height: 60px;
}

.mode-switcher {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 15px;
  height: 40px;
}

.link {
  height: 40px;
  width: 100%;
  background: rgb(var(--background-color));
  border: 3px solid rgb(var(--secondary-color));
  box-sizing: border-box;
  color: rgb(var(--text-color));
  font-size: var(--font-size-medium);
  border-radius: var(--border-radius-medium);
  display: flex;
  justify-content: center;
  align-items: center;
  text-decoration: none;
  text-align: center;
}

.link.router-link-active {
  color: rgb(var(--background-color));
  border-color: rgb(var(--tertiary-color));
  background: rgb(var(--tertiary-color));
  font-weight: bold;
}

@media screen and (min-width: 650px) {
  nav {
    width: 650px;
  }
}
</style>