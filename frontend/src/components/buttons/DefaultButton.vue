<template>
  <div class="button-wrapper">
    <button :disabled="isDisabled">
      <slot></slot>
      {{ text }}
    </button>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  text: string,
  isDisabled: boolean
}>();
</script>


<style scoped>
.button-wrapper {
  width: 100%;
  margin: 20px 0 20px 0;
}

button {
  width: 100%;
  background: var(--primary-color);
  height: 40px;
  border: none;
  border-radius: var(--border-radius-medium);
  color: var(--text-color);
  font-size: var(--font-size-medium);
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

button:hover {
  cursor: pointer;
}

button:disabled {
  background: var(--dark-gray);
}
</style>