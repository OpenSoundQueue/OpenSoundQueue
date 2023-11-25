<template>
  <div class="registration-form-wrapper">
    <h2>{{ $translate('registration.heading') }}</h2>
    <div class="entry-container">
      <div>{{ user.username }}</div>
      <div @click="emits('change')" class="link change">{{ $translate('registration.change') }}</div>
    </div>
    <div class="entry-container">
      <div>{{ user.email }}</div>
      <div @click="emits('change')" class="link change">{{ $translate('registration.change') }}</div>
    </div>
    <form @submit.prevent>
      <div class="input-container">
        <div class="explanation">{{ $translate('registration.explanation') }}</div>
        <InputField
            v-model="verificationCode.input"
            :required="false"
            :placeholder="$translate('emailValidation.placeholder')"
        />
      </div>
      <div class="submit-container">
        <DefaultButton :is-disabled="formStatus || waitingForResponse" :text="translate('registration.createAccount')"
                       @click="sendVerification"/>
      </div>
    </form>
    <div class="no-code-section">
      <div>{{ translate('registration.noCode.text') }}</div>
      <router-link class="link" to="/login">{{ $translate("registration.noCode.button") }}</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import {computed, onMounted, ref} from "vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import {HttpService} from "@/services/HttpService";
import {translate} from "@/plugins/TranslationPlugin";

type Form = {
  username: string,
  email: string,
  password: string
}

const httpService = new HttpService();

const emits = defineEmits<{
  change: []
}>()

const waitingForResponse = ref(false);

const user = ref({
  username: "",
  email: "",
  password: ""
})

const verificationCode = ref({
  input: "",
  errorStatus: false,
  message: ""
});

const formStatus = computed(() => {
  return verificationCode.value.input.length == 0;
})

onMounted(() => {
  const form: Form = getForm();
  user.value = {
    username: form.username,
    email: form.email,
    password: form.email
  }
})

async function sendVerification() {
  waitingForResponse.value = true;

  await httpService.postRegisterCreateAccount(username.value.input, email.value.input, password.value.input)
      .then((id: number) => {
        console.log(id);
        localStorage.removeItem("form")
      });

  waitingForResponse.value = false;
}

function getForm(): Form {
  const form = localStorage.getItem("form")
  if (!form)
    return {username: "", email: "", password: ""}
  else
    return JSON.parse(form)
}
</script>

<style scoped>
.registration-form-wrapper {
  padding: 0 40px 0 40px;
  margin: 0 auto 0 auto;
}

.entry-container {
  display: flex;
  flex-direction: row;
  gap: 20px;
  padding-top: 20px;

  font-size: calc(var(--font-size-medium) * 1.2);
}

h2 {
  margin-top: 20px;
  margin-bottom: 20px;
}


form {
  display: flex;
  flex-direction: column;
  margin-top: 40px;
}

.submit-container {
  margin-top: 20px;
}

.input-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.no-code-section {
  display: flex;
  flex-direction: column;
  text-align: center;
  gap: 20px;
  padding: 40px 0 40px 0;
}

.link {
  text-decoration: none;
  color: var(--pink);
  text-align: center;
}

.change {
  user-select: none;
}

.change:hover {
  cursor: pointer;
}

.link:hover {
  text-decoration: underline;
}
</style>