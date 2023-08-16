<template>
  <main>
    <h1>Hello World</h1>
    <PageSelector/>
  </main>
</template>

<script setup lang="ts">
import {ref} from "vue";
import PageSelector from "@/components/Paging/PageSelector.vue";

const pages = ref([
  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
]);
</script>

<style>
</style>
