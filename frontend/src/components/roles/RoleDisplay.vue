<template>
  <div class="role-display-wrapper">
    <div class="role-display-container">
      <!-- MOBILE -->
      <div class="mobile">
        <RolePagedNavBar @back="router.back()"/>
      </div>

      <!-- ALL VIEWPORTS -->
      <div v-if="role">
        <InputField
            :label="translate('roleEdit.display.roleName')"
            :placeholder="translate('roleEdit.display.placeholder')"
            :manual-value="role.name"
            @userInput="(input)=>store.patchName(input)"
        ></InputField>
      </div>
      <div v-else>
        Change name of new role
      </div>

      <!-- MOBILE -->
      <div class="nav-button-wrapper mobile">
        <div class="mobile overlay"
             @click="role?.id ? router.push(`/admin/roles/members/${role?.id}`) : router.push('/admin/roles/members/new')">
          <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')"/>
        </div>
        <div class="nav-button-container">
          Members
        </div>
      </div>
      <div class="nav-button-wrapper mobile">
        <div class="mobile overlay"
             @click="role?.id ? router.push(`/admin/roles/permissions/${role?.id}`) : router.push('/admin/roles/permissions/new')">
          <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')"/>
        </div>
        <div class="nav-button-container">
          Permissions
        </div>
      </div>

      <!-- ALL VIEWPORTS -->
      <div>Delete</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import RolePagedNavBar from "@/components/roles/RolePagedNavBar.vue";
import router from "@/router";
import {useRoleStore} from "@/stores/Role";
import type {Role} from "@/models/Role";
import {translate} from "@/plugins/TranslationPlugin";
import InputField from "@/components/inputs/InputField.vue";
import {onMounted, ref, watch} from "vue";
import type {Ref} from "vue";
import {storeToRefs} from "pinia";

const store = useRoleStore();
const role: Ref<Role> = ref();

onMounted(() => {
  const refStore = storeToRefs(store);

  watch(refStore.patchedRole, () => {
    role.value = refStore.patchedRole.value
  });

  setInterval(() => {
    //console.log(role.value)
  }, 1000)
})


const emit = defineEmits<{
  back: [],
  toMembers: []
  toPermissions: []
}>();
</script>

<style scoped>
.role-display-wrapper {
  position: fixed;
  top: 60px;
  left: 0;
  width: 100%;
  height: 100vh;
  background: var(--background-color);
  padding: 15px;
  box-sizing: border-box;
}

.role-display-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 100%;
}

.nav-button-wrapper {
  background: var(--background-color);
  position: relative;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 2px solid var(--secondary-color);
  border-radius: var(--border-radius-medium);
}

.nav-button-wrapper:hover {
  cursor: pointer;
}

.nav-button-wrapper .overlay {
  position: absolute;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
}

.nav-button-wrapper .overlay img {
  height: 35px;
}

@media screen and (min-width: 800px) {
  .role-display-container {
    margin: auto;
  }
}

@media screen and (min-width: 1250px) {
  .mobile {
    display: none;
  }

  .role-display-wrapper {
    padding: 0;
    position: static;
    height: fit-content;
    background: none;
  }
}
</style>
