<template>
  <div>
    <button @click="emit('back')">back</button>
    <button @click="emit('toMembers')">to members</button>
    <button @click="emit('toPermissions')">to perms</button>
    <div v-if="roleId">
      Display of Role with Id {{ roleId }}
    </div>
    <div v-else>
      Display of New Role
    </div>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits<{
  back: [],
  toMembers: []
  toPermissions: []
}>();

defineProps<{
  roleId?: number
}>()
</script>


<style scoped>

</style>
