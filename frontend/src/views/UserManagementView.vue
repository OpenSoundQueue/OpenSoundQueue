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
    <UserDetail class="detail-container" :user="selectedUser" @delete:User="updateUsers($event)"></UserDetail>
  </main>
</template>

<script setup lang="ts">
import {HttpService} from "@/services/HttpService";
import type {Ref} from "vue";
import {computed, onMounted, ref} from "vue";
import {User} from "@/models/User";
import SortingButton from "@/components/buttons/SortingButton.vue";
import UserDetail from "@/components/UserDetail.vue";

type SortingDirection = 'asc' | 'desc' | 'none';
type SortingMetric = {
  attributeName: keyof User,
  direction: SortingDirection
}

const httpService = new HttpService();
const users: Ref<Array<User>> = ref([]);
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
})

function getUsers(): void {
  httpService.getUsers()
      .then((data: User[]) => {
        users.value = data;
        selectFirstUser()
      })
}

function updateUsers(event: User): void {
  users.value = [...users.value].filter(user => user !== event)
  selectFirstUser()
}

function selectFirstUser(): void {
  if (users.value.length > 0){
    selectedID.value = sortedUsers.value[0].id;
  }
}

function updateSorting(column: string, event: SortingDirection): void {
  sortingMetric.value = {
    attributeName: column as keyof User,
    direction: event
  }
}

function selectUser(index: number): void {
  selectedID.value = index;
}

function sortUsers(users: User[], attributeName: string, direction: 'asc' | 'desc' | 'none'): User[] {
  const compareFunction = (a: User, b: User) => {
    const aValue = a[attributeName as keyof User];
    const bValue = b[attributeName as keyof User];

    if (direction === 'none') return [...users];

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

  return [...users].sort(compareFunction);
}
</script>


<style scoped>
main {
  width: calc(100% - 30px);
  height: calc(100svh - 60px);
  margin: auto;
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
  border-bottom: var(--secondary-color) dashed 2px;
  height: calc(100% - 70px - 190px);
}

.scroll-component {
  height: 100%;
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

  .table-header {
    padding: 20px 20px 0 20px;
    display: flex;
    gap: 10px;
    justify-content: space-between;
    align-items: center;
    height: 40px;
  }

  .username {
    width: 30%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .email {
    width: 50%;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .role {
    text-align: center;
    width: 20%;
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

  .scroll-component {
    height: calc(100% - 50px);
    margin: 10px;
    box-sizing: border-box;
    overflow-y: auto;
  }

  .user {
    display: flex;
    flex-direction: row;
    align-items: center;
    padding: 0 10px 0 10px;
    height: calc(var(--font-size-medium) * 2.5);
    border-radius: var(--border-radius-medium);
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
}
</style>