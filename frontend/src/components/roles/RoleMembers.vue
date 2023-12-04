<template>
  <div>
    <button @click="emit('back')">back</button>
    <div v-if="roleId">
      Members of Role with Id {{ roleId }}
    </div>
    <div v-else>
      Members of New Role
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
