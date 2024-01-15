<template>
  <div class="role-display-wrapper">
    <div class="role-display-container">
      <!-- MOBILE -->
      <div class="mobile">
        <RolePagedNavBar @back="router.back()"/>
      </div>

      <!-- ALL VIEWPORTS -->
      <InputField
          :label="translate('roleEdit.display.roleName')"
          :placeholder="translate('roleEdit.display.placeholder')"
          :manual-value="role?.name == undefined? store.patchedRole?.name: role?.name"
          @userInput="(input)=>store.patchName(input)"
      ></InputField>

      <!-- MOBILE -->
      <div class="nav-button-wrapper mobile">
        <div class="mobile overlay"
             @click="changeTab('members')">
          <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')"/>
        </div>
        <div class="nav-button-container">
          Members
        </div>
      </div>
      <div class="nav-button-wrapper mobile">
        <div class="mobile overlay"
             @click="changeTab('permissions')">
          <img src="@/assets/icons/arrows/keyboard_arrow_right.svg" :alt="$translate('altTexts.arrowRight')"/>
        </div>
        <div class="nav-button-container">
          Permissions
        </div>
      </div>

      <!-- ALL VIEWPORTS -->
      <!-- TODO:  button style fix and testing -->
      <DynamicButton v-show="store.fetchedRole"
                     b-style="delete"
                     :status="deleteStatus"
                     @click="deleteRole()"
      >{{ translate('roleEdit.delete') }}
      </DynamicButton>
    </div>
  </div>
</template>

<script setup lang="ts">
import RolePagedNavBar from "@/components/roles/RolePagedNavBar.vue";
import router from "@/router";
import {useRoleStore} from "@/stores/Role";
import {Role} from "@/models/Role";
import {translate} from "@/plugins/TranslationPlugin";
import InputField from "@/components/inputs/InputField.vue";
import {onMounted, ref, watch} from "vue";
import type {Ref} from "vue";
import {storeToRefs} from "pinia";
import DynamicButton from "@/components/buttons/DynamicButton.vue";
import {PopUpService} from "@/services/PopUpService";
import {ToastService} from "@/services/ToastService";
import {User} from "@/models/User";

const store = useRoleStore();
const role: Ref<Role | undefined> = ref();
const deleteStatus = ref("active");
onMounted(() => {
  const refStore = storeToRefs(store);

  watch(refStore.patchedRole, () => {
    if (refStore.patchedRole.value != undefined) {
      let members: User[] = []

      refStore.patchedRole.value.members.forEach(user => {
        members.push(new User(user.id, user.username))
      })

      role.value = new Role(refStore.patchedRole.value?.id, refStore.patchedRole.value?.name, refStore.patchedRole.value?.permissions, members)
    }
  });
})

async function deleteRole() {
  await store.deleteRole();
  await router.push("/admin/roles")
}

async function changeTab(destination: "members" | "permissions") {
  if (store.patchedRole?.id == -1) {
    ToastService.sendNotification(translate('popUp.editRole.newRoleRedirectError'), "error", 3000)
    return
  }
  if (store.roleEdited) {
    PopUpService.openPopUp(translate('popUp.editRole.unsavedChanges'), translate('popUp.editRole.save'));
    const userAction = await PopUpService.waitForUserAction();

    if (userAction === "accepted") {
      await store.save();
    } else {
      store.rollback();
    }
  }
  store.fetchedRole?.id != undefined ? await router.push(`/admin/roles/${destination}/${store.fetchedRole?.id}`) : await router.push(`/admin/roles/${destination}/new`)
}

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
    margin-top: 10px;
    position: static;
    height: fit-content;
    background: none;
  }
}
</style>
