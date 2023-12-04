<template>
  <div>
    <button @click="emit('toDisplay')">new role</button>

    <button @click="emit('toDisplay', 1)">user 1</button>
    <button @click="emit('toDisplay', 2)">user 2</button>
    <button @click="emit('toDisplay', 3)">user 3</button>
    List
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  back: [],
  toDisplay: [id?: number]
}>();
</script>


<style scoped>

</style>