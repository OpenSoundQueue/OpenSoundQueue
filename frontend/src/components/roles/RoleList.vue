<template>
  <div>
    <button @click="emit('toDisplay')">new role</button>
    List
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  back: [],
  toDisplay: []
}>();
</script>


<style scoped>

</style>