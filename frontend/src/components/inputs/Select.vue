<template>
  <div class="select-wrapper">
    <label class="input-field-label">{{ label }} <span v-if="required">*</span></label>
    <div class="select-container" @click="isActive = !isActive">
      <img class="drop-down-icon"
           src="@/assets/icons/arrows/keyboard_arrow_down.svg"
           :style="{transform: `rotate(${isActive ? 180 : 0}deg)`}"
      />
      <div v-if="!defaultSelected && !selectedOption.text">
        {{ $translate("select") }}
      </div>
      <div v-else>
        {{ selectedOption.text }}
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
import {computed, onMounted, ref} from "vue";
import type {Ref} from "vue";

type Option = {
  value?: string
  text?: string
}

const props = defineProps<{
  label?: string,
  required?: boolean,
  defaultSelected?: string,
  options: Option[]
}>();

const passedOptions = computed(() => {
  return props.options.filter((option: Option) => option.value !== selectedOption.value.value)
})

const isActive = ref(false);
const selectedOption: Ref<Option> = ref({})

onMounted(() => {
  console.log(props.defaultSelected, selectedOption.value.value);
  if (typeof props.defaultSelected === "undefined") {
    return;
  }

  selectedOption.value = getOptionByValue(props.defaultSelected);
})

function selectOption(option: Option) {
  selectedOption.value = option;

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
}
</style>