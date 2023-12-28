<template>
<div>{{ response }}</div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import {HttpService} from "@/services/HttpService";

const httpService = new HttpService()
const response = ref();
const response1 = ref();
const response2 = ref();

onMounted(()=>{

  httpService.setSources(["youtube"])
      .then((data)=>{
        response.value = data
      })
})
</script>

<style scoped>

</style>