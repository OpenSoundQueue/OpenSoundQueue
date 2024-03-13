<template>
  <div class="login-container">
    <h2>{{ $translate('loginTitle') }}</h2>
    <form @submit.prevent>
      <div class="input-container">
        <!-- Username -->
        <InputField
            ref="usernameComponent"
            v-model="username"
            :label="$translate('username.title')"
            :error-status="errorStatus[0]"
            :error-message="usernameError"
            :validation-function="$isEmpty"
            :validation-message="$translate('username.empty')"
            :required="false"
            :placeholder="$translate('username.placeholder')">
        </InputField>
        <!-- Password -->
        <InputField v-if="props.requireAuth"
                    v-model="password"
                    :label="$translate('password.title')"
                    :error-status="errorStatus[1]"
                    :error-message="$translate('password.error')"
                    :validation-function="$isEmpty"
                    :validation-message="$translate('password.empty')"
                    :required="false"
                    input-type="password"
                    :placeholder="$translate('password.placeholder')">
        </InputField>
        <!-- EntryCode -->
        <InputField v-if="props.isPrivate"
                    v-model="entryCode"
                    :manualValue="entryCode"
                    :label="$translate('entryCode.title')"
                    :error-status="errorStatus[2]"
                    :error-message="$translate('entryCode.error')"
                    :validation-function="$isEmpty"
                    :validation-message="$translate('entryCode.empty')"
                    :required="false"
                    :placeholder="$translate('entryCode.placeholder')">
        </InputField>
      </div>
      <DefaultButton :text="$translate('login')" @click="loginCall"/>
    </form>
  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import {onMounted, ref} from "vue";
import type {Ref} from "vue";
import {useRoute} from "vue-router";
import router from "@/router";
import {translate} from "@/plugins/TranslationPlugin";
import {HttpService} from "@/services/HttpService";
import * as cookieService from "@/services/cookieService";
import DefaultButton from "@/components/buttons/DefaultButton.vue";

interface Props {
  requireAuth?: boolean,
  isPrivate?: boolean,
}

const props = withDefaults(defineProps<Props>(), {
  requireAuth: false,
  isPrivate: false,
});

const httpService = new HttpService();

const username = ref("")
const usernameComponent = ref(null);
const usernameError = ref(translate('username.error'))
const password = ref("")
const entryCode = ref("")
const errorStatus: Ref<Array<boolean>> = ref([])

onMounted(() => {
  const route = useRoute()
  errorStatus.value = new Array(3).fill(false);
  if (route.params.entryCode != undefined) entryCode.value = route.params.entryCode + "";
})

async function loginCall() {
    errorStatus.value = new Array(3).fill(false);
    switch (true) {
      case props.requireAuth && props.isPrivate:
        await httpService.postPrivateAuthLogin(username.value, password.value, entryCode.value)
            .then(apiKey => {
              cookieService.clearApiKey();
              cookieService.setApiKey(apiKey);
              router.push("/home");
            })
            .catch(error => {
              switch (error) {
                case 400:
                  // wrong credentials
                  errorStatus.value[0] = true;
                  errorStatus.value[1] = true;
                  break;
                case 401:
                  // wrong entryCode
                  errorStatus.value[2] = true;
                  break;
              }
            })
        break;
      case props.isPrivate:
        await httpService.postPrivateLogin(username.value, entryCode.value)
            .then(apiKey => {
              cookieService.clearApiKey();
              cookieService.setApiKey(apiKey);
              router.push("/home");
            })
            .catch(error => {
              switch (error) {
                case 400:
                  // wrong username
                  usernameError.value = translate("username.taken")
                  errorStatus.value[0] = true;
                  break;
                case 401:
                  // wrong entryCode
                  errorStatus.value[2] = true;
                  break;
              }
            })
        break;
      case props.requireAuth:
        await httpService.postPublicAuthLogin(username.value, password.value)
            .then(apiKey => {
              cookieService.clearApiKey();
              cookieService.setApiKey(apiKey);
              router.push("/home");
            })
            .catch(error => {
              // wrong credentials
              usernameError.value = translate("username.error")
              errorStatus.value = [true, true, true]
            })
        break;
      default:
        await httpService.postPublicLogin(username.value)
            .then(apiKey => {
              cookieService.clearApiKey();
              cookieService.setApiKey(apiKey);
              router.push("/home");
            })
            .catch(() => {
              // wrong username
              usernameError.value = translate("username.taken")
              errorStatus.value[0] = true;
            })
        break;
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