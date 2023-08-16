<template>
  <button @click="nextPage">Next Page</button>
  <div v-for="element in selection">
    <div v-if="element == selectedPage">- {{ element }}</div>
    <div v-else>{{ element }}</div>
  </div>
  <div>{{ }}</div>
  <button @click="previousPage">Previous Page</button>
</template>

<script setup lang="ts">
import {computed, ref} from "vue";

const buttonCount = 7;
const pageCount = 12;
const selectedPage = ref(1);


const selection = computed(() => {
  // first pages
  if (selectedPage.value <= buttonCount / 2) {
    return [
      ...getFirstNPages(buttonCount - 2),
      getPlaceHolder(),
      getLastPage()
    ]
  }

  // last pages
  if (selectedPage.value > pageCount - buttonCount / 2) {
    return [
      getFirstPage(),
      getPlaceHolder(),
      ...getLastNPages(buttonCount - 3)
    ];
  }

  return [
    getFirstPage(),
    getPlaceHolder(),
    ...getSelectedPageInTheMiddle(selectedPage.value, buttonCount - 4),
    getPlaceHolder(),
    getLastPage()
  ];
});

function nextPage() {
  if (selectedPage.value === pageCount) return;
  selectedPage.value++;
}

function previousPage() {
  if (selectedPage.value === 1) return;
  selectedPage.value--;
}

function getSelectedPageInTheMiddle(selectedPage: number, length: number) {
  const pageArray = [];

  if (length % 2 === 0) return [];

  const distanceToSelected = (length - 1) / 2;

  for (let i = selectedPage - distanceToSelected; i <= selectedPage + distanceToSelected; i++) {
    pageArray.push(i);
  }

  return pageArray;
}

function getLastNPages(n: number) {
  const pages = [];

  for (let i = pageCount - n; i <= pageCount; i++) {
    pages.push(i);
  }

  return pages;
}

function getFirstNPages(n: number) {
  const pages: string[] = [];
  for (let i = 1; i <= n; i++) {
    pages.push(`${i}`);
  }

  return pages;
}

function getPlaceHolder(): string {
  return "...";
}

function getFirstPage(): string {
  return "1";
}

function getLastPage(): string {
  return `${pageCount}`;
}
</script>

<style scoped>

</style>