<template>
  <div class="role-display-wrapper">
    <nav class="mobile">
      <button @click="emit('back')">back</button>
    </nav>
    <button class="mobile" @click="emit('toMembers')">to members</button>
    <button class="mobile" @click="emit('toPermissions')">to perms</button>
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
.role-display-wrapper {
  position: fixed;
  top: 60px;
  left: 0;
  width: 100%;
  height: 100vh;
  background: var(--background-color);
}

@media screen and (min-width: 1250px) {
  .mobile {
    display: none;
  }

  .role-display-wrapper {
    position: static;
    height: fit-content;
    background: none;
  }
}
</style>
