<template>
  <div>
    <button @click="emit('back')">back</button>
    <div v-if="roleId">
      Permissions of Role with Id {{ roleId }}
    </div>
    <div v-else>
      Permissions of New Role
    </div>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  back: [],
}>();

defineProps<{
  roleId?: number
}>()
</script>

<style scoped>

</style>
