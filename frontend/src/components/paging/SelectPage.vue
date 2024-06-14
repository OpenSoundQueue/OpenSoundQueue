<template>
  <button @click="emit('selectPage')" :class="[selected ? 'selected' : 'not-selected']">{{ label }}</button>
</template>

<script setup lang="ts">
defineProps<{
  label: number,
  selected: boolean
}>();

const emit = defineEmits<{
  selectPage: []
}>()
</script>

<style scoped>
.not-selected {
  background: transparent;
}

.selected {
  background: rgb(var(--secondary-color));
}
</style>