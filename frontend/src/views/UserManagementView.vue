<template>
  <main>
    <div class="user-container">
      <div class="table-header">
        <SortingButton class="username" :label="$translate('adminPage.tableHeader.username')"
                       @update:sortingStatus="updateSorting('username',$event)"></SortingButton>
        <SortingButton class="email" :label="$translate('adminPage.tableHeader.email')"
                       @update:sortingStatus="updateSorting('email',$event)"></SortingButton>
        <SortingButton class="role" :label="$translate('adminPage.tableHeader.role')"
                       @update:sortingStatus="updateSorting('role',$event)"></SortingButton>
      </div>
      <div class="hr"></div>
      <div class="scroll-component scrollbar">
        <div v-for="user in sortedUsers" class="user" :class="selectedID===user.id?'selected':''" :key="user.id"
             @click="selectUser(user.id)">
          <p class="username">{{ user.username }}</p>
          <p class="email">{{ user.email }}</p>
          <p class="role"><span class="dot"></span>{{ user.role }}</p>
        </div>
      </div>
    </div>
    <UserDetail class="detail-container" :user="selectedUser" :selfID="selfID" @update:Users="updateUsers($event)"></UserDetail>
  </main>
</template>

<script setup lang="ts">
import {HttpService} from "@/services/HttpService";
import type {Ref} from "vue";
import {computed, onMounted, ref} from "vue";
import {User} from "@/models/User";
import SortingButton from "@/components/buttons/SortingButton.vue";
import UserDetail from "@/components/UserDetail.vue";
import * as http from "http";
import * as https from "https";

type SortingDirection = 'asc' | 'desc' | 'none';
type SortingMetric = {
  attributeName: keyof User,
  direction: SortingDirection
}

const httpService = new HttpService();
const users: Ref<Array<User>> = ref([]);
const selfID = ref(0);
const selectedID = ref(0);
const sortingMetric: Ref<SortingMetric> = ref({attributeName: "username", direction: "none"})

const selectedUser = computed(() => {
  return users.value.find((user) => user.id === selectedID.value);
});
const sortedUsers = computed(() => {
  return sortUsers(users.value, sortingMetric.value.attributeName, sortingMetric.value.direction)
})

onMounted(() => {
  getUsers();
  getSelf();
})

async function getUsers(): void {
  await httpService.getUsers()
      .then((data: User[]) => {
        users.value = data;
        selectFirstUser()
      })
}

async function getSelf(): void{
  await httpService.getSelf()
      .then((data: User) => {
        selfID.value = parseInt(data.id);
      })
}

function updateUsers(event: User[]): void {
  users.value = event
  selectFirstUser()
}

function selectFirstUser(): void {
  if (users.value.length > 0) {
    selectedID.value = sortedUsers.value[0].id;
  }
}

function updateSorting(column: string, event: string): void {
  if (['asc', 'desc', 'none'].indexOf(event) !== -1) {
    sortingMetric.value = {
      attributeName: column as keyof User,
      direction: event as SortingDirection
    }
  }
}

function selectUser(index: number): void {
  selectedID.value = index;
}

function sortUsers(usersArray: User[], attributeName: string, direction: 'asc' | 'desc' | 'none'): User[] {
  if (!usersArray) {
    return [];
  }
  const compareFunction = (a: User, b: User) => {
    const aValue = a[attributeName as keyof User];
    const bValue = b[attributeName as keyof User];

    if (direction === 'none') return 0;

    if (direction === 'asc') {
      if (aValue < bValue) return -1;
      if (aValue > bValue) return 1;
      return 0;
    } else {
      if (aValue < bValue) return 1;
      if (aValue > bValue) return -1;
      return 0;
    }
  };

  return [...usersArray].sort(compareFunction);
}
</script>


<style scoped>
main {
  width: calc(100% - 30px);
  height: calc(100vh - 60px);
  margin: 0 auto 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-sizing: border-box;
  padding-top: 20px;
}

.dot {
  height: 5px;
  aspect-ratio: 1/1;
  background-color: var(--tertiary-color);
  border-radius: 50%;
  display: inline-block;
  margin-right: 5px;
}

.user-container {
  overflow-y: hidden;
  display: flex;
  flex-direction: column;
}

.detail-container {
  flex: 0 0 auto;
}

.scroll-component {
  height: calc(100% - 40px);
  flex: 1;
}

.email {
  display: none;
}

.user {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 0 10px 0 10px;
  height: calc(var(--font-size-medium) * 2.5);
  border-radius: var(--border-radius-medium);
  background-color: var(--secondary-color);
  margin-top: 10px;
}

.user:hover {
  cursor: pointer;
}

.user.selected {
  background-color: var(--tertiary-color);
}

.user.selected > .username,
.user.selected > .email {
  color: var(--background-color);
  font-weight: bold;
}

.table-header {
  padding: 20px 20px 0 20px;
  display: flex;
  gap: 10px;
  justify-content: space-between;
  align-items: center;
  height: 40px;
  border-bottom: solid 3px var(--secondary-color);
}

.role {
  width: 30%;
  text-align: center;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.table-header > .role {
  justify-content: center;
}

.user > .role {
  font-size: var(--font-size-small);
  background-color: var(--background-color);
  width: fit-content;
  max-width: 20%;
  box-sizing: border-box;
  padding: 5px;
  border-radius: var(--border-radius-small);
  margin: 0 auto 0 auto;
  text-align: left;
}


.username {
  width: 70%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

@media screen and (min-width: 800px) {
  main {
    width: 750px;
  }
}

@media screen and (min-width: 1250px) {
  main {
    width: 1250px;
    display: grid;
    grid-template-columns: 66% 33%;
    grid-template-rows: 60px calc(100% - 90px);
  }

  .email {
    display: flex;
  }

  .detail-container {
    grid-column: 2;
    grid-row: 2;
  }

  .user-container {
    grid-column: 1;
    grid-row: 2;
    height: 100%;
    border-bottom: none;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
    display: flex;
    flex-direction: column;
  }

  .hr {
    margin: auto;
    width: calc(100% - 20px);
    border-bottom: var(--tertiary-color) 2px solid;
    height: 2px;
  }

  .user {
    margin-top: 0;
  }

  .username {
    width: 30%;
  }

  .email {
    width: 50%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .role {
    width: 20%;
  }

  .scroll-component {
    height: calc(100% - 50px);
    margin: 10px;
    box-sizing: border-box;
    overflow-y: auto;
  }
}
</style>