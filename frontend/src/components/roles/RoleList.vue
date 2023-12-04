<template>
  <div>
    <button @click="emit('toDisplay')">new role</button>

    <button @click="emit('toDisplay', 1)">role 1</button>
    <button @click="emit('toDisplay', 2)">role 2</button>
    <button @click="emit('toDisplay', 3)">role 3</button>
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