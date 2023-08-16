<template>
  <div class="page-selector-wrapper">
    <button @click="previousPage" class="change-page-button">
      <img alt="arrow left" src="@/assets/icons/arrows/keyboard_arrow_left.svg">
    </button>
    <div v-for="page in selection">
      <SelectPage @select-page="setSelectedPage(page)" :label="page" :selected="isPageSelected(page)"/>
    </div>
    <div>{{ }}</div>
    <button @click="nextPage" class="change-page-button">
      <img alt="arrow right" src="@/assets/icons/arrows/keyboard_arrow_right.svg">
    </button>
  </div>
</template>

<script setup lang="ts">
import {computed, ref} from "vue";
import type {Ref} from "vue";
import SelectPage from "@/components/Paging/SelectPage.vue";

const buttonCount = 7;
const pageCount = 122;
const selectedPage: Ref<number> = ref(1);


const selection = computed((): (number | string)[] => {
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

function nextPage(): void {
  if (selectedPage.value === pageCount) return;
  selectedPage.value++;
}

function previousPage(): void {
  if (selectedPage.value === 1) return;
  selectedPage.value--;
}

function setSelectedPage(page: string | number): void {
  if (typeof page === "string") {
    return;
  }
  selectedPage.value = page;
}

function isPageSelected(page: string | number): boolean {
  if (typeof page === "string") {
    return false;
  }

  return page === selectedPage.value;
}

function getSelectedPageInTheMiddle(selectedPage: number, length: number): number[] {
  const pageArray = [];

  if (length % 2 === 0) return [];

  const distanceToSelected = (length - 1) / 2;

  for (let i = selectedPage - distanceToSelected; i <= selectedPage + distanceToSelected; i++) {
    pageArray.push(i);
  }

  return pageArray;
}

function getLastNPages(n: number): number[] {
  const pages = [];

  for (let i = pageCount - n; i <= pageCount; i++) {
    pages.push(i);
  }

  return pages;
}

function getFirstNPages(n: number): number[] {
  const pages = [];
  for (let i = 1; i <= n; i++) {
    pages.push(i);
  }

  return pages;
}

function getPlaceHolder(): string {
  return "...";
}

function getFirstPage(): number {
  return 1;
}

function getLastPage(): number {
  return pageCount;
}
</script>

<style scoped>
.page-selector-wrapper {
  display: flex;
  justify-content: space-evenly;
  width: 300px;
  margin: auto;
}

.change-page-button {
  display: flex;
  width: 32px;
  height: 32px;
  justify-content: center;
  align-items: center;
  border: none;
  border-radius: 50%;
  color: var(--text-color);
  background: none;;
}

.change-page-button img {
  width: 22px;
  height: 22px;
}
</style>