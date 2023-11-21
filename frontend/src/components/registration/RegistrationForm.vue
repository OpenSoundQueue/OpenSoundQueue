<template>
  <div class="registration-form-wrapper">
    <form @submit.prevent>
      <!-- Username -->
      <InputField
          v-model="username.input"
          :label="$translate('username.title')"
          :validation-function="$validateUsername"
          :validation-message="$translate('username.validation')"
          :required="false"
          :placeholder="$translate('username.placeholder')">
      </InputField>

      <!-- Email -->
      <InputField
          v-model="email.input"
          :label="$translate('email.title')"
          :validation-function="$validateEmail"
          :validation-message="$translate('email.validation')"
          :required="false"
          :placeholder="$translate('email.placeholder')">
      </InputField>

      <!-- Password -->
      <InputField
          v-model="password.input"
          :label="$translate('password.title')"
          :validation-function="$validatePassword"
          :validation-message="$translate('password.validation')"
          :required="false"
          :placeholder="$translate('password.placeholder')">
      </InputField>

      <!-- Password Repeat -->
      <InputField
          v-model="passwordRepeat.input"
          :label="$translate('passwordRepeat.title')"
          :validation-function="validatePasswordRepeat"
          :validation-message="$translate('passwordRepeat.validation')"
          :required="false"
          :placeholder="$translate('passwordRepeat.placeholder')"
      />

      <DefaultButton :is-disabled="formStatus" text="Continue"/>

      <button @click="emits('continue')">Continue</button>
    </form>
  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import {computed, ref} from "vue";
import DefaultButton from "@/components/buttons/DefaultButton.vue";
import {validateEmail, validatePassword, validateUsername} from "@/plugins/ValidationPlugin";

const emits = defineEmits<{
  continue: []
}>()

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

</script>

<style scoped>
.registration-form-wrapper {
  padding: 0 40px 0 40px;
  margin: 0 auto 0 auto;
}
</style>