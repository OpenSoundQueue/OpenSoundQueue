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
      <div class="user-container">
        <div v-for="(user,index) in filteredUsers" :key="index" class="user">
          <div>{{ user.username }} --- {{ user.isMember }}</div>
          <input type="checkbox" :checked="user.isMember" v-on:change="updateMember(user.id,user.username)">
        </div>
        <div v-show="filteredUsers.length==0">{{ translate('roleEdit.member.noResults') }}'{{ searchText }}'</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import RolePagedNavBar from "@/components/roles/RolePagedNavBar.vue";
import router from "@/router";
import {Role} from "@/models/Role";
import {useRoleStore} from "@/stores/Role";
import {computed, ComputedRef, onMounted, ref, watch} from "vue";
import type {Ref} from "vue";
import {storeToRefs} from "pinia";
import {HttpService} from "@/services/HttpService";
import type {User} from "@/models/User";
import InputField from "@/components/inputs/InputField.vue";
import {translate} from "@/plugins/TranslationPlugin";

type UserCheck = {
  id: number,
  username: string,
  isMember: boolean
}

defineProps<{
  role?: Role
}>()

const httpService = new HttpService();

const store = useRoleStore();
const role: Ref<Role | undefined> = ref();
const users: Ref<User[]> = ref([]);
const searchText = ref("");
const filteredUsers = computed(() => {
  return mapUsers(users);
})

onMounted(async () => {
  const refStore = storeToRefs(store);
  role.value = refStore.patchedRole.value

  watch(refStore.patchedRole, () => {
    role.value = refStore.patchedRole.value
  });

  await getUsers();
})

function updateMember(id:number,name:string){
  store.toggleMember(id,name)
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
      });
  return users;
}
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

.user-container {
  margin: 0 20px;
}

.user {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-bottom: var(--font-size-medium);
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
