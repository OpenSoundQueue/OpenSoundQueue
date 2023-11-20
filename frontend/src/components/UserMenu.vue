<template>
  <div class="user-menu-wrapper">
    <div class="user-menu-container">
      <div class="close-container">
        <img @click="close" src="@/assets/menu/close.svg"/>
      </div>
      <div v-if="user">
        <p class="username">Quantompixel</p>
        <p class="email">quantompxiel@adfa.com</p>
        <p class="role"><span class="dot"></span>Test</p>
        <div class="button">
          <DefaultButton text="Logout"/>
        </div>
      </div>
      <div v-else>
        <p>You are not logged in</p>
        <div class="button">
          <DefaultButton text="Login" @click="router.push('/login')"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import {HttpService} from "@/services/HttpService";
import type {Ref} from "vue";
import {onMounted, ref} from "vue";
import type {User} from "@/models/User";
import router from "@/router";

const httpService = new HttpService();

const user: Ref<User | null> = ref(null);

const emit = defineEmits<{
  close: []
}>();

onMounted(() => {
  httpService.getSelf()
      .then((data: User) => user.value = data)
      .catch(() => {

      })
})

function close() {
  emit("close");
}
</script>

<style scoped>
.user-menu-wrapper {
  position: fixed;
  width: 100%;
  right: 0;
  top: 0;
  padding: 5px;
  box-sizing: border-box;
}

.user-menu-container {
  background: var(--secondary-color);
  border: 2px solid var(--tertiary-color);
  border-radius: var(--border-radius-big);
  display: flex;
  flex-direction: column;
  padding: 10px 20px 20px 20px;
  box-sizing: border-box;
}

.close-container {
  display: flex;
  justify-content: flex-end;
}

.username, .email, .role {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin: 5px 0;
}

.dot {
  height: 5px;
  aspect-ratio: 1/1;
  background-color: var(--tertiary-color);
  border-radius: 50%;
  display: inline-block;
  margin-right: 5px;
}

.role {
  font-size: var(--font-size-small);
  background-color: var(--background-color);
  width: fit-content;
  max-width: 20%;
  box-sizing: border-box;
  padding: 5px;
  border-radius: var(--border-radius-small);
  text-align: left;
}

.button {
  margin-top: 15px;
}

@media screen and (min-width: 400px) {
  .user-menu-wrapper {
    width: 400px;
  }
}
</style>