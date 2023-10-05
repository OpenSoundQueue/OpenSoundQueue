<template>
  <div class="input-field-wrapper">
    <label class="input-field-label" :for="inputId">{{ label }} <span v-if="required">*</span></label>
    <div class="input-element-wrapper">
      <div v-if="inputType === 'password'" @click="toggleVisibility" class="password-icon-wrapper">
        <img v-if="showPassword" src="@/assets/icons/input/hide.svg" title="Hide" class="password-icon"
             alt="Show Password">
        <img v-else src="@/assets/icons/input/show.svg" title="Show" class="password-icon" alt="Hide Password">
      </div>
      <div v-else class="default-icon-wrapper">
        <img v-if="inputValue" @click="clearInput" src="@/assets/icons/input/delete.svg" class="close-icon" alt="clear input">
        <div class="input-icon">
          <slot></slot>
        </div>
      </div>
      <input class="input-field" :class="[customIcon || inputType === 'password' ? 'has-icon' : 'no-icon']"
             :id="inputId"
             :type="inputTypeDynamic"
             :value="inputValue"
             @input="setValue"
             :placeholder="placeholder"
      >
    </div>
    <p v-if="displayError" class="error-message">
      {{ errorMessage }}
    </p>
  </div>
</template>

<script setup lang="ts">
import {computed, ref, watch} from "vue";
import {generateUUID} from "@/services/uuidService";

interface Props {
  modelValue?: string | number
  label?: string
  validationFunction?: Function
  errorMessage?: string
  inputType?: string
  required?: boolean
  customIcon?: boolean
  iconAlt?: string
  manualValue?: string | undefined,
  placeholder?: string
}

const props = withDefaults(defineProps<Props>(), {
  label: "",
  inputType: "text",
  customIcon: false
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

const inputId = computed(() => {
  return generateUUID();
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
  margin-bottom: 5px;
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
  box-sizing: border-box;
  width: 100%;
  padding: 0 40px 0 5px;
  height: 40px;
  font-size: var(--font-size-medium);
  background: none;
  border: 2px solid var(--tertiary-color);
  color: var(--text-color);
  border-radius: var(--border-radius-medium);
}

.input-field::placeholder {
  color: var(--tertiary-color);
}

.input-field:focus {
  outline: 2px solid var(--primary-color);
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