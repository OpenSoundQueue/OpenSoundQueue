<template>
  <div class="registration-form-wrapper">
    <h2 v-if="mode!=='installation'">{{ $translate('registration.heading') }}</h2>
    <div class="entry-container">
      <div>{{ user.username }}</div>
      <div @click="emit('change')" class="link change">{{ $translate('registration.change') }}</div>
    </div>
    <div class="entry-container">
      <div>{{ user.email }}</div>
      <div @click="emit('change')" class="link change">{{ $translate('registration.change') }}</div>
    </div>
    <form @submit.prevent>
      <div class="input-container">
        <div class="explanation">{{ $translate('registration.explanation') }}</div>
        <InputField
            v-model="verificationCode.input"
            :required="false"
            :placeholder="$translate('emailVerification.placeholder')"
            :error-status="verificationCode.errorStatus"
            :error-message="$translate('emailVerification.error')"
            @user-input="verificationCode.errorStatus = false"
        />
      </div>
      <div class="submit-container">
        <DefaultButton :is-disabled="formStatus || waitingForResponseValidation" :text="translate('registration.createAccount')"
                       @click="sendVerification"/>
      </div>
    </form>
    <div class="no-code-section">
      <div>{{ translate('registration.noCode.text') }}</div>
      <div class="link" @click="resendMail">{{ $translate("registration.noCode.button") }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import {computed, onMounted, ref} from "vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import {HttpService} from "@/services/HttpService";
import {translate} from "@/plugins/TranslationPlugin";
import * as cookieService from "@/services/cookieService"
import {ToastService} from "@/services/ToastService";
import router from "@/router";
import {registration} from "@/store/store";

defineProps<{
  mode: "installation"
}>();

type Form = {
  username: string,
  email: string,
  password: string,
  timestamp: number
}

const httpService = new HttpService();

const emit = defineEmits<{
  change: [],
  validated:[]
}>()

const waitingForResponseValidation = ref(false);
const waitingForResponseResend = ref(false);

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
  if (registration.email == "" || registration.username == "" || registration.password == "") {
    registration.username = "";
    registration.email = "";
    registration.password = "";
    registration.timestamp = 0;
    registration.state = "input";
    router.go(0)
  }
  user.value = {
    username: registration.username,
    email: registration.email,
    password: registration.email
  }
})

async function sendVerification() {
  waitingForResponseValidation.value = true;

  await httpService.postRegisterVerify(verificationCode.value.input, user.value.email)
      .then((apiKey: string) => {
        cookieService.setApiKey(apiKey);
        ToastService.sendNotification(translate('registration.success'), 'success', 3000)
        emit("validated");
      })
      .catch(() => {
        verificationCode.value.errorStatus = true;
      });

  waitingForResponseValidation.value = false;
}
async function resendMail() {
  if (waitingForResponseResend.value) return;

  waitingForResponseResend.value = true;

  await httpService.postResendMail(user.value.email)
      .then(() => {
        ToastService.sendNotification(translate('registration.noCode.success'), 'success', 3000)
      })
      .catch(() => {
        ToastService.sendNotification(translate('registration.noCode.error'), 'error', 3000)
      });

  waitingForResponseResend.value = false;
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
  user-select: none;
}

.change {
  user-select: none;
}

.change:hover {
  cursor: pointer;
}

.link:hover {
  cursor: pointer;
  text-decoration: underline;
}
</style>