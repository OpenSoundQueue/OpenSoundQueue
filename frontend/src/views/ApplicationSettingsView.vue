<template>
  <main :class="{'show-mode-switcher': hasAllManagementPermissions}">
    <AdminNavigation v-show="hasAllManagementPermissions"/>
    <div class="settings-container">
      <h2>Privacy</h2>
      <ToggleSwitch :checked="checked" @click="checked = !checked"/>
      <h2>Sources</h2>
      <h2>Default Language</h2>
    </div>
    <GridBackground/>
  </main>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";

import AdminNavigation from "@/components/AdminNavigation.vue";
import {PermissionService} from "@/services/PermissionService";
import GridBackground from "@/components/background/GridBackground.vue";
import ToggleSwitch from "@/components/buttons/ToggleSwitch.vue";

const checked = ref(false);

const hasAllManagementPermissions = ref(true);

onMounted(async ()=>{
  await PermissionService.getPermissions();

  hasAllManagementPermissions.value = PermissionService.hasAllPermissions(["MANAGE_ROLES","MANAGE_USER"])
})
</script>

<style scoped>
main {
  width: calc(100% - 30px);
  height: calc(100vh - 60px);
  margin: 0 auto 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  box-sizing: border-box;
  padding-top: 20px;
}

.settings-container {
  overflow-y: hidden;
  display: flex;
  flex-direction: column;
  height: 100%;
}

@media screen and (min-width: 800px) {
  main {
    width: 800px;
  }
}

@media screen and (min-width: 1250px) {
  main {
    width: 1250px;
    display: grid;
    justify-content: space-between;
    grid-template-rows: calc(100% - 30px);
  }

  main.show-mode-switcher {
    grid-template-rows: 60px calc(100% - 90px);

    .settings-container {
      grid-row: 2;
    }
  }

  .settings-container {
    grid-column: 1;
    padding: 20px;
    box-sizing: border-box;
    grid-row: 1;
    height: 100%;
    width: 100%;
    border-bottom: none;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
    display: flex;
    flex-direction: column;
  }
}
</style>