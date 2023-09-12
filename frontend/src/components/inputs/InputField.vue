<template>
  <div class="input-field-wrapper">
    <label class="input-field-label" :for="passedId">{{ label }} <span v-if="required">*</span></label>
    <div class="input-element-wrapper">
      <div v-if="inputType === 'password'" @click="toggleVisibility" class="password-icon-wrapper">
        <img v-if="showPassword" :src="resolveFilePath('/icons/input/hide.svg')" title="Hide" class="password-icon"
             alt="Show Password">
        <img v-else :src="resolveFilePath('/icons/input/show.svg')" title="Show" class="password-icon" alt="Hide Password">
      </div>
      <div v-else class="default-icon-wrapper">
        <img v-if="inputValue" @click="clearInput" :src="resolveFilePath('/icons/input/delete.svg')" class="close-icon" alt="clear input">
        <img :src="iconPath" class="input-icon" :alt="iconAlt">
      </div>
      <input class="input-field" :class="[iconPath || inputType === 'password' ? 'has-icon' : 'no-icon']"
             :id="passedId"
             :type="inputTypeDynamic"
             :value="inputValue"
             @input="setValue">
    </div>
    <p v-if="displayError" class="error-message">
      {{ errorMessage }}
    </p>
  </div>
</template>

<script setup lang="ts">
import {computed, ref, watch} from "vue";
import {resolveFilePath} from "@/services/urlService";

interface Props {
  inputId: string | number
  modelValue?: string | number
  label?: string
  validationFunction?: Function
  errorMessage?: string
  inputType?: string
  required?: boolean
  iconPath?: string
  iconAlt?: string
  manualValue?: string | undefined
}

const props = withDefaults(defineProps<Props>(), {
  label: "",
  inputType: "text"
});

const emit = defineEmits<{
  "update:modelValue": [data: string]
  userInput: [data: string]
  escape: []
  inputClear: []
  arrowUp: []
  arrowDown: []
  enter: []
}>();

const inputValue = ref("");
const inputTypeDynamic = ref(props.inputType);
const showPassword = ref(false);
const displayError = ref(false);

const manualValue = computed(() => props.manualValue);
const passedId = computed(() => {
  return props.inputId ? props.inputId.toString() : ''
})

function setValue(event: Event) {
  inputValue.value = (event.target as HTMLInputElement).value;
  emit("update:modelValue", (event.target as HTMLInputElement).value);
  emit("userInput", inputValue.value);
}

function toggleVisibility() {
  showPassword.value = !showPassword.value;

  if (showPassword.value) {
    inputTypeDynamic.value = "text";
  } else {
    inputTypeDynamic.value = "password";
  }
}

function clearInput() {
  inputValue.value = "";
  emit("inputClear");
}

watch(manualValue, (newValue) => {
  if (typeof newValue !== "undefined") {
    inputValue.value = newValue;
  }
});
</script>

<style scoped>
.input-field-label {
  padding-left: 2px;
}

.input-field-wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: fit-content;
  width: 100%;
}

.input-element-wrapper {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.input-field {
  width: 100%;
  padding-right: 40px;
}

.input-field.has-icon {
  padding-left: 35px;
}

.password-icon-wrapper, .default-icon-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
}

.input-icon {
  height: 20px;
  position: absolute;
  left: 10px;
}

.password-icon, .close-icon {
  height: 20px;
  position: absolute;
  right: 10px;
}

.close-icon {
  cursor: pointer;
}

.error-message {
  margin-top: 5px;
  color: red;
}
</style>