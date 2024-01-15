<template>
  <div class="role-members-wrapper">
    <div class="role-members-container">
      <!-- MOBILE -->
      <div class="mobile">
        <RolePagedNavBar @back="router.back()"/>
      </div>

      <!-- ALL VIEWPORTS -->
      <InputField
          :placeholder="translate('roleEdit.member.search')"
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
          <div>{{ $translate('roleEdit.member.selectAll') }}</div>
        </div>
        <div class="selection-manipulator" @click="selectNone">
          <img src="@/assets/icons/user_selection/none.svg" :alt="translate('altTexts.selectNone')"/>
          <div>{{ $translate('roleEdit.member.selectNone') }}</div>
        </div>
        <div class="selection-manipulator" @click="invertSelection">
          <img src="@/assets/icons/user_selection/invert.svg" :alt="translate('altTexts.invertSelection')"/>
          <div>{{ $translate('roleEdit.member.invert') }}</div>
        </div>
      </div>
      <div class="user-container scrollbar">
        <div v-for="(user,index) in filteredUsers" :key="index" class="user"
             @click="updateMember(user.id,user.username)">
          <div>{{ user.username }}</div>
          <Checkbox :checked="user.isMember" @change="updateMember(user.id,user.username)"/>
        </div>
        <div v-show="filteredUsers.length==0 && !usersAreLoading">{{ translate('roleEdit.member.noResults') }}'{{ searchText }}'</div>
        <div v-if="usersAreLoading" v-for="index of 10" :key="index" class="skeleton"/>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import RolePagedNavBar from "@/components/roles/RolePagedNavBar.vue";
import router from "@/router";
import {Role} from "@/models/Role";
import {useRoleStore} from "@/stores/Role";
import {computed, onMounted, ref, watch, watchEffect} from "vue";
import type {Ref} from "vue";
import {storeToRefs} from "pinia";
import {HttpService} from "@/services/HttpService";
import {User} from "@/models/User";
import InputField from "@/components/inputs/InputField.vue";
import {translate} from "@/plugins/TranslationPlugin";
import Checkbox from "@/components/buttons/Checkbox.vue";

type UserCheck = {
  id: number,
  username: string,
  isMember: boolean
}

defineProps<{
  role?: Role
}>()

const httpService = new HttpService();
const usersAreLoading = ref(true);

const store = useRoleStore();
const role: Ref<Role | undefined> = ref();
const users: Ref<User[]> = ref([]);
const searchText = ref("");
const filteredUsers = computed(() => {
  return mapUsers(users);
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

  watchEffect(() => {
    if (refStore.patchedRole.value != undefined) {
      let members: User[] = []

      refStore.patchedRole.value.members.forEach(user => {
        members.push(new User(user.id, user.username))
      })

      role.value = new Role(refStore.patchedRole.value?.id, refStore.patchedRole.value?.name, refStore.patchedRole.value?.permissions, members)
    }
  });

  await getUsers();
})

function selectAll() {
  if (store.patchedRole == undefined)
    return

  for (let i = 0; i < filteredUsers.value.length; i++) {
    if (store.patchedRole.members.filter(user => user.id == filteredUsers.value[i].id).length == 0)
      store.toggleMember(filteredUsers.value[i].id, filteredUsers.value[i].username)
  }
}

function selectNone() {
  if (store.patchedRole == undefined)
    return

  for (let i = 0; i < filteredUsers.value.length; i++) {
    if (store.patchedRole.members.filter(user => user.id == filteredUsers.value[i].id).length > 0)
      store.toggleMember(filteredUsers.value[i].id, filteredUsers.value[i].username)
  }

}

function invertSelection() {
  for (let i = 0; i < filteredUsers.value.length; i++) {
    store.toggleMember(filteredUsers.value[i].id, filteredUsers.value[i].username)
  }
}

function updateMember(id: number, name: string) {
  store.toggleMember(id, name)
}

function mapUsers(users: Ref<User[]>): UserCheck[] {

  const mappedUsers: Ref<UserCheck[]> = ref(toUserCheck(users));

  if (role.value == undefined)
    return mappedUsers.value;

  for (let i = 0; i < mappedUsers.value.length; i++) {
    for (let ii = 0; ii < role.value?.members.length; ii++) {
      if (mappedUsers.value[i].id == role.value?.members[ii].id)
        mappedUsers.value[i].isMember = true;
    }
  }

  return mappedUsers.value.filter(user => user.username.toLowerCase().includes(searchText.value.toLowerCase()));
}

function toUserCheck(users: Ref<User[]>): UserCheck[] {
  return users.value.map((user) => {
    return <UserCheck>{
      id: user.id,
      username: user.username,
      isMember: false,
    }
  })
}

async function getUsers() {
  await httpService.getUsers()
      .then((data) => {
        users.value = data
        usersAreLoading.value = false;
      });
}
</script>

<style scoped>
.role-members-wrapper {
  position: fixed;
  top: 60px;
  left: 0;
  width: 100%;
  padding: 15px;
  box-sizing: border-box;
}


.role-members-container {
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

.user-container {
  margin: 0 20px;
  overflow: scroll;
  height: 100%;
  overflow-x: hidden;
}

.user {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: calc(var(--font-size-medium) / 1.5);
  border-radius: var(--border-radius-small);
}

.user:hover {
  background-color: var(--primary-color);
}

.user:hover > .checkbox {
  border-color: white !important;
}

.skeleton{
  width: calc(100% - var(--font-size-medium) / 1.5 * 2);
  height: 20px;
  box-sizing: border-box;

  margin: calc(var(--font-size-medium) / 1.5 * 2) calc(var(--font-size-medium) / 1.5);
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
