<template>
  <div class="privacy-container scrollbar" v-if="store.editedApplicationSettings">
    <div class="setting-container" @click="toggleRequireEmailAuth">
      <div class="setting-title">{{ $translate('applicationSettings.privacy.emailAuth.title') }}</div>
      <ToggleSwitch :checked="store.editedApplicationSettings.emailAuth"/>
    </div>
    <div class="description">{{ $translate('applicationSettings.privacy.emailAuth.description') }}</div>
    <div v-if="store.editedApplicationSettings.emailAuth">
      <InputField
          placeholder="E-Mail-Adresse"
      />
      <InputField
          placeholder="E-Mail Passwort"
      />
      <InputField
          placeholder="E-Mail Host"
      />
    </div>
    <div class="hr"/>
    <div class="setting-container" @click="toggleIsPrivate">
      <div class="setting-title">{{ $translate('applicationSettings.privacy.privateRoom.title') }}</div>
      <ToggleSwitch :checked="store.editedApplicationSettings.isPrivate"/>
    </div>
    <div class="description">{{ $translate('applicationSettings.privacy.privateRoom.description') }}</div>
    <div class="entry-code-container" v-if="store.editedApplicationSettings.isPrivate">
      <div class="setting-title">{{ $translate('entryCode.title') }}</div>
      <InputField
          :required="false"
          :placeholder="$translate('entryCode.placeholder')"
          v-model="store.editedApplicationSettings.entryCode"
          :manual-value="store.editedApplicationSettings.entryCode"
          :validation-function="$validateEntryCode"
          :validation-message="$translate('entryCode.validation')"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import ToggleSwitch from "@/components/buttons/ToggleSwitch.vue";
import {onMounted, watchEffect} from "vue";
import InputField from "@/components/inputs/InputField.vue";
import {validateEntryCode} from "@/plugins/ValidationPlugin";
import {useApplicationSettingsStore} from "@/stores/ApplicationSettings";

const emit = defineEmits<{
  ready: [],
  notReady: []
}>()

const store = useApplicationSettingsStore();

onMounted(() => {
  checkStatus()

  watchEffect(() => {
    checkStatus();
  });
})

function checkStatus() {
  if (!store.editedApplicationSettings?.entryCode) {
    return;
  }

  if (validateEntryCode(store.editedApplicationSettings.entryCode)()) {
    if (store.editedApplicationSettings.isPrivate) {
      store.editedApplicationSettings.entryCode.length > 0 ? emit("ready") : emit("notReady");
    } else {
      emit("ready");
    }
  } else {
    emit("notReady");
  }
}

function toggleRequireEmailAuth() {
  if (!store.editedApplicationSettings) {
    return;
  }

  store.setRequireEmailAuth(!store.editedApplicationSettings.emailAuth);
}

function toggleIsPrivate() {
  if (!store.editedApplicationSettings) {
    return;
  }

  store.setIsPrivate(!store.editedApplicationSettings.isPrivate);
}
</script>

<style scoped>
.privacy-container {
  padding: 25px;
}

.setting-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  padding-bottom: 10px;
}

.description {
  display: flex;
  align-items: center;
  min-height: 30px;

  font-size: var(--font-size-small);
  font-style: italic;
}

.entry-code-container {
  margin-top: 25px;
}

.hr {
  width: 100%;
  height: 2px;
  border-radius: 2px;
  background-color: rgb(var(--tertiary-color));
  margin-top: 10px;
  margin-bottom: 25px;
}
</style>