<template>
  <div>
    <p>Verification</p>
    <button @click="emits('change')">Change</button>
  </div>
</template>

<script setup lang="ts">
const emits = defineEmits<{
  change: []
}>()
</script>

<style scoped>

</style>