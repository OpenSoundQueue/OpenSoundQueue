<template>
  <main :class="{'show-mode-switcher': hasAllManagementPermissions}">
    <AdminNavigation v-show="hasAllManagementPermissions"/>
    <div class="settings-wrapper" v-if="store.editedApplicationSettings">
      <div v-show="store.areApplicationSettingsEdited" class="save-button">
        <img class="undo" src="@/assets/icons/undo.svg"
             :alt="translate('altTexts.undo')"
             :title="translate('roleEdit.rollback')"
             @click="store.rollback"/>
        <DynamicButton b-style="save" :status="buttonState" @click="save">{{
            translate('roleEdit.save')
          }}
        </DynamicButton>
      </div>
      <div class="settings-container">
        <!-- Privacy -->
        <div class="settings-heading">{{ $translate('applicationSettings.privacy.title') }}</div>
        <div class="toggle-switch-wrapper" @click="toggleRequireEmailAuth">
          <div class="description-container">
            <div class="label">{{ $translate('applicationSettings.privacy.emailAuth.title') }}</div>
            <p>{{ $translate('applicationSettings.privacy.emailAuth.description') }}</p>
          </div>
          <div class="interactive-container">
            <ToggleSwitch :checked="store.editedApplicationSettings.requireEmailAuth"/>
          </div>
        </div>
        <div class="toggle-switch-wrapper" @click="toggleIsPrivate">
          <div class="description-container">
            <div class="label">{{ $translate('applicationSettings.privacy.privateRoom.title') }}</div>
            <p>{{ $translate('applicationSettings.privacy.privateRoom.description') }}</p>
          </div>
          <div class="interactive-container">
            <ToggleSwitch :checked="store.editedApplicationSettings.isPrivate"/>
          </div>
        </div>
        <div class="input-field-container">
          <div class="interactive-container">
            <InputField v-if="store.editedApplicationSettings.isPrivate"
                        :label="$translate('applicationSettings.privacy.entryCode.title')"
                        v-model="store.editedApplicationSettings.entryCode"
                        :manual-value="store.editedApplicationSettings.entryCode"
                        :validation-function="$validateEntryCode"
                        :validation-message="$translate('entryCode.validation')"
            />
          </div>
        </div>
        <!-- Sources -->
        <div class="settings-heading">{{ $translate('applicationSettings.sources.title') }}</div>
        <div class="sources-container" :class="{error: sourcesError}">
          <p class="settings-description">{{ $translate('applicationSettings.sources.description') }}</p>
          <div v-for="(supportedSource, index) in store.editedApplicationSettings.supportedSources"
               class="checkbox-container"
               :key="index"
               @click="() => toggleSource(supportedSource)">
            <div>{{ supportedSource }}</div>
            <div class="interactive-container">
              <Checkbox :checked="store.editedApplicationSettings.sources.includes(supportedSource)"/>
            </div>
          </div>
          <p class="error-message" v-if="sourcesError">{{ $translate('applicationSettings.sources.error') }}</p>
        </div>
        <!-- Default Language -->
        <div class="settings-heading">{{ $translate('applicationSettings.defaultLanguage.title') }}</div>
        <div class="language-wrapper">
          <p class="settings-description">{{ $translate('applicationSettings.defaultLanguage.description') }}</p>
          <div v-for="(language, index) of Object.keys(translations)"
               :key="index"
               @click="() => setLanguage(language)"
               class="language-container"
               :class="[store.editedApplicationSettings.language.toLowerCase() === language ? 'selected' : '']">
            <div>{{ $translate(`languages.${language}`) }}</div>
            <svg v-show="store.editedApplicationSettings.language === language" xmlns="http://www.w3.org/2000/svg"
                 viewBox="0 -960 960 960" :alt="$translate('altTexts.check')">
              <path d="M382-240 154-468l57-57 171 171 367-367 57 57-424 424Z"/>
            </svg>
          </div>
        </div>
      </div>
    </div>
    <GridBackground/>
  </main>
</template>

