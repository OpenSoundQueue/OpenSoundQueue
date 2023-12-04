<template>
  <div class="role-permissions-wrapper">
    <nav class="mobile">
      <button @click="emit('back')">back</button>
    </nav>
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
.role-permissions-wrapper {
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

  .role-permissions-wrapper {
    position: static;
    height: fit-content;
    background: none;
  }
}


</style>
