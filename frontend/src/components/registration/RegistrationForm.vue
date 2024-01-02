<template>
  <div class="registration-form-wrapper">
    <h2 v-if="mode!=='installation'">{{ $translate('registration.heading') }}</h2>
    <form :class="[mode=='installation'?'installation':'']" @submit.prevent>
      <!-- Username -->
      <InputField
          v-model="username.input"
          :manual-value="username.input"
          :label="$translate('username.title')"
          :validation-function="$validateUsername"
          :validation-message="$translate('username.validation')"
          :error-status="username.errorStatus"
          :error-message="$translate('username.taken')"
          :required="false"
          :placeholder="$translate('username.placeholder')"
          @user-input="username.errorStatus=false"
      />

      <!-- Email -->
      <InputField
          v-model="email.input"
          :manual-value="email.input"
          :label="$translate('email.title')"
          :validation-function="$validateEmail"
          :validation-message="$translate('email.validation')"
          :error-status="email.errorStatus"
          :error-message="$translate('email.taken')"
          :required="false"
          :placeholder="$translate('email.placeholder')"
          @user-input="email.errorStatus=false"
      />

      <!-- Password -->
      <InputField
          v-model="password.input"
          :manual-value="password.input"
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
          :manual-value="passwordRepeat.input"
          :label="$translate('passwordRepeat.title')"
          :validation-function="validatePasswordRepeat"
          :validation-message="$translate('passwordRepeat.validation')"
          :required="false"
          :placeholder="$translate('passwordRepeat.placeholder')"
          input-type="password"
      />
      <div class="submit-container">
        <DefaultButton :is-disabled="formStatus || waitingForResponse" :text="translate('registration.continue')"
                       @click="createAccount"/>
      </div>
    </form>
    <div v-if="mode!=='installation'" class="link-container">
      <router-link class="link" to="/login">{{ $translate("alreadyHaveAnAccount") }}</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import {computed, onMounted, ref} from "vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import {validateEmail, validatePassword, validateUsername} from "@/plugins/ValidationPlugin";
import {HttpService} from "@/services/HttpService";
import {translate} from "@/plugins/TranslationPlugin";
import {registration} from "@/store/store";

defineProps<{
  mode: "installation"
}>();

type Form = {
  username: string,
  email: string,
  password: string
  timestamp: number
}

const httpService = new HttpService();

const emits = defineEmits<{
  continue: []
}>()

const waitingForResponse = ref(false);

const username = ref({
  input: registration.username,
  errorStatus: false,
  message: ""
});

const email = ref({
  input: registration.email,
  errorStatus: false,
  message: ""
});

const password = ref({
  input: registration.password,
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
      validatePasswordRepeat(passwordRepeat.value.input)() &&
      username.value.input != "" &&
      email.value.input != "" &&
      password.value.input != "" &&
      passwordRepeat.value.input != "");
})

onMounted(() => {
  username.value.input = registration.username;
  email.value.input = registration.email;
  password.value.input = registration.password;
  passwordRepeat.value.input = registration.password;
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
      .then(() => {
        registration.username = username.value.input;
        registration.email = email.value.input;
        registration.password = password.value.input;
        registration.timestamp = Date.now();
        registration.state = "validation";
        emits("continue");
      })
      .catch((error) => {
        if (error.error.toLowerCase().includes("username"))
          username.value.errorStatus = true;
        if (error.error.toLowerCase().includes("email"))
          email.value.errorStatus = true;
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

.installation{
  margin-top: 0;
}
</style>