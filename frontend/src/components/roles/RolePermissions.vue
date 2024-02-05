<template>
  <div class="role-permissions-wrapper">
    <div class="role-permissions-container">
      <!-- MOBILE -->
      <nav class="mobile">
        <RolePagedNavBar @back="router.back()"/>
      </nav>

      <!-- ALL VIEWPORTS -->
      <InputField
          :placeholder="translate('roleEdit.permissions.search')"
          :custom-icon="true"
          @userInput="(newSearchText)=>searchText=newSearchText"
      >
        <template #icon>
          <img src="@/assets/icons/input/search.svg" :alt="$translate('altTexts.search')"/>
        </template>
      </InputField>
      <div class="selection-manipulation-container">
        <div class="selection-manipulator" @click="selectAll">
          <img src="@/assets/icons/user_selection/all.svg" :alt="translate('altTexts.selectAll')"/>
          <div>{{ $translate('roleEdit.permissions.selectAll') }}</div>
        </div>
        <div class="selection-manipulator" @click="selectNone">
          <img src="@/assets/icons/user_selection/none.svg" :alt="translate('altTexts.selectNone')"/>
          <div>{{ $translate('roleEdit.permissions.selectNone') }}</div>
        </div>
        <div class="selection-manipulator" @click="invertSelection">
          <img src="@/assets/icons/user_selection/invert.svg" :alt="translate('altTexts.invertSelection')"/>
          <div>{{ $translate('roleEdit.permissions.invert') }}</div>
        </div>
      </div>
      <div class="permissions-container scrollbar">
        <div v-for="(permission,index) in filteredPermissions" :key="index" class="permission"
             @click="updatePermission(permission.name)">
          <div>{{ permission.name }}</div>
          <ToggleSwitch :checked="permission.isActive" @change="updatePermission(permission.name)"/>
        </div>
        <div v-show="filteredPermissions.length==0 && !permissionsAreLoading">{{ translate('roleEdit.permissions.noResults') }}'{{ searchText }}'</div>
        <div v-if="permissionsAreLoading" v-for="index of 10" :key="index" class="skeleton"/>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import RolePagedNavBar from "@/components/roles/RolePagedNavBar.vue";
import router from "@/router";
import {Role} from "@/models/Role";
import {useRoleStore} from "@/stores/Role";
import {computed, onMounted, ref, watch} from "vue";
import type {Ref} from "vue";
import {storeToRefs} from "pinia";
import {HttpService} from "@/services/HttpService";
import InputField from "@/components/inputs/InputField.vue";
import {translate} from "@/plugins/TranslationPlugin";
import ToggleSwitch from "@/components/buttons/ToggleSwitch.vue";
import {User} from "@/models/User";

type PermissionCheck = {
  name:string,
  isActive:boolean
}

defineProps<{
  role?: Role
}>()

const httpService = new HttpService();
const permissionsAreLoading = ref(true);

const store = useRoleStore();
const role: Ref<Role | undefined> = ref();
const permissions: Ref<string[]> = ref([]);
const searchText = ref("");
const filteredPermissions = computed(() => {
  return mapPermissions(permissions);
})

onMounted(async () => {
  const refStore = storeToRefs(store);

  if (refStore.patchedRole.value != undefined) {
    let members: User[] = []

    refStore.patchedRole.value.members.forEach(user => {
      members.push(new User(user.id, user.username))
    })

    role.value = new Role(refStore.patchedRole.value?.id, refStore.patchedRole.value?.name, refStore.patchedRole.value?.permissions, members)
  }

  watch(refStore.patchedRole, () => {
    if (refStore.patchedRole.value != undefined) {
      let members: User[] = []

      refStore.patchedRole.value.members.forEach(user => {
        members.push(new User(user.id, user.username))
      })

      role.value = new Role(refStore.patchedRole.value?.id, refStore.patchedRole.value?.name, refStore.patchedRole.value?.permissions, members)
    }
  });

  await getPermissions();
})

function selectAll() {
  if (store.patchedRole == undefined)
    return

  for (let i = 0; i < filteredPermissions.value.length; i++) {
    if (store.patchedRole.permissions.filter(permission => permission == filteredPermissions.value[i].name).length == 0)
      store.togglePermission(filteredPermissions.value[i].name)
  }
}

function selectNone() {
  if (store.patchedRole == undefined)
    return

  for (let i = 0; i < filteredPermissions.value.length; i++) {
    if (store.patchedRole.permissions.filter(permission => permission == filteredPermissions.value[i].name).length > 0)
      store.togglePermission(filteredPermissions.value[i].name)
  }
}

function invertSelection() {
  for (let i = 0; i < filteredPermissions.value.length; i++) {
    store.togglePermission(filteredPermissions.value[i].name)
  }
}

function updatePermission(name: string) {
  store.togglePermission(name)
}

function mapPermissions(permissions: Ref<string[]>): PermissionCheck[] {

  const mappedPermissions: Ref<PermissionCheck[]> = ref(toUserCheck(permissions));

  if (role.value == undefined)
    return mappedPermissions.value;

  for (let i = 0; i < mappedPermissions.value.length; i++) {
    for (let ii = 0; ii < role.value?.permissions.length; ii++) {
      if (mappedPermissions.value[i].name == role.value?.permissions[ii])
        mappedPermissions.value[i].isActive = true;
    }
  }

  return mappedPermissions.value.filter(permission => permission.name.toLowerCase().includes(searchText.value.toLowerCase()));
}

function toUserCheck(permissions: Ref<string[]>): PermissionCheck[] {
  return permissions.value.map((permission) => {
    return <PermissionCheck>{
      name: permission,
      isActive: false
    }
  })
}

async function getPermissions() {
  await httpService.getPermissions()
      .then((data) => {
        permissions.value = data
        permissionsAreLoading.value = false;
      });
}
</script>

<style scoped>
.role-permissions-wrapper {
  position: fixed;
  top: 60px;
  left: 0;
  width: 100%;
  padding: 15px;
  box-sizing: border-box;
}

.role-permissions-container {
  display: flex;
  height: 100%;
  flex-direction: column;
  gap: 15px;
  width: 100% !important;
}
.selection-manipulation-container {
  display: flex;
  flex-direction: row;
  gap: 10px;
  width: 100%;
  margin-top: -10px;
}

.selection-manipulator {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 5px;
  padding: 2px 5px;

  background-color: var(--primary-color);
  color: var(--text-color);
  border: solid 2px var(--primary-color);
  border-radius: var(--border-radius-small);
  user-select: none;
  box-sizing: border-box;
}

.selection-manipulator:hover {
  cursor: pointer;
}

.selection-manipulator > img {
  aspect-ratio: 1;
  height: var(--font-size-medium);
  border: solid 1px var(--primary-color);
  border-radius: 2px;
}

.permissions-container {
  margin: 0 20px;
  overflow: scroll;
  height: 100%;
  overflow-x: hidden;
}

.permission {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: calc(var(--font-size-medium) / 1.5);
  border-radius: var(--border-radius-small);
}

.permission:hover {
  background-color: var(--hover-color);
}

.skeleton{
  width: calc(100% - var(--font-size-medium) / 1.5 * 2);
  height: 20px;
  box-sizing: border-box;

  margin: calc(var(--font-size-medium) / 1.5 * 2) calc(var(--font-size-medium) / 1.5);
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
