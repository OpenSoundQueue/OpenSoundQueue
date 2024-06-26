<template>
  <div class="input-field-wrapper">
    <label class="input-field-label" :for="inputId">{{ label }} <slot name="help"></slot><span v-if="required">*</span></label>
    <div class="input-element-wrapper">
      <div v-show="inputType === 'password'" @click="toggleVisibility" class="password-icon-wrapper">
        <img v-show="showPassword" src="@/assets/icons/input/hide.svg" title="Hide" class="password-icon"
             :alt="$translate('altTexts.hide')">
        <img v-show="!showPassword" src="@/assets/icons/input/show.svg" title="Show" class="password-icon" :alt="$translate('altTexts.show')">
      </div>
      <div v-show="inputType !== 'password'" class="default-icon-wrapper">
        <img v-show="inputValue" @click="clearInput" src="@/assets/icons/input/delete.svg" class="close-icon" :alt="$translate('altTexts.delete')">
        <div class="input-icon">
          <slot name="icon"></slot>
        </div>
      </div>
      <input class="input-field" :class="[
          errorStatus?'error':'',
          displayInvalid?'error':'',
          customIcon && inputType !== 'password' ? 'has-icon' : 'no-icon']"
             :id="inputId"
             :type="inputTypeDynamic"
             :value="inputValue"
             @input="setValue"
             :placeholder="placeholder"
      />
    </div>
    <p v-if="displayInvalid" class="error-message">
      {{ validationMessage }}
    </p>
    <p v-else-if="errorStatus" class="error-message">
      {{ errorMessage }}
    </p>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref, watch} from "vue";
import {generateUUID} from "@/services/uuidService";

interface Props {
  modelValue?: string | number
  label?: string
  validationFunction?: Function
  errorStatus?: boolean
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
  userInput: [data: string]
}>();

defineExpose({clearInput});

const inputValue = ref("");
const inputTypeDynamic = ref(props.inputType);
const showPassword = ref(false);
const isDirty = ref(false);

const manualValue = computed(() => props.manualValue);

const inputId = computed(() => {
  return generateUUID();
})

const displayInvalid = computed( () =>{
  if (!isDirty.value) {
    return false;
  }

  if (props.validationFunction){
    return !props.validationFunction(inputValue.value)();
  }
  return false;
})

function setValue(event: Event) {
  isDirty.value = true;

  inputValue.value = (event.target as HTMLInputElement).value;
  emit("update:modelValue", (event.target as HTMLInputElement).value);
  emit("userInput", inputValue.value);

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
  emit("update:modelValue", inputValue.value);
  emit("userInput", inputValue.value);
}

onMounted(()=>{
  if (typeof props.manualValue !== "undefined") {
    inputValue.value = props.manualValue;
  }
})

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
  display: flex;
  flex-direction: row;
  gap: 5px;
  align-items: center;
}

.input-field-wrapper {
  display: flex;
  margin-bottom: 10px;
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
  padding: 0 40px 0 10px;
  height: 40px;
  font-size: var(--font-size-medium);
  background: none;
  border: 2px solid rgb(var(--tertiary-color));
  color: rgb(var(--text-color));
  border-radius: var(--border-radius-medium);
}

.input-field::placeholder {
  color: rgb(var(--tertiary-color));
}

.input-field.has-icon {
  padding-left: 40px;
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
  margin-bottom: 0;
  color: rgb(var(--error));
}

.error{
  border-color: rgb(var(--error));
}
</style>