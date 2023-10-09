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
      <input class="input-field" :class="[
          displayError?'error':'',
          displayInvalid?'error':'',
          customIcon || inputType === 'password' ? 'has-icon' : 'no-icon']"
             :id="inputId"
             :type="inputTypeDynamic"
             :value="inputValue"
             @input="setValue"
             :placeholder="placeholder"
      >
    </div>
    <p v-if="displayInvalid" class="error-message">
      {{ validationMessage }}
    </p>
    <p v-else-if="displayError" class="error-message">
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
  validationMessage?: string
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
}>();

const inputValue = ref("");
const inputTypeDynamic = ref(props.inputType);
const showPassword = ref(false);
const errorStatus = ref(false);

const manualValue = computed(() => props.manualValue);

const inputId = computed(() => {
  return generateUUID();
})

const displayError = computed( () =>{
  return errorStatus.value
})
const displayInvalid = computed( () =>{
  if (props.validationFunction){
    return !props.validationFunction(inputValue.value)()
  }
  return false;
})

function setValue(event: Event) {
  inputValue.value = (event.target as HTMLInputElement).value;
  emit("update:modelValue", (event.target as HTMLInputElement).value);
}

function toggleVisibility(): void {
  showPassword.value = !showPassword.value;

  if (showPassword.value) {
    inputTypeDynamic.value = "text";
  } else {
    inputTypeDynamic.value = "password";
  }
}

function clearInput(): void {
  inputValue.value = "";
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

.error{
  border-color: red;
}
</style>