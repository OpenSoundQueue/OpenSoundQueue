<template>
  <div>
    <button @click="emit('back')">back</button>
    <button @click="emit('toMembers')">to members</button>
    <button @click="emit('toPermissions')">to perms</button>
    Display
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  back: [],
  toMembers: []
  toPermissions: []
}>();
</script>


<style scoped>

</style>
