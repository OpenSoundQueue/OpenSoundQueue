<template>
  <div class="privacy-container scrollbar">
    <div class="setting-container" @click="store.toggleEmailAuth()">
      <div class="setting-title">{{ $translate('applicationSettings.privacy.emailAuth.title') }}</div>
      <ToggleSwitch :checked="store.requireEmailAuth"/>
    </div>
    <div class="description">{{ $translate('applicationSettings.privacy.emailAuth.description') }}</div>
    <div class="hr"/>
    <div class="setting-container" @click="store.toggleIsPrivate()">
      <div class="setting-title">{{ $translate('applicationSettings.privacy.privateRoom.title') }}</div>
      <ToggleSwitch :checked="store.isPrivate"/>
    </div>
    <div class="description">{{ $translate('applicationSettings.privacy.privateRoom.description') }}</div>
    <div class="entry-code-container">
      <div class="setting-title">{{ $translate('entryCode.title') }}</div>
      <InputField
          :required="false"
          :placeholder="$translate('entryCode.placeholder')"
          @user-input="(entryCode)=>store.setEntryCode(entryCode)"
          :manual-value="store.entryCode"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import ToggleSwitch from "@/components/buttons/ToggleSwitch.vue";
import {onMounted, watchEffect} from "vue";
import InputField from "@/components/inputs/InputField.vue";
import {validateEntryCode} from "@/plugins/ValidationPlugin";
import {useInstallationStore} from "@/stores/Installation";

const store = useInstallationStore();

onMounted(() => {
  checkStatus()

  watchEffect(() => {
    checkStatus();
  });
})


function checkStatus() {
  if (validateEntryCode(store.entryCode)()) {
    if (store.isPrivate) {
      store.entryCode.length > 0 ? emit("ready") : emit("notReady");
    } else {
      emit("ready");
    }
  } else {
    emit("notReady");
  }
}

const emit = defineEmits<{
  ready: [],
  notReady: []
}>()
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