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
      <button>{{ $translate("login") }}</button>
    </form>

  </div>
</template>

<script setup lang="ts">
import InputField from "@/components/inputs/InputField.vue";
import {$validatePassword, $validateUsername} from "@/validationHelper";


</script>

<style scoped>
h2 {
  margin-bottom: 0;
  margin-top: 20px;
}

.login-container {
  padding: 0 60px 0 60px;
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