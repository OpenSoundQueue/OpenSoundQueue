<template>
<div>
  <p>Form</p>
  <p>{{ test }}</p>
  <button @click="emits('continue')">Continue</button>
</div>
</template>

<script setup lang="ts">
defineProps<{
  test: string
}>()

const emits = defineEmits<{
  continue: []
}>()
</script>

<style scoped>

</style>