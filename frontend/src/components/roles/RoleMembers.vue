<template>
  <div>
    <button @click="emit('back')">back</button>
    Members
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  back: [],
}>();
</script>


<style scoped>

</style>
