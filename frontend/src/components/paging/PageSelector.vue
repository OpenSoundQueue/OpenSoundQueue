<template>
  <div class="page-selector-wrapper">
    <div class="selector-element">
      <button @click="previousPage" class="change-page-button">
        <img alt="arrow left" src="@/assets/icons/arrows/keyboard_arrow_left.svg">
      </button>
    </div>
    <div v-for="(page, index) in selection" :key="index" class="selector-element">
      <SelectPage @select-page="setSelectedPage(page)" :label="page + 1" :selected="isPageSelected(page)"/>
    </div>
    <div class="selector-element">
      <button @click="nextPage" class="change-page-button">
        <img alt="arrow right" src="@/assets/icons/arrows/keyboard_arrow_right.svg">
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, ref, watch} from "vue";
import SelectPage from "@/components/paging/SelectPage.vue";
import type {Ref} from "vue";

const props = defineProps<{
  selectablePagesCount: number,
  pageCount: number,
}>();

const emit = defineEmits<{
  selectPage: [pageNumber: number]
}>();

const selectedPage: Ref<number> = ref(0);

const selection = computed((): number[] => {
  // first pages
  if (selectedPage.value <= Math.round(props.selectablePagesCount / 2) - 1) {
    return getFirstNPages(props.selectablePagesCount);
  }

  // last pages
  if (selectedPage.value > props.pageCount - props.selectablePagesCount / 2) {
    return getLastNPages(props.selectablePagesCount);
  }

  return getSelectedPageInTheMiddle(selectedPage.value, props.selectablePagesCount);
});

watch(selectedPage, () => {
  emit("selectPage", selectedPage.value);
})

function nextPage(): void {
  if (selectedPage.value === props.pageCount - 1) return;
  selectedPage.value++;
}

function previousPage(): void {
  if (selectedPage.value === 0) return;
  selectedPage.value--;
}

function setSelectedPage(page: number): void {
  selectedPage.value = page;
}

function isPageSelected(page: number): boolean {
  return page === selectedPage.value;
}

function getSelectedPageInTheMiddle(selectedPage: number, length: number): number[] {
  const pageArray = [];

  let distanceToSelected: { before: number, after: number };

  if (length % 2 === 0) {
    distanceToSelected = {
      before: length / 2,
      after: length / 2 - 1
    };
  } else {
    distanceToSelected = {
      before: Math.floor(length / 2),
      after: Math.floor(length / 2)
    };
  }

  for (let i = selectedPage - distanceToSelected.before; i <= selectedPage + distanceToSelected.after; i++) {
    pageArray.push(i);
  }

  return pageArray;
}

function getLastNPages(n: number): number[] {
  const pages = [];

  for (let i = props.pageCount - n; i < props.pageCount; i++) {
    pages.push(i);
  }

  return pages;
}

function getFirstNPages(n: number): number[] {
  const pages = [];
  for (let i = 0; i < n; i++) {
    pages.push(i);
  }

  return pages;
}
</script>

<style scoped>
.page-selector-wrapper {
  display: flex;
  justify-content: space-between;
  width: 300px;
  margin: auto;
}

.selector-element > button {
  display: flex;
  width: 40px;
  height: 40px;
  font-size: 16px;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  color: var(--text-color);
  border: solid 2px var(--secondary-color);
}

.change-page-button {
  background: transparent;
}

.selector-element > button img {
  width: 22px;
  height: 22px;
}
</style>