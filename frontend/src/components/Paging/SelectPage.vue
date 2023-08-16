<template>
  <button @click="emit('selectPage')" :class="[selected ? 'selected' : '']">{{ label }}</button>
</template>

<script setup lang="ts">
defineProps<{
  label: string | number,
  selected: boolean
}>();

const emit = defineEmits<{
  selectPage: []
}>()
</script>

<style scoped>
button {
  font-size: 15px;
  display: flex;
  width: 32px;
  height: 32px;
  justify-content: center;
  align-items: center;
  border: none;
  border-radius: 50%;
  color: var(--text-color);
  background: none;
}

.selected {
  background: var(--secondary-color);
}
</style>