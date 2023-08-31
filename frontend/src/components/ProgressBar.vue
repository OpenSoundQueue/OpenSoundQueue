<template>
  <div class="progress-bar-wrapper">
    <div class="label">{{ labelLeft }}</div>
    <div class="progress-bar-container">
      <div class="progress-bar" :style="{transform: `translateX(${getValue - 100}%)`}"></div>
    </div>
    <div class="label">{{ labelRight }}</div>
  </div>
</template>

<script setup lang="ts">
import {computed} from "vue";

const props = defineProps<{
  labelLeft: string,
  labelRight: string,
  value: number,
  min: number,
  max: number
}>();

const getValue = computed(() => {
  const valueInBounds = (props.value - props.min) / props.max * 100;

  if (valueInBounds > props.max) {
    return props.max;
  }

  return (props.value - props.min) / props.max * 100;
});
</script>

<style scoped>
.progress-bar-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
}

.progress-bar-container {
  overflow: hidden;
  border-radius: var(--border-radius-small);
  background: var(--secondary-color);
  height: 5px;
  width: 100%;
}

.label {
  text-align: center;
  width: 25px;
  font-size: var(--font-size-small);
}

.progress-bar {
  background: var(--text-color);
  border-radius: var(--border-radius-small);
  position: relative;
  height: 5px;
  top: 0;
  transition: transform 0.1s linear;
}
</style>