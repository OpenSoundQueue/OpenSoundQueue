<template>
  <div class="registration-form-wrapper">
    <h2>Create an account</h2>
    <form @submit.prevent>
      <!-- Username -->
      <InputField
          v-model="username.input"
          :label="$translate('username.title')"
          :validation-function="$validateUsername"
          :validation-message="$translate('username.validation')"
          :required="false"
          :placeholder="$translate('username.placeholder')"
      />

      <!-- Email -->
      <InputField
          v-model="email.input"
          :label="$translate('email.title')"
          :validation-function="$validateEmail"
          :validation-message="$translate('email.validation')"
          :required="false"
          :placeholder="$translate('email.placeholder')"
      />

      <!-- Password -->
      <InputField
          v-model="password.input"
          :label="$translate('password.title')"
          :validation-function="$validatePassword"
          :validation-message="$translate('password.validation')"
          :required="false"
          :placeholder="$translate('password.placeholder')"
          input-type="password"
      />

      <!-- Password Repeat -->
      <InputField
          v-model="passwordRepeat.input"
          :label="$translate('passwordRepeat.title')"
          :validation-function="validatePasswordRepeat"
          :validation-message="$translate('passwordRepeat.validation')"
          :required="false"
          :placeholder="$translate('passwordRepeat.placeholder')"
          input-type="password"
      />
      <div class="submit-container">
        <DefaultButton :is-disabled="formStatus || waitingForResponse" text="Continue" @click="createAccount"/>
      </div>
    </form>
    <div class="link-container">
      <router-link class="link" to="/login">{{ $translate("alreadyHaveAnAccount") }}</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import {computed, ref} from "vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import {validateEmail, validatePassword, validateUsername} from "@/plugins/ValidationPlugin";
import {HttpService} from "@/services/HttpService";

const httpService = new HttpService();

const emits = defineEmits<{
  continue: []
}>()

const waitingForResponse = ref(false);

const username = ref({
  input: "",
  errorStatus: false,
  message: ""
});

const email = ref({
  input: "",
  errorStatus: false,
  message: ""
});

const password = ref({
  input: "",
  errorStatus: false,
  message: ""
});

const passwordRepeat = ref({
  input: "",
  errorStatus: false,
  message: ""
});

const formStatus = computed(() => {
  return !(validateUsername(username.value.input)() &&
      validateEmail(email.value.input)() &&
      validatePassword(password.value.input)() &&
      validatePasswordRepeat(passwordRepeat.value.input)());
})

function validatePasswordRepeat(value: string) {
  return () => {
    if (value.length === 0) {
      return true
    }

    return value === password.value.input;
  }
}

async function createAccount() {
  waitingForResponse.value = true;

  await httpService.postRegisterCreateAccount(username.value.input, email.value.input, password.value.input)
      .then((id: number) => {
        console.log("id");
        emits("continue");
      });

  waitingForResponse.value = false;
}
</script>

<style scoped>
.registration-form-wrapper {
  padding: 0 40px 0 40px;
  margin: 0 auto 0 auto;
}

h2 {
  margin-bottom: 0;
  margin-top: 20px;
}


form {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 40px;
}

.submit-container {
  margin-top: 20px;
}

.link-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 40px 0 40px 0;
}

.link {
  text-decoration: none;
  color: var(--pink);
  text-align: center;
}

.link:hover {
  text-decoration: underline;
}
</style>