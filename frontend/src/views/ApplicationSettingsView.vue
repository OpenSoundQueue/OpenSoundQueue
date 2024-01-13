<template>
  <main :class="{'show-mode-switcher': hasAllManagementPermissions}">
    <AdminNavigation v-show="hasAllManagementPermissions"/>
    <div v-if="store.areApplicationSettingsEdited">edited</div>
    <div class="settings-container" v-if="store.editedApplicationSettings">
      <h2>Privacy</h2>
      <ToggleSwitch :checked="store.editedApplicationSettings.requireEmailAuth" @click="toggleRequireEmailAuth"/>
      <ToggleSwitch :checked="store.editedApplicationSettings.isPrivate" @click="toggleIsPrivate"/>
      <InputField v-if="store.editedApplicationSettings.isPrivate"
                  v-model="store.editedApplicationSettings.entryCode"
                  :manual-value="store.editedApplicationSettings.entryCode"/>
      <h2>Sources</h2>
      <div v-for="(supportedSource, index) in store.editedApplicationSettings.supportedSources" :key="index">
        {{ supportedSource }}
        <Checkbox :checked="store.editedApplicationSettings.sources.includes(supportedSource)"/>
      </div>
      <h2>Default Language</h2>
      <div v-for="(language, index) of Object.keys(translations)" :key="index">
        <div><span v-if="language === store.editedApplicationSettings.language">> </span>{{ $translate(`languages.${language}`) }}</div>
      </div>
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
import {useApplicationSettingsStore} from "@/stores/ApplicationSettings";
import InputField from "@/components/inputs/InputField.vue";
import Checkbox from "@/components/buttons/Checkbox.vue";
import {translations} from "@/plugins/TranslationPlugin";

const store = useApplicationSettingsStore();

const hasAllManagementPermissions = ref(true);

onMounted(async () => {
  await PermissionService.getPermissions();

  hasAllManagementPermissions.value = PermissionService.hasAllPermissions(["MANAGE_ROLES", "MANAGE_USER"])

  await store.fetchApplicationSettings();
})

function toggleRequireEmailAuth() {
  if (!store.editedApplicationSettings) {
    return;
  }

  store.setRequireEmailAuth(!store.editedApplicationSettings.requireEmailAuth);
}

function toggleIsPrivate() {
  if (!store.editedApplicationSettings) {
    return;
  }

  store.setIsPrivate(!store.editedApplicationSettings.isPrivate);
}
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
  }

  main.show-mode-switcher {
    height: calc(100vh - 60px - 30px);
  }

  .settings-container {
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