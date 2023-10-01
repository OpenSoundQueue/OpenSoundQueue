<template>
  <div class="login-container">

    <form @submit.prevent>
      <!-- Username -->
      <label>Benutzername</label>
      <input class="username input-field" type="text"
             v-model="v$.form.username.$model"
             :class="status(v$.form.username)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.username.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>

      <!-- Password -->
      <label>Passwort</label>
      <input class="password input-field" type="password"
             v-model="v$.form.password.$model"
             :class="status(v$.form.password)">
      <!-- error message -->
      <div class="input-errors" v-for="(error, index) of v$.form.password.$errors" :key="index">
        <div class="error-msg">{{ error.$message }}</div>
      </div>
      <button>{{  $translate("login") }}</button>
    </form>

  </div>
</template>

<script setup lang="ts">
import useVuelidate from 'vuelidate/core'
import {required$, email$, passwordMinLength$, passwordSameAs$} from "@/validators.js";
import {ref, watch} from "vue";

const props = defineProps({
  passwordRequired: Boolean
})
type Form = {
  username: string,
  password: string
}
let v$ = useVuelidate();
let form:Form = {
  username:"",
  password:""
}

let error:boolean =  false
let errorMessage:string =  ""
let state:string = "inactive"

const validations = {
  form: {
    username: {
      required$
    },
    password: {
      required$,
      min: passwordMinLength$(8)
    },
  }
}

function status(validation) {
  return {
    error: validation.$error,
    dirty: validation.$dirty
  }
}

async function sendRegistration() {
  resetError();
  state = "waiting";

  state = "active";
}

function resetError() {
  error = false;
  errorMessage = "";
}

function setError(message:string) {
  error = true;
  errorMessage = message;
}

watch('v$.form.$invalid'(),()=>{
  state = v$.form.$invalid ? 'inactive' : 'active'
})
</script>

<style scoped>

</style>