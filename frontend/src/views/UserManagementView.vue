<template>
  <main>
    <div class="user-container">

    </div>
    <div class="detail-container">

    </div>
  </main>
</template>

<script setup lang="ts">
import {HttpService} from "@/services/HttpService";
import {onMounted, ref} from "vue";
import type {Ref} from "vue";
import {User} from "@/models/User";

const httpService = new HttpService();
const users: Ref<User[]> = ref([]);

onMounted(() => {
  getUsers();
})
function getUsers(){
  httpService.getUsers()
      .then((data: User[]) => {
        users.value = data;
      })
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
  padding-bottom: 20px;
}

.detail-container {
  height: calc(100% - 70px - 190px);
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
    grid-template-rows: 100%;
  }

  .user-container {
    grid-column: 2;
    grid-row: 1;
    height: 100%;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
  }

  .detail-container {
    grid-column: 1;
    grid-row: 1;
    height: 100%;
    border-radius: var(--border-radius-big);
    background: var(--secondary-color);
  }
}
</style>