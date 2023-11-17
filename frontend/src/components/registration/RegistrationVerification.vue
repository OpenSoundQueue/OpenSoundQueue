<template>
  <div>
    <p>Verification</p>
    <p>{{ test }}</p>
    <button @click="emits('change')">Change</button>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  test: string
}>()

const emits = defineEmits<{
  change: []
}>()
</script>

<style scoped>

</style>