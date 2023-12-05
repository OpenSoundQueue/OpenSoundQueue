<template>
  <div class="role-permissions-wrapper">
    <div class="role-permissions-container">
      <!-- MOBILE -->
      <nav class="mobile">
        <RolePagedNavBar @back="emit('back')"/>
      </nav>

      <!-- ALL VIEWPORTS -->
      <div v-if="roleId">
        Permissions of Role with Id {{ roleId }}
      </div>
      <div v-else>
        Permissions of New Role
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import RolePagedNavBar from "@/components/roles/RolePagedNavBar.vue";

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
  padding: 15px;
  box-sizing: border-box;
}

.role-permissions-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

@media screen and (min-width: 800px) {
  .role-permissions-container {
    width: 800px;
    margin: auto;
  }
}

@media screen and (min-width: 1250px) {
  .mobile {
    display: none;
  }

  .role-permissions-wrapper {
    position: static;
    height: fit-content;
    background: none;
    padding: 0;
  }
}


</style>
