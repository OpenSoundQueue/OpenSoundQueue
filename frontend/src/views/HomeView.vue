<template>
  <main>
    <PageSelector :page-count="12" :selectable-pages-count="5"/>
  </main>
</template>

<script setup lang="ts">
import {ref} from "vue";
import PageSelector from "@/components/paging/PageSelector.vue";

const pages = ref([
  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
]);
</script>

<style>
</style>
