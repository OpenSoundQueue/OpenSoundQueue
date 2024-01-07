<template>
  <div class="container">
    <div class="field-wrapper">
      <div class="field-title">{{ translate('username.title') }}</div>
      <div>{{ user.username }}</div>
    </div>
    <div class="field-wrapper">
      <div class="field-title">{{ translate('email.title') }}</div>
      <div>{{ user.email }}</div>
    </div>
    <div class="field-wrapper">
      <div class="field-title">{{ translate('registration.verified') }}</div>
      <div>{{ translate('registration.verified') }}</div>
    </div>
    <div class="verification">
      <img src="@/assets/icons/user_verified.svg" :alt="translate('altTexts.userVerified')">
      <div>{{ translate('registration.verifiedCTO') }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {registration} from "@/store/store";
import {onMounted, ref} from "vue";
import router from "@/router";
import {translate} from "@/plugins/TranslationPlugin";

const user = ref({
  username: "",
  email: "",
  password: ""
})

onMounted(() => {
  if (registration.email == "" || registration.username == "" || registration.password == "") {
    registration.username = "";
    registration.email = "";
    registration.password = "";
    registration.timestamp = 0;
    registration.state = "validation";
    router.go(0)
  }
  user.value = {
    username: registration.username,
    email: registration.email,
    password: registration.email
  }
})
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: auto 0 auto 0;
  padding: 25px;
}

.field-wrapper {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
  justify-content: left;
  width: 100%;
}

.field-title {
  color: var(--pink);
}

.verification {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: calc(100% - 50px);

  color: var(--cool-gray);
  text-align: center;

  padding: 20px;
  border-radius: var(--border-radius-medium);
  border: solid 2px var(--cool-gray);

  img {
    height: 50px;
    aspect-ratio: 1;
  }
}
</style>