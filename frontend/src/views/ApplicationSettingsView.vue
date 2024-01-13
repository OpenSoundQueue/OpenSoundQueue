<template>
  <main :class="{'show-mode-switcher': hasAllManagementPermissions}">
    <AdminNavigation v-show="hasAllManagementPermissions"/>
    <div v-if="store.areApplicationSettingsEdited">
      <button @click="store.rollback">rollback</button>
      <button @click="store.save">save</button>
    </div>
    <div class="settings-container" v-if="store.editedApplicationSettings">
      <!-- Privacy -->
      <h2>Privacy</h2>
      <ToggleSwitch :checked="store.editedApplicationSettings.requireEmailAuth" @click="toggleRequireEmailAuth"/>
      <ToggleSwitch :checked="store.editedApplicationSettings.isPrivate" @click="toggleIsPrivate"/>
      <InputField v-if="store.editedApplicationSettings.isPrivate"
                  v-model="store.editedApplicationSettings.entryCode"
                  :manual-value="store.editedApplicationSettings.entryCode"/>
      <!-- Sources -->
      <h2>Sources</h2>
      <div v-for="(supportedSource, index) in store.editedApplicationSettings.supportedSources"
           :key="index"
           @click="() => toggleSource(supportedSource)">
        {{ supportedSource }}
        <Checkbox :checked="store.editedApplicationSettings.sources.includes(supportedSource)"/>
      </div>
      <!-- Default Language -->
      <h2>Default Language</h2>
      <div v-for="(language, index) of Object.keys(translations)"
           :key="index"
           @click="() => setLanguage(language)"
           class="language-wrapper"
           :class="[store.editedApplicationSettings.language === language ? 'selected' : '']">
        <div>{{ $translate(`languages.${language}`) }}</div>
        <svg v-show="store.editedApplicationSettings.language === language" xmlns="http://www.w3.org/2000/svg"
             viewBox="0 -960 960 960" :alt="$translate('altTexts.check')">
          <path d="M382-240 154-468l57-57 171 171 367-367 57 57-424 424Z"/>
        </svg>
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

function toggleSource(source: string) {
  if (!store.editedApplicationSettings) {
    return;
  }

  store.toggleSource(source);
}

function setLanguage(language: string) {
  if (!store.editedApplicationSettings) {
    return;
  }

  store.setLanguage(language);
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

.language-wrapper {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  width: 100%;
  padding: calc(var(--font-size-medium) / 2);

  border: solid 2px var(--cool-gray);
  border-radius: var(--border-radius-medium);
  box-sizing: border-box;


  &&.selected {
    background-color: var(--cool-gray-transparent);
    border-color: var(--cool-gray);
    color: var(--dark-blue);
    font-weight: bold;
  }

  svg {
    height: var(--font-size-medium);
    fill: var(--dark-blue);
    aspect-ratio: 1;
  }
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