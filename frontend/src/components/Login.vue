<template>
  <div class="login-container">
    <h2>{{ $translate('loginTitle') }}</h2>
    <form @submit.prevent>
      <div class="input-container">
        <!-- Username -->
        <InputField
            v-model="username"
            :label="$translate('username')"
            :validationFunction="$validateUsername"
            :errorMessage="$translate('usernameError')"
            :validationMessage="$translate('usernameValidationError')"
            :required="false"
            :placeholder="$translate('usernamePlaceholder')">
        </InputField>
        <!-- Password -->
        <InputField v-if="props.requireAuth"
                    v-model="password"
                    :label="$translate('password')"
                    :validationFunction="$validatePassword"
                    :errorMessage="$translate('passwordError')"
                    :validationMessage="$translate('passwordValidationError')"
                    :required="false"
                    inputType="password"
                    :placeholder="$translate('passwordPlaceholder')">
        </InputField>
        <!-- EntryCode -->
        <InputField v-if="props.isPrivate"
                    v-model="entryCode"
                    :label="$translate('entrycode')"
                    :validationFunction="$validateEntryCode"
                    :errorMessage="$translate('entryCodeError')"
                    :validationMessage="$translate('entryCodeValidationError')"
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
import {HttpService} from "@/services/HttpService";
import {$validatePassword, $validateUsername, $validateEntryCode} from "@/validationHelper";
import {computed, ref} from "vue";

interface Props {
  requireAuth?: boolean,
  isPrivate?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  requireAuth: false,
  isPrivate: false
});

const username = ref("")
const password = ref("")
const entryCode = ref("")

const formStatus = computed(() => {
  let lengthCheck = username.value.length > 0
  let validationCheck = $validateUsername(username.value)()

  switch (true) {
    case props.requireAuth && props.isPrivate:
      lengthCheck &&= password.value.length > 0
      lengthCheck &&= entryCode.value.length > 0
      validationCheck &&= $validateEntryCode(password.value)()
      validationCheck &&= $validateEntryCode(entryCode.value)()
      break;
    case props.isPrivate:
      lengthCheck &&= entryCode.value.length > 0
      validationCheck &&= $validateEntryCode(entryCode.value)()
      break;
    case props.requireAuth:
      lengthCheck &&= password.value.length > 0
      validationCheck &&= $validateEntryCode(password.value)()
      break;
  }

  return (lengthCheck && validationCheck) ? "active" : "inactive"
})

async function loginCall() {
  const httpService = new HttpService()
  let response;
  console.log("RUN LOGIN")
  if (formStatus.value === "active") {
    switch (true) {
      case props.requireAuth && props.isPrivate:
        await httpService.postPrivateAuthLogin(username.value, password.value, entryCode.value)
        // TODO: add error-handling
        break;
      case props.isPrivate:
        await httpService.postPrivateLogin(username.value, entryCode.value)
        // TODO: add error-handling
        break;
      case props.requireAuth:
        await httpService.postPublicAuthLogin(username.value, password.value)
        // TODO: add error-handling
        break;
      default:
        await httpService.postPublicLogin(username.value)
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