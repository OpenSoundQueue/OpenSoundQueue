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
          <div>{{ user.username }}</div>
          <input type="checkbox">
        </div>
        <div v-show="filteredUsers.length==0">{{ translate('roleEdit.member.noResults')}}'{{ searchText}}'</div>
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
import type {User} from "@/models/User";
import InputField from "@/components/inputs/InputField.vue";
import {translate} from "@/plugins/TranslationPlugin";

const httpService = new HttpService();

const store = useRoleStore();
const role: Ref<Role | undefined> = ref();
const users: Ref<User[]> = ref([]);
const searchText = ref("");
const filteredUsers = computed(()=>{
  if (users === undefined)
    return [];
  return users.value.filter(user=>user.username.toLowerCase().includes(searchText.value.toLowerCase()));
})

onMounted(async () => {
  const refStore = storeToRefs(store);
  watch(refStore.patchedRole, () => {
    role.value = refStore.patchedRole.value
  });

  users.value = await httpService.getUsers();
})

const emit = defineEmits<{
  back: [],
}>();


defineProps<{
  role?: Role
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
