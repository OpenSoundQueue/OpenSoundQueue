<template>
  <div class="role-members-wrapper">
    <div class="role-members-container">
      <!-- MOBILE -->
      <div class="mobile">
        <RolePagedNavBar @back="router.back()"/>
      </div>

      <!-- ALL VIEWPORTS -->
      <div v-if="roleId">
        Members of Role with Id {{ roleId }}
      </div>
      <div v-else>
        Members of New Role
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import RolePagedNavBar from "@/components/roles/RolePagedNavBar.vue";
import router from "@/router";

const emit = defineEmits<{
  back: [],
}>();

defineProps<{
  roleId?: number
}>()
</script>

<style scoped>
.role-members-wrapper {
  position: fixed;
  top: 60px;
  left: 0;
  width: 100%;
  height: 100vh;
  background: var(--background-color);
  padding: 15px;
  box-sizing: border-box;
}

.role-members-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

@media screen and (min-width: 800px) {
  .role-members-container {
    width: 800px;
    margin: auto;
  }
}

@media screen and (min-width: 1250px) {
  .mobile {
    display: none;
  }

  .role-members-wrapper {
    padding: 0;
    position: static;
    height: fit-content;
    background: none;
  }
}

</style>
