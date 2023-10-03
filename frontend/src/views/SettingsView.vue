<template>
  <main>
    <div class="settings-wrapper">
      <div class="settings-nav">
        <img class="back" src="@/assets/icons/arrows/arrow_back.svg">
      </div>
      <div class="settings-header">
        <img src="@/assets/icons/settings.svg">
        <h1>{{ $translate('settings')}}</h1>
      </div>
      <TranslateButton/>
      <InputField label="Hallo Welt"/>
      <Select :label="$translate('language')"
              :options="options"
              :id-of-default-selected="1"
      />
      <p>asdflkasdfkljaskldf asdfa sdfasdfasdfa sdf asdf </p>
    </div>
  </main>
</template>

<script setup lang="ts">
import TranslateButton from "@/components/buttons/TranslateButton.vue";
import InputField from "@/components/inputs/InputField.vue";
import Select from "@/components/inputs/Select.vue";
import {ref} from "vue";

const options = ref([
  {id: 0, text: "Deutsch"},
  {id: 1, text: "English"},
]);
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

.settings-nav {
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
    height: 75vh;
  }
}
</style>