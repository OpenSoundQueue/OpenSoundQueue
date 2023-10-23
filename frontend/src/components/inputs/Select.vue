<template>
  <div class="select-wrapper">
    <label class="input-field-label">{{ label }} <span v-if="required">*</span></label>
    <div class="select-container" @click="isActive = !isActive">
      <img class="drop-down-icon" alt="Dropdown Icon"
           src="@/assets/icons/arrows/keyboard_arrow_down.svg"
           :style="{transform: `rotate(${isActive ? 180 : 0}deg)`}"
      />
      <div v-if="!value">
        {{ $translate("select") }}
      </div>
      <div v-else>
        {{ getOptionByValue(value).text }}
      </div>
    </div>
    <div class="options-container" :class="{active: isActive}">
      <div v-for="(option, index) in passedOptions" :key="index" @click="selectOption(option)" class="option">
        {{ option.text }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, ref} from "vue";

type Option = {
  value?: string
  text?: string
}

const props = defineProps<{
  modelValue: string,
  label?: string,
  required?: boolean,
  options: Option[]
}>();

const emit = defineEmits<{
  "update:modelValue": [data: string]
}>();

const value = computed({
  get() {
    return props.modelValue
  },
  set(value) {
    emit('update:modelValue', value)
  }
})

const passedOptions = computed(() => {
  return props.options.filter((option: Option) => option.value !== value.value)
})

const isActive = ref(false);

function selectOption(option: Option) {
  if (typeof option.value !== "undefined") {
    value.value = option.value;
  }

  isActive.value = false;
}

function getOptionByValue(value: string): Option {
  for (let option of props.options) {
    if (option.value === value) {
      return option;
    }
  }

  return {};
}
</script>

<style scoped>
.select-wrapper {
  width: 100%;
  position: relative;
}

label {
  padding-left: 2px;
}

.select-container:hover, .options-container:hover{
  cursor: pointer;
}

.select-container {
  margin-top: 5px;
  padding-left: 5px;
  box-sizing: border-box;
  width: 100%;
  height: 40px;
  border: 2px solid var(--tertiary-color);
  border-radius: var(--border-radius-medium);
  position: relative;
  display: flex;
  align-items: center;
}

.drop-down-icon {
  right: 10px;
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: transform 0.2s ease;
}

.options-container {
  margin-top: 3px;
  min-height: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  box-sizing: border-box;
  position: absolute;
  background: var(--secondary-color);
  border: 2px solid var(--tertiary-color);
  border-radius: var(--border-radius-medium);
  width: 100%;
  visibility: hidden;
}

.options-container.active {
  visibility: visible;
}

.option {
  padding: 4px 5px;
}

.option:hover {
  background: var(--tertiary-color);
  color: var(--background-color);
  font-weight: bold;
}
</style>