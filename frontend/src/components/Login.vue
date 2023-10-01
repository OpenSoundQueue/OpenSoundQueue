<template>
  <div class="login-container">
    <h2>{{ $translate('loginTitle') }}</h2>
    <form @submit.prevent>
      <div class="input-container">
        <!-- Username -->
        <InputField
            :label="$translate('username')"
            :validationFunction="$validateUsername"
            :errorMessage="$translate('usernameError')"
            :validationMessage="$translate('usernameValidationError')"
            :required="false"
            :placeholder="$translate('usernamePlaceholder')">
        </InputField>
        <!-- Password -->
        <InputField
            :label="$translate('password')"
            :validationFunction="$validatePassword"
            :errorMessage="$translate('passwordError')"
            :validationMessage="$translate('passwordValidationError')"
            :required="false"
            inputType="password"
            :placeholder="$translate('passwordPlaceholder')">
        </InputField>
      </div>
      <OSQButton bStyle="login" :status="formStatus">{{ $translate('login') }}</OSQButton>
    </form>

  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import OSQButton from "@/components/buttons/OSQButton.vue";
import {$validatePassword, $validateUsername} from "@/validationHelper";
import {ref} from "vue";

const formStatus = ref('active')
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

.input-container{
  gap: 20px;
  margin: 40px 0 40px 0;
}
</style>