<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import AdminNavigation from "@/components/AdminNavigation.vue";
import {PermissionService} from "@/services/PermissionService";
import GridBackground from "@/components/background/GridBackground.vue";
import ToggleSwitch from "@/components/buttons/ToggleSwitch.vue";
import {useApplicationSettingsStore} from "@/stores/ApplicationSettings";
import InputField from "@/components/inputs/InputField.vue";
import Checkbox from "@/components/buttons/Checkbox.vue";
import {translate, translations} from "@/plugins/TranslationPlugin";
import DynamicButton from "@/components/buttons/DynamicButton.vue";
import {validateEntryCode} from "@/plugins/ValidationPlugin";

const store = useApplicationSettingsStore();

const hasAllManagementPermissions = ref(true);
const isWaiting = ref(false);

const buttonState = computed(() => {
  if (isWaiting.value) {
    return "waiting";
  }

  if (!store.editedApplicationSettings) {
    return "inactive";
  }

  if (!store.editedApplicationSettings.entryCode) {
    return "inactive";
  }

  if (!validateEntryCode(store.editedApplicationSettings.entryCode)()) {
    return "inactive";
  }

  if (sourcesError.value) {
    return "inactive";
  }

  return "active";
});

const sourcesError = computed(() => {
  if (store.editedApplicationSettings === undefined) {
    return true;
  }

  return store.editedApplicationSettings.sources.length === 0;
})

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

async function save() {
  if (buttonState.value !== "active") {
    return
  }

  isWaiting.value = true;
  await store.save()
  isWaiting.value = false;
}

</script>

<style scoped>
main {
  width: calc(100% - 30px);
  height: calc(100svh - 60px);
  margin: 0 auto 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  box-sizing: border-box;
  padding-top: 20px;
}

.settings-wrapper {
  padding-top: 20px;
  overflow-y: scroll;
  display: flex;
  flex-direction: column;
  height: 100%;
  position: relative;
}

.settings-container {
  margin-bottom: 25px;
  padding-right: 15px;
}

.save-button {
  position: absolute;
  right: 10px;
  top: 10px;
  margin-left: auto;
  display: flex;
  flex-direction: row;
  gap: 10px;
}

.save-button > button {
  min-width: 100px;
}

.undo {
  height: 30px;
  margin: auto 0 auto 0;
}

.undo:hover {
  cursor: pointer;
}

.settings-heading {
  font-size: 28px;
  font-weight: bold;
  margin-top: 30px;
}

.settings-heading:first-child {
  margin-top: 0;
}

.settings-description {
  font-size: var(--font-size-small);
}

.toggle-switch-wrapper, .checkbox-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-top: 10px;
  margin-bottom: 5px;
  padding: 10px;
}

.checkbox-container:hover {
  cursor: pointer;
  background-color: var(--hover-color);
  border-radius: var(--border-radius-medium);
}

.toggle-switch-wrapper:hover {
  cursor: pointer;
  background-color: var(--hover-color);
  border-radius: var(--border-radius-medium);
}

.toggle-switch-wrapper .description-container {
  width: 100%;
}

.toggle-switch-wrapper .interactive-container {
  width: 75px;
  display: flex;
  justify-content: center;
}

.toggle-switch-wrapper .label {
  font-size: var(--font-size-medium);
  font-weight: bold;
}

.toggle-switch-wrapper p {
  font-size: var(--font-size-small);
}

.input-field-container {
  display: flex;
  justify-content: flex-start;
  padding-left: 10px;
}

.input-field-container .interactive-container {
  width: calc(100% - 75px);
}

.checkbox-container .description-container {
  width: 100%;
}

.checkbox-container .interactive-container {
  width: 75px;
  display: flex;
  justify-content: center;
}

.sources-container {
  padding-left: 10px;
}

.sources-container.error {
  border: 2px solid var(--red);
  border-radius: var(--border-radius-medium);
  box-sizing: border-box;
}

.sources-container .error-message {
  color: var(--red);
}

.language-wrapper {
  padding-left: 10px;
}

.language-container {
  margin-top: 5px;
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

  &&:hover {
    cursor: pointer;
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

  .settings-wrapper {
    padding: 20px;
    box-sizing: border-box;
    grid-row: 1;
    height: 100%;
    width: 66%;
    border-bottom: none;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
    display: flex;
    flex-direction: column;
  }
}
</style>