<template>
  <div>
    <button @click="emit('back')">back</button>
    Permissions
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  back: [],
}>();
</script>


<style scoped>

</style>
