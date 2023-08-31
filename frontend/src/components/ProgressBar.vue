<template>
  <div class="progress-bar-wrapper">
    <div class="label">{{ labelLeft }}</div>
    <div class="progress-bar-container">
      <div class="progress-bar" :style="{transform: `translateX(${getValue}%)`}"></div>
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
  background: var(--text-color);
  height: 5px;
  width: 100%;
}

.label {
  text-align: center;
  width: 5%;
  font-size: 11px;
}

.progress-bar {
  background: var(--secondary-color);
  position: relative;
  height: 5px;
  top: 0;
  transition: transform 0.1s linear;
}
</style>