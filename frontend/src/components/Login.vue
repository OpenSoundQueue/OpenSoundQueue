<template>
  <div class="login-container">
    <h2>{{ $translate('loginTitle') }}</h2>
    <form @submit.prevent>
      <div class="input-container">
        <!-- Username -->
        <InputField
            ref="usernameComponent"
            v-model="username"
            :label="$translate('username')"
            :error-status="errorStatus"
            :error-message="$translate('usernameError')"
            :validation-function="$validateUsername"
            :validation-message="$translate('usernameValidationError')"
            :required="false"
            :placeholder="$translate('usernamePlaceholder')">
        </InputField>
        <!-- Password -->
        <InputField v-if="props.requireAuth"
                    v-model="password"
                    :label="$translate('password')"
                    :error-status="errorStatus"
                    :error-fessage="$translate('passwordError')"
                    :validation-function="$validatePassword"
                    :validation-message="$translate('passwordValidationError')"
                    :required="false"
                    input-type="password"
                    :placeholder="$translate('passwordPlaceholder')">
        </InputField>
        <!-- EntryCode -->
        <InputField v-if="props.isPrivate"
                    v-model="entryCode"
                    :manualValue="entryCode"
                    :label="$translate('entrycode')"
                    :error-status="errorStatus"
                    :error-message="$translate('entryCodeError')"
                    :validation-function="$validateEntryCode"
                    :validation-message="$translate('entryCodeValidationError')"
                    :required="false"
                    :placeholder="$translate('entryCodePlaceholder')">
        </InputField>
      </div>
      <OSQButton bStyle="login" :status="formStatus" @click="loginCall">{{ $translate('login') }}</OSQButton>
    </form>
  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import OSQButton from "@/components/buttons/OSQButton.vue";
import {computed, onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {validateEntryCode, validatePassword, validateUsername} from "@/plugins/ValidationPlugin";
import router from "@/router";

interface Props {
  requireAuth?: boolean,
  isPrivate?: boolean,
}

const props = withDefaults(defineProps<Props>(), {
  requireAuth: false,
  isPrivate: false,
});

const username = ref("")
const usernameComponent = ref(null);
const password = ref("")
const entryCode = ref("")
const errorStatus = ref(false);

onMounted(() => {
  const route = useRoute()
  if (route.params.entryCode != undefined) entryCode.value = route.params.entryCode + "";
})



const formStatus = computed(() => {
  let validationCheck = validateUsername(username.value)()

  switch (true) {
    case props.requireAuth && props.isPrivate:
      validationCheck &&= validatePassword(password.value)()
      validationCheck &&= validateEntryCode(entryCode.value)()
      break;
    case props.isPrivate:
      validationCheck &&= validateEntryCode(entryCode.value)()
      break;
    case props.requireAuth:
      validationCheck &&= validatePassword(password.value)()
      break;
  }

  return validationCheck ? "active" : "inactive"
})

async function loginCall() {
  if (formStatus.value === "active") {
    switch (true) {
      case props.requireAuth && props.isPrivate:
        break;
      case props.isPrivate:
        //await httpService.postPrivateLogin(username.value, entryCode.value)
        // TODO: add error-handling
        break;
      case props.requireAuth:
        await fetch(`http://${window.location.hostname}:8080/api/login/public/auth`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            "username": username.value,
            "password": password.value
          }),
          credentials: 'same-origin'
        })
            .then(response =>{
              if (response.status === 200){
                const data = response.json();
                document.cookie = "sessionKey= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";
                document.cookie = "sessionKey=" + data.apiKey+ "; path=/";
                router.push("/home");
              }else{
                console.log(response.status)
                errorStatus.value = true;
              }
            })
        break;
      default:
        //await httpService.postPublicLogin(username.value)
        // TODO: add error-handling
        break;
    }
  }
}
</script>

<style scoped>
h2 {
  margin-bottom: 0;
  margin-top: 20px;
}

.login-container {
  padding: 0 40px 0 40px;
  margin: 0 auto 0 auto;
}

.input-container, form {
  display: flex;
  flex-direction: column;
}

.input-container {
  gap: 20px;
  margin: 40px 0 40px 0;
}
</style